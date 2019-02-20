/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.Mensagens.FabMensagens;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.ErroRegraDeNegocio;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * Classe para gestão de entity manager, com utilitários para gestão de entidade
 * e os seguintes métodos abstratos:
 *
 * Extenda esta classe e seja feliz, criando sua própria lógica de execução na
 * camada controller da sua aplicaçao
 *
 *
 * @see ItfExecucaoRegraDeNegocio#executarAcoesFinais()
 * @see ItfExecucaoRegraDeNegocio#executarAcoesIniciais()
 * @see ItfExecucaoRegraDeNegocio#executarAcao()
 *
 * @author SalvioF
 */
public abstract class GestaoEntityManagerAbstrata implements ItfExecucaoRegraDeNegocio, ItfExecucaoRegraDeNegocioComGestaodeEntityManager {

    private EntityManager em;
    private boolean emFoiCricado = false;

    public GestaoEntityManagerAbstrata() {
    }

    public GestaoEntityManagerAbstrata(EntityManager em) {
        this.em = em;
    }

    @Override
    public EntityManager getEm() {
        if (em != null) {
            return em;
        }
        if (em == null && !emFoiCricado) {
            em = UtilSBPersistencia.getNovoEM();
            emFoiCricado = true;
        }
        if (em == null && emFoiCricado) {
            throw new UnsupportedOperationException("O EntityManager já foi Encerrado");
        }
        return em;

    }

    public final void execucaoPadrao() {
        try {
            try {
                executarAcoesIniciais();
                regraDeNegocio();
                executarAcoesFinais();

            } catch (ErroRegraDeNegocio pErro) {
                SBCore.enviarMensagemUsuario(pErro.getMessage(), FabMensagens.ERRO);
            } catch (ErroEmBancoDeDados pErro) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, pErro.getMensagemProgrador(), pErro);
                SBCore.enviarMensagemUsuario(pErro.getMensagemUsuario(), FabMensagens.ERRO);
            } catch (Throwable t) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Inexperado executando ação com gestao automatica de entity manager", t);
            }
        } finally {
            fecharEntityManagerEmSeguranca();
        }

    }

    @Override
    public void fecharEntityManagerEmSeguranca() {
        if (em != null) {
            if (getEm().getTransaction().isActive()) {
                try {
                    getEm().getTransaction().rollback();
                } catch (Throwable t) {
                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro executando roolback, em transação ativa em ", t);
                    try {
                        getEm().close();
                    } catch (Throwable tt) {
                        SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro tentando fechar entityManger apos erro em tentativa de roolback", tt);
                    }

                }
            }
            if (getEm().isOpen()) {
                try {
                    getEm().close();
                } catch (Throwable t) {
                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro finalizando entitymanager não finalizado em ", t);
                }

            }
            em = null;

        }
    }

}
