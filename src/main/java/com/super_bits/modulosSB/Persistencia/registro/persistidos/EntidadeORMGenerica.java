package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.Persistencia.centralOrigemDados.CentralAtributosSBPersistencia;
import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCReflexao;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCReflexaoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.calculos.ItfCalculos;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.listas.ItfListas;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.ErroCaminhoCampoNaoExiste;

import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.UtilCRCReflexaoCaminhoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.CaminhoCampoReflexao;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.CampoEsperado;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciado.ItfCampoInstanciado;
import com.super_bits.modulosSB.SBCore.modulos.objetos.UtilCRCReflecaoIEstruturaEntidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.ComoEntidadeGenerica;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;

import com.super_bits.modulosSB.SBCore.modulos.servicosCore.ComoServicoAtributosDeObjetos;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

public abstract class EntidadeORMGenerica extends ComoEntidadeGenerica implements Serializable {

    protected Field searchCampoIdentificacao() {

        Class classeDoCampo = this.getClass();
        Field campo;
        campo = UtilCRCReflexaoCaminhoCampo.getSBCampobyTipoCampo(classeDoCampo, FabTipoAtributoObjeto.ID);

        if (campo == null) {
            campo = UtilCRCReflexaoCaminhoCampo.getFieldByClasseAnotacao(classeDoCampo, Id.class);
        }

        return campo;
    }
    @Transient
    private Map<FabTipoAtributoObjeto, String> mapaTipoCampoAutoDetect = null;

    @Override
    public ItfCampoInstanciado getCampoInstanciadoByAnotacao(FabTipoAtributoObjeto pTipocampo) {

        if (pTipocampo.equals(FabTipoAtributoObjeto.ID)) {
            ItfCampoInstanciado cp = super.getCampoInstanciadoByAnotacao(pTipocampo); //chamada super do metodo (implementação classe pai)
            if (cp != null) {
                return cp;
            }
            if (mapaTipoCampoAutoDetect != null) {
                String cpstr = mapaTipoCampoAutoDetect.get(FabTipoAtributoObjeto.ID);
                if (cpstr != null) {
                    cp = super.getCampoInstanciadoByNomeOuAnotacao(cpstr);
                    return cp;
                }
            }
            List<Class> classs = UtilCRCReflexao.getClasseESubclassesAteClasseBaseDeEntidade(this.getClass());
            for (Class classe : classs) {
                for (Field campo : classe.getDeclaredFields()) {
                    if (campo.isAnnotationPresent(Id.class)) {
                        cp = super.getCampoInstanciadoByNomeOuAnotacao(campo.getName());
                        if (mapaTipoCampoAutoDetect == null) {
                            mapaTipoCampoAutoDetect = new HashMap<>();
                        }
                        mapaTipoCampoAutoDetect.put(FabTipoAtributoObjeto.ID, campo.getName());
                        return cp;
                    }
                }
            }

        }
        return super.getCampoInstanciadoByAnotacao(pTipocampo); //chamada super do metodo (implementação classe pai)
    }

    @Override
    protected final void adcionaCampoEsperado(CampoEsperado pCampo) {
        Field campo;
        if (pCampo.equals("id")) {
            campo = searchCampoIdentificacao();
            pCampo.setAnotacaoObrigatoria(true);
        } else {
            super.adcionaCampoEsperado(pCampo);
        }
    }

    @Override
    protected ComoServicoAtributosDeObjetos getCentraldeAtributosDoObjeto(Field pCampo) {
        return new CentralAtributosSBPersistencia() {
            @Override
            public EntityManager obterEntityManagerLasyMode() {
                return UtilSBPersistencia.getEMDoContexto();

            }
        };
    }

    @Override
    protected Field getCampoByAnotacao(FabTipoAtributoObjeto pNomedaAnotacao) {

        if (pNomedaAnotacao == FabTipoAtributoObjeto.ID) {
            return searchCampoIdentificacao();
        }

        return super.getCampoByAnotacao(pNomedaAnotacao);
    }

    public void loadByID(Long pId, EntityManager pEM) {

        Object resultado = UtilSBPersistencia.getRegistroByID((Class<? extends ComoEntidadeSimples>) this.getClass(), pId, pEM);
        System.out.println("ATENÇÃO O METODO LOAD BY ID AINDA NÃO SUPORTA CLASSES COM POLIMORFISMO DE ENTIDADE");
        //todo compativel com Extenção de classe
        if (resultado != null) {
            copiaDados(resultado);
        }

    }

    protected EntidadeORMGenerica() {
        super();
    }

