/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.Controller.ControllerAppAbstratoSBCore;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfResposta;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfRespostaAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.UtilSBController;
import com.super_bits.modulosSB.SBCore.modulos.Controller.comunicacao.RespostaAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.ErroRegraDeNegocio;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.UtilSBCoreReflexaoCaminhoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.CaminhoCampoReflexao;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanNormal;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 *
 * Classe para camada controller que utliza controle de usuário através de
 * persistencia com Hibernate
 *
 *
 * @author Salvio Fubino
 */
public abstract class ControllerAbstratoSBPersistencia extends ControllerAppAbstratoSBCore {

    public static void lancarErroAPartirDaResposta(ItfRespostaAcaoDoSistema pREsposta) throws ErroRegraDeNegocio {
        if (!pREsposta.isSucesso()) {
            throw new ErroRegraDeNegocio(pREsposta.getMensagens().get(0).getMenssagem());
        }
    }

    /**
     *
     * Retorna a resposta verificando as permissões e adicionando mensagens de
     * erro para parametros não enviados (AINDA NÂO IMPLEMENTADO)
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *
     * @param pEntidade
     * @return
     */
    protected static ItfRespostaAcaoDoSistema getNovaRespostaAutorizaChecaNulo(ItfBeanSimples pEntidade) {
        try {
            if (pEntidade == null) {
                throw new UnsupportedOperationException("Entidade não enviada para execução de atividade");
            }
            RespostaAcaoDoSistema resp = new RespostaAcaoDoSistema(pEntidade, UtilSBController.getAcaoByMetodo(getMetodoChamado(), true));

            resp.setTipoRetorno(pEntidade.getClass());
            addMensagemDeAutorizacao(resp);

            return resp;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro criando objeto de resposta padrão", t);
            return new RespostaAcaoDoSistema(UtilSBController.getAcaoByMetodo(getMetodoChamado(), true)).addErro("Erro inesperado executando "
                    + UtilSBController.getAcaoByMetodo(getMetodoChamado(), true).getNomeAcao());
        }
    }

    protected static void persistirTodasEntidadesVinculadas(ItfResposta pResp, ItfBeanSimples pEntidade, EntityManager pEM) {
        persistirEntidadeComEntidadesVinculadas(pResp, pEntidade, pEM, 3);
    }

    protected static void persistirEntidadeComEntidadesVinculadas(ItfResposta pResp, ItfBeanSimples pEntidade, EntityManager pEM, int quantidadeDemanda) {

        try {

            try {
                if (!UtilSBPersistencia.iniciarTransacao(pEM)) {
                    throw new UnsupportedOperationException("Impossível iniciar tranzação");
                }
            } catch (Throwable t) {

                pResp.addErro("Desculpe, a tentativa de conexão com o banco não foi bem sucedida");
                return;
            }
            String nomeRegistroEntidadePrincipal = pEntidade.getNomeDoObjeto();
            String nomeOperacao = getAcaoDoMetodo().getNomeAcao();

            List<CaminhoCampoReflexao> lista = new ArrayList<>();
            if (!UtilSBCoreReflexaoCaminhoCampo.buildListaSubEntidadesPersistiveis(pEntidade, 0, quantidadeDemanda, lista, null)) {
                throw new UnsupportedOperationException("Erro Otendo Lista de entidades vinculadas executando" + nomeOperacao + " para o registro" + nomeRegistroEntidadePrincipal);
            }
            for (CaminhoCampoReflexao campo : lista) {
                Object novaEntidade = null;
                try {
                    novaEntidade = pEntidade.getValorCampoByCaminhoCampo(campo);
                    ItfBeanSimples entidade = null;
                    if (novaEntidade != null) {
                        try {
                            entidade = (ItfBeanSimples) novaEntidade;
                        } catch (Throwable tt) {
                            //System.out.println("Campo não é do tipo bean Simples TODO solução melhor para isso");
                        }

                        if (entidade != null) {
                            UtilSBPersistencia.mergeRegistro(novaEntidade, pEM);
                        }
                    }
                } catch (Throwable t) {

                    String nomeEntidadeErro = "Registro indefinido";
                    try {
                        nomeEntidadeErro = ((ItfBeanSimples) novaEntidade).getNomeDoObjeto();
                    } catch (Throwable tn) {
                        SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro obtendo nome da entidade", tn);
                    }

                    pResp.addErro("Ouve um erro ao salvar um registro do tipo" + nomeEntidadeErro + " vinculado ao " + pEntidade.getNomeDoObjeto());

                }

            }

            if (UtilSBPersistencia.finalizarTransacao(pEM)) {
                pResp.addAviso("As informações sobre o registro " + nomeRegistroEntidadePrincipal + " foram armazenadas com sucesso");

            } else {
                pResp.addErro("A operação não foi realizada, Ouve um erro de consistencia nas informações do " + pEntidade.getNomeDoObjeto());
            }

        } catch (Throwable t) {

            pResp.addErro("Ocorreu durante tentativa de " + getAcaoDoMetodo().getNomeAcao() + ", os Desenvolvedores já foram informados. Pode ser que tentando fazer isso de outra forma o problema seja resolvido, mas não podemos garantir..");
            if (pEM.getTransaction().isActive()) {
                pEM.getTransaction().rollback();
            }

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Ocorreu um erro ao Peristir todas entidades vinculadas", t);
        }

    }

    protected static void alterarStatus(ItfResposta pResposta, ItfBeanNormal pEntidade, EntityManager pem) {
        try {
            UtilSBPersistencia.iniciarTransacao(pem);
            String nomeAcao = "Ativar";
            if (pEntidade.isAtivo()) {
                pEntidade.setAtivo(false);
                nomeAcao = "Desativar";
            } else {
                pEntidade.setAtivo(true);
                nomeAcao = "Ativar";
            }

            if (UtilSBPersistencia.mergeRegistro(pEntidade) != null) {
                pResposta.addErro("Não foi possível " + nomeAcao + " o registro" + pEntidade.getClass().getSimpleName());
            }

            if (!UtilSBPersistencia.finalizarTransacao(pem)) {
                pResposta.addErro("Não foi possível " + nomeAcao + " o registro" + pEntidade.getClass().getSimpleName());
            }
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Ocorreu um erro inesperado ao tentar alterar O Status", t);
        }
    }

}
