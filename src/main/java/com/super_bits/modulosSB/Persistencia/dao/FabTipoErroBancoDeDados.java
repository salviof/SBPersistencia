/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreListas;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreReflexaoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.UtilSBCoreErros;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaDeEntidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.ItemGenerico;
import java.io.NotSerializableException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.PropertyValueException;
import org.hibernate.TransientPropertyValueException;
import org.hibernate.exception.ConstraintViolationException;

/**
 *
 * @author desenvolvedor
 */
public enum FabTipoErroBancoDeDados {

    ERRO_DE_CONEXAO,
    CHAVE_EXTRANGEIRA,
    CHAVE_EXTRANGEIRA_MYSQL_NATIVO,
    NAO_PODE_SER_NULO,
    VALOR_INCOMPATIVEL,
    INFORMACAO_DUPLICADA,
    EXCEDEU_TAMANHO_MAXIMO,
    TIPO_INVALIDO,
    INDEFINIDO,
    OBJETO_FILHO_TRANSIENT;

    public static FabTipoErroBancoDeDados getTipoErroViaMensagem(Throwable erro) {
        Throwable causaInicial = erro.getCause();
        if (causaInicial != null) {
            if (erro.getCause() instanceof PropertyValueException) {
                PropertyValueException propInvalida = (PropertyValueException) erro.getCause();
                if (propInvalida.getMessage().contains("not-null")) {
                    return NAO_PODE_SER_NULO;
                } else {
                    return VALOR_INCOMPATIVEL;
                }
            }

            if (causaInicial instanceof ConstraintViolationException) {
                //DETECTANDO CAMPO DUPLICADO EM AMBIENTE MYSQL
                Throwable causaSuperior = causaInicial.getCause();
                if (causaSuperior != null) {

                    if (causaSuperior.getMessage() != null) {
                        String mensagemBancoDeDados = causaSuperior.getMessage();
                        if (mensagemBancoDeDados.contains("Duplicate entry") && mensagemBancoDeDados.contains("UK_")) {
                            return INFORMACAO_DUPLICADA;

                        }
                    }
                }
                return CHAVE_EXTRANGEIRA;

            }

        }

        Throwable causa = ExceptionUtils.getRootCause(erro);
        if (causa != null) {
            Class classeCausa = causa.getClass();
            if (causa instanceof MySQLIntegrityConstraintViolationException) {
                if (causa.getMessage() != null) {
                    String mensagemBancoDeDados = causa.getMessage();
                    if (mensagemBancoDeDados.contains("Duplicate entry") && mensagemBancoDeDados.contains("UK_")) {
                        return INFORMACAO_DUPLICADA;

                    }
                }

                MySQLIntegrityConstraintViolationException erroChaveExtrangeiraNativo = (MySQLIntegrityConstraintViolationException) causa;
                System.out.println(erroChaveExtrangeiraNativo);
                return CHAVE_EXTRANGEIRA_MYSQL_NATIVO;
            }

            if (causa instanceof TransientPropertyValueException) {
                return OBJETO_FILHO_TRANSIENT;

            }

            if (classeCausa.getSimpleName().equals(NotSerializableException.class.getSimpleName())) {
                return TIPO_INVALIDO;
            }
        }

        return INDEFINIDO;

    }

