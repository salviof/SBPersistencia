/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.centralOrigemDados;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.fonteDados.CentralAtributosDeObjetosSemPersistencia;
import com.super_bits.modulosSB.SBCore.modulos.fonteDados.ItfCentralAtributosDeObjetos;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.ItfPropriedadesReflexaoCampos;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoLisgagemOpcoesCampo;
import static com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoLisgagemOpcoesCampo.LISTAR_POR_SUBLISTA;
import static com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoLisgagemOpcoesCampo.LISTA_POR_ENTIDADE;
import static com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoLisgagemOpcoesCampo.LISTA_POR_FABRICA_DE_REGISTROS;
import static com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoLisgagemOpcoesCampo.LISTA_POR_LISTAGEM_DE_ENTIDADE;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciado.ItfCampoInstanciado;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 */
public class CentralAtributosSBPersistencia extends CentralAtributosDeObjetosSemPersistencia implements ItfCentralAtributosDeObjetos {

    private EntityManager em;

    public CentralAtributosSBPersistencia() {
    }

    public CentralAtributosSBPersistencia(EntityManager pEM) {
        em = pEM;
    }

    @Override
    public List getListaOpcoesCampo(ItfPropriedadesReflexaoCampos pPropriedades) {

        FabTipoLisgagemOpcoesCampo tipoLista = pPropriedades.getTipoListagem();
        if (tipoLista == null) {
            return new ArrayList();
        }

        switch (tipoLista) {
            case LISTA_POR_FABRICA_DE_REGISTROS:
                return super.getListaOpcoesCampo(pPropriedades);
            case LISTAR_POR_SUBLISTA:
                throw new UnsupportedOperationException("Para listar a partir de sublista, é nescessário utilizar um campo instanciado");

            case LISTA_POR_ENTIDADE:
                return UtilSBPersistencia.getListaTodos(pPropriedades.getClasseDeclaracaoAtributo(), em);

            case LISTA_POR_LISTAGEM_DE_ENTIDADE:
                return UtilSBPersistencia.getListaTodos(pPropriedades.getEntidadeLista(), em);
            default:
                throw new AssertionError(tipoLista.name());

        }

    }