    protected List<Field> getCamposUmParaMuitos() {
        List<Field> resposta = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            OneToMany campoAnotado = field.getAnnotation(OneToMany.class);

            if (campoAnotado != null) {
                resposta.add(field);
            }
        }
        return resposta;
    }

    protected List<Field> getCamposMuitosParaUm() {
        List<Field> resposta = new ArrayList<>();
        Field[] fields = this.getClass().getDeclaredFields();

        for (Field field : fields) {
            ManyToOne campoAnotado = field.getAnnotation(ManyToOne.class);
            if (campoAnotado != null) {
                resposta.add(field);
            }
        }
        return resposta;
    }

    @Override
    public String getImgPequena() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected List getListaDaEtidade(boolean pAtualizarSempre) {

        String nomeCampo;
        String nomeMetodo = "Metodo não encontrado (este metodo só deve ser chamado dentro metodo get padrão pojo ex:   entidade.getValorDoCalculo();";
        try {

            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

            nomeMetodo = stackTraceElements[2].getMethodName();
            nomeCampo = nomeMetodo.substring(3);
            nomeCampo = nomeCampo.substring(0, 1).toLowerCase() + nomeCampo.substring(1);

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro localizando atributo de  lista atravez do metodo " + nomeMetodo + " na classe " + this.getClass().getSimpleName(), t);
            return null;
        }

        Field campo;

        try {

            ItfListas lista;

            campo = UtilCRCReflexaoCaminhoCampo.getFieldByCaminho(new CaminhoCampoReflexao(nomeCampo, this.getClass()));
            if (campo == null) {
                throw new UnsupportedOperationException("o campo " + nomeCampo + "não foi encontrado");
            }
            lista = UtilCRCReflecaoIEstruturaEntidade.getListaByField(campo);
            if (lista == null) {
                throw new UnsupportedOperationException("é nessessário anotar o campo do método com o enum de fabrica da lista");
            }
            List valorAnteriorLista = (List) campo.get(this);

            if (valorAnteriorLista == null || valorAnteriorLista.isEmpty() || pAtualizarSempre) {
                campo.set(this, lista.getLista(this));
                return (List) campo.get(this);
            } else {
                return (List) campo.get(this);
            }

        } catch (Throwable ex) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro configurando calculo para o campo" + nomeCampo + " na tabela " + this.getClass().getSimpleName(), ex);
            return new ArrayList();
        }

    }

    /**
     *
     * Este metodo deve ser chamado em metodos padrão POJO do tipo get, ele
     * buscará o atributo referente ao método, e atravéz
     *
     * @return
     */
    protected Object getRetornoCalculo() {
        // Obtem a anotação por reflexao do nome do metodo por atributo
        // seta o valor no atrbuto, e retorna o valor obtido
        String nomeCampo;
        String nomeMetodo = "Metodo não encontrado (este metodo só deve ser chamado dentro metodo get padrão pojo ex:   entidade.getValorDoCalculo();";
        try {

            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

            nomeMetodo = stackTraceElements[2].getMethodName();
            if (nomeMetodo.startsWith("get")) {
                nomeCampo = nomeMetodo.substring(3);
                nomeCampo = nomeCampo.substring(0, 1).toLowerCase() + nomeCampo.substring(1);
            } else if (nomeMetodo.startsWith("is")) {
                nomeCampo = nomeMetodo.substring(2);
                nomeCampo = nomeCampo.substring(0, 1).toLowerCase() + nomeCampo.substring(1);
            } else {
                //TODO implementação extra pojo...
                nomeCampo = nomeMetodo.substring(3);
                nomeCampo = nomeCampo.substring(0, 1).toLowerCase() + nomeCampo.substring(1);
            }
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro localizando atributo de  calculo atravez do metodo " + nomeMetodo + " na classe " + this.getClass().getSimpleName(), t);
            return null;
        }

        Field campo;
        try {

            ItfCalculos calculo;
            CaminhoCampoReflexao caminhoCampo = null;

            caminhoCampo = new CaminhoCampoReflexao(nomeCampo, UtilCRCReflexaoObjeto.getClassExtraindoProxy(this.getClass().getSimpleName()));
            campo = UtilCRCReflexaoCaminhoCampo.getFieldByCaminho(caminhoCampo);

            calculo = UtilCRCReflecaoIEstruturaEntidade.getCalculoByField(campo);
            try {

                campo.set(this, calculo.getValor(this));
            } catch (IllegalAccessException | IllegalArgumentException t) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "O calor do campo  " + campo.getName() + " não pode ser configurado por reflexão, o valor enviado foi" + calculo + " o erro que aconteceu foi: " + t.getMessage(), t);

            }

            return campo.get(this);
        } catch (SecurityException | IllegalArgumentException | ErroCaminhoCampoNaoExiste | IllegalAccessException ex) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro configurando calculo para o campo" + nomeCampo + " na tabela " + this.getClass().getSimpleName(), ex);
            return null;
        }

    }

    private class CampoEntidadeGenericaInstanciada extends CampoIntemGenericoInstanciado implements ItfCampoInstanciado {

        public CampoEntidadeGenericaInstanciada(Field pCampoReflection) {
            super(pCampoReflection);
        }

        @Override
        public List<ComoEntidadeSimples> getListaDeOpcoes() {
            return super.getListaDeOpcoes(); //To change body of generated methods, choose Tools | Templates.
        }

    }

    @Override
    protected ItfCampoInstanciado instanciarnovoCampo(Field pCampoReflexao) {
        return new CampoEntidadeGenericaInstanciada(pCampoReflexao);
    }

}