    public String getMensagemUsuario(Throwable erro, ItfBeanSimples entidade) {
        switch (this) {
            case ERRO_DE_CONEXAO:
                String nomePropriedade = "indefinido";
                try {
                    TransientPropertyValueException t = (TransientPropertyValueException) UtilSBCoreErros.getCausaRaiz(erro);
                    nomePropriedade = t.getPropertyName();
                } catch (Throwable tt) {

                }
                return "Os dados referentes ao " + nomePropriedade + " são inválidos ";

            case CHAVE_EXTRANGEIRA:
                return "Um valor de campo incompatível foi configurado";

            case NAO_PODE_SER_NULO:
                PropertyValueException propNulo = (PropertyValueException) erro.getCause();

                String nomeAtributoNulo = propNulo.getPropertyName();

                String nomeEntidadeDoAtributoNulo = ClassUtils.getShortClassName(propNulo.getEntityName());
                String nomeCampoNulo = MapaObjetosProjetoAtual.getEstruturaObjeto(nomeEntidadeDoAtributoNulo).getCampoByNomeDeclarado(nomeAtributoNulo).getLabel();
                return "O campo " + nomeCampoNulo + " é obrigatório";

            case EXCEDEU_TAMANHO_MAXIMO:
                break;
            case TIPO_INVALIDO:
                if (entidade == null) {
                    return "Erro inesperado, gravando dados";
                }
                return "Erro inesperado, gravando" + UtilSBCoreReflexaoObjeto.getNomeObjeto((Class<? extends ItemGenerico>) entidade.getClass());

            case INDEFINIDO:
                break;
            case VALOR_INCOMPATIVEL:
                PropertyValueException prop = (PropertyValueException) erro.getCause();

                String nomeAt = prop.getPropertyName();

                String nomeEnt = ClassUtils.getShortClassName(prop.getEntityName());
                String nomeCampo = MapaObjetosProjetoAtual.getEstruturaObjeto(nomeEnt).getCampoByNomeDeclarado(nomeAt).getLabel();
                return "O valor para " + nomeCampo + " é incompatível";
            case CHAVE_EXTRANGEIRA_MYSQL_NATIVO:
                Throwable causa = ExceptionUtils.getRootCause(erro);
                MySQLIntegrityConstraintViolationException erroChaveExtrangeiraNativo = (MySQLIntegrityConstraintViolationException) causa;
                System.out.println(erroChaveExtrangeiraNativo);
                List<String> tabelasVinculada = UtilSBPersistenciaMysql.tabelaVinculadasAChaveExtrangeira(erroChaveExtrangeiraNativo.getMessage());
                List<String> objetosVinculados = new ArrayList<>();
                for (String tabela : tabelasVinculada) {
                    try {
                        EstruturaDeEntidade estrutura = MapaObjetosProjetoAtual.getEstruturaObjeto(tabela);
                        objetosVinculados.add(estrutura.getTags().get(0));
                    } catch (Throwable t) {
                        objetosVinculados.add(tabela);
                    }
                }
                return "Remoção negada, Existem vinculos de registro entre " + UtilSBCoreListas.getValoresSeparadosPorVirgula(objetosVinculados);
            case INFORMACAO_DUPLICADA:
                Throwable causaDuplicado = ExceptionUtils.getRootCause(erro);
                List<String> campoDuplicado = UtilSBPersistenciaMysql.colunasVinculadas_erro_chaveDB(causaDuplicado.getMessage());
                return "Já existe um " + campoDuplicado + " com este valor registrado no sistema.";
            case OBJETO_FILHO_TRANSIENT:
                break;

            default:
                throw new AssertionError(this.name());

        }
        Throwable causa = ExceptionUtils.getRootCause(erro);

        if (causa != null) {
            return ExceptionUtils.getRootCause(erro).getMessage();
        }
        return erro.getMessage();

    }

    public String getMensagemProgramador(Throwable erro, ItfBeanSimples entidade) {
        String textoMensagem = "";
        switch (this) {
            case ERRO_DE_CONEXAO:
                break;
            case CHAVE_EXTRANGEIRA:
                ConstraintViolationException erroChave = (ConstraintViolationException) erro.getCause();
                textoMensagem += "Erro chave extrangeira:" + erroChave.getConstraintName() + "" + erroChave.getSQLException().getMessage();
                break;
            case NAO_PODE_SER_NULO:
                break;
            case EXCEDEU_TAMANHO_MAXIMO:
                break;
            case TIPO_INVALIDO:
                textoMensagem += "O tipo do objeto não é reconhecido pelo hibernate "
                        + " é possível que o Objeto não possua anotações de relacionamento adequadas, como manytoOne, Manytomany"
                        + " ou que um objeto transient não tenha sido anotado com Transient ";
                List<String> nomes = UtilSBPersistenciaValidacao.getCamposNaoIdentificadosHibernate(entidade);
                if (!nomes.isEmpty()) {
                    textoMensagem += "\n é provavel que  os campos incompativeis sejam " + Arrays.toString(nomes.toArray());
                }

                break;
            case INDEFINIDO:
                break;
            case VALOR_INCOMPATIVEL:
                break;
            case CHAVE_EXTRANGEIRA_MYSQL_NATIVO:
                Throwable causa = ExceptionUtils.getRootCause(erro);
                MySQLIntegrityConstraintViolationException erroChaveExtrangeiraNativo = (MySQLIntegrityConstraintViolationException) causa;
                System.out.println(erroChaveExtrangeiraNativo);
                return erroChaveExtrangeiraNativo.getMessage();
            case INFORMACAO_DUPLICADA:
                Throwable causaDadoDuplicado = ExceptionUtils.getRootCause(erro);
                return causaDadoDuplicado.getMessage();
            case OBJETO_FILHO_TRANSIENT:
                String nomePropriedade = "indefinido";
                try {
                    TransientPropertyValueException t = (TransientPropertyValueException) UtilSBCoreErros.getCausaRaiz(erro);
                    nomePropriedade = t.getPropertyName() + " do tipo" + t.getTransientEntityName() + " no objeto " + t.getPropertyOwnerEntityName();
                } catch (Throwable tt) {

                }
                return "Erro tipo " + TransientPropertyValueException.class.getSimpleName() + ",Causa comum: o objeto filho: [" + nomePropriedade + "] não existe no banco , ou um objeto filho declarado na entidade sem as anotacoes devidas como manytoone ou transient";

            default:
                throw new AssertionError(this.name());

        }

        Throwable causa = ExceptionUtils.getRootCause(erro);

        if (causa != null) {
            textoMensagem += textoMensagem + "\n CAUSA INICIAL:" + causa.getMessage();
        }
        if (textoMensagem.isEmpty()) {
            return erro.getMessage();
        } else {
            return textoMensagem;
        }
    }

}