    @Override
    public List getListaOpcoesCampoInstanciado(ItfCampoInstanciado pCampoInstanciado) {
        try {

            FabTipoLisgagemOpcoesCampo tipoLista = pCampoInstanciado.getPropriedadesRefexao().getTipoListagem();
            switch (tipoLista) {
                case LISTAR_POR_SUBLISTA:
                    String caminho = pCampoInstanciado.getPropriedadesRefexao().getCaminhoListagemOpcoes();
                    ItfCampoInstanciado campoInstanciado = pCampoInstanciado.getObjetoDoAtributo().getCampoInstanciadoByNomeOuAnotacao(caminho);
                    if (campoInstanciado.isCampoNaoInstanciado()) {
                        return new ArrayList();
                    } else {
                        return (List) campoInstanciado.getValor();
                    }
                case LISTA_POR_LISTAGEM_DE_ENTIDADE:

                case LISTA_POR_FABRICA_DE_REGISTROS:

                case LISTA_POR_ENTIDADE:
                    return getListaOpcoesCampo(pCampoInstanciado.getPropriedadesRefexao());

                default:
                    return getListaOpcoesCampo(pCampoInstanciado.getPropriedadesRefexao());

            }

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "erro Obtendo lista de opções do campo instanciado", t);
            return new ArrayList();
        }

    }

    @Override
    public List getListaOpcoesCampoInstanciado(ItfCampoInstanciado pCampoInstanciado, String pFiltro, String... pCampos) {
        ItfPropriedadesReflexaoCampos propAtributoReflexao = pCampoInstanciado.getPropriedadesRefexao();
        FabTipoLisgagemOpcoesCampo tipoLista = propAtributoReflexao.getTipoListagem();
        switch (tipoLista) {

            case LISTA_POR_ENTIDADE:
                try {
                    for (String campo : pCampos) {

                    }
                } catch (Throwable t) {
                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro ao pesquisar por campos", t);
                }
                return UtilSBPersistencia.getListaRegistrosLikeNomeCurto(pFiltro, propAtributoReflexao.getEntidadeLista(), em);
            case LISTA_POR_LISTAGEM_DE_ENTIDADE:
                throw new UnsupportedOperationException("");

            default:
                return super.getListaOpcoesCampoInstanciado(pCampoInstanciado, pFiltro, pCampos); //To change body of generated methods, choose Tools | Templates.        ;

        }

    }

    /**
     * @Override public void configurarCampo(FieldComSerializacao
     * pCAmpoReflexao, TipoAtributoObjetoSB sbCampo) { try {
     * super.configurarCampo(pCAmpoReflexao, sbCampo); //To change body of
     * generated methods, choose Tools | Templates. InfoCampo anotacaoInfoCampo
     * = pCAmpoReflexao.getInfoCampo(); FabTipoAtributoObjeto tipoDoCampo;
     *
     * TIPO_DECLARACAO tipoDeclaracaoPersistencia =
     * getTipoDeclaracao(pCAmpoReflexao);
     *
     * if (tipoDeclaracaoPersistencia != null) {
     *
     * sbCampo.setTipoDeclaracao(tipoDeclaracaoPersistencia); if
     * (!sbCampo.getFabricaTipoAtributo().isUmCampoDeObjetoConhecido()) { switch
     * (tipoDeclaracaoPersistencia) { case MUITOS_PARA_MUITOS:
     * sbCampo.setFabricaTipoCampo(FabTipoAtributoObjeto.LISTA_OBJETOS); break;
     * case MUITOS_PARA_MUITOS_COM_REPETICAO:
     * sbCampo.setFabricaTipoCampo(LISTA_OBJETOS); break; case MUITOS_PARA_UM:
     * sbCampo.setFabricaTipoCampo(OBJETO_DE_UMA_LISTA); break;
     *
     * default: throw new AssertionError(tipoDeclaracaoPersistencia.name());
     *
     * }
     * }
     * }
     *
     * // CONFIGURANDO OPÇÕES DE SELEÇÃO switch (sbCampo.getTipoDeclaracao()) {
     * case SIMPLES: break; case VALOR_CALCULADO: break; case LISTA_DINIMICA:
     * break; case MUITOS_PARA_MUITOS: break; case
     * MUITOS_PARA_MUITOS_COM_REPETICAO: break; case MUITOS_PARA_UM: break; case
     * OBJETO_EMBUTIDO: break; case CAMPO_TRANSIENTE: break; case
     * OBJETO_TRANSIENTE: break; default: throw new
     * AssertionError(sbCampo.getTipoDeclaracao().name());
     *
     * }
     *
     * if (anotacaoInfoCampo != null) { if (anotacaoInfoCampo.Mask().length() >
     * 0) { sbCampo.setMascara(anotacaoInfoCampo.Mask()); } if
     * (anotacaoInfoCampo.label().length() > 0) {
     * sbCampo.setLabel(anotacaoInfoCampo.label()); }
     * sbCampo.setObrigatorio(anotacaoInfoCampo.obrigatorio()); if
     * (anotacaoInfoCampo.valoresAceitos().length > 0) {
     * anotacaoInfoCampo.valoresAceitos(); } }
     *
     * Annotation[] outrasAnotacoes = pCAmpoReflexao.getAnotacoes();
     *
     * NotNull nulo = pCAmpoReflexao.getAnotacaoNotNull(); if (nulo != null) {
     * sbCampo.setObrigatorio(true); }
     *
     * for (Annotation anotacao : outrasAnotacoes) { // Verificar outras
     * anotacoes } } catch (Throwable t) {
     * SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro configurando
     * propriedades do campo " + sbCampo + "-" + pCAmpoReflexao, t);
     *
     * }
     *
     * }
     */
}
