/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.ErroRegraDeNegocio;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimplesSomenteLeitura;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 */
public abstract class ExecucaoComGestaoEntityManager extends GestaoEntityManagerAbstrata {

    private final EnumTipoExecucao pTipoExecucao = EnumTipoExecucao.INICIAR_E_FINALIZAR_TRANSACAO;
    private boolean executarAoConstruir = true;
    private boolean executouAcoesFinais = false;

    public void execucaoPadraoComLancaMentoDeErro() throws ErroEmBancoDeDados, ErroRegraDeNegocio {
        try {
            executarAcoesIniciais();

            regraDeNegocio();

            executarAcoesFinais();

        } finally {
            fecharEntityManagerEmSeguranca();
        }
    }

    @Override
    public void executarAcao() throws ErroEmBancoDeDados, ErroRegraDeNegocio {

        execucaoPadraoComLancaMentoDeErro();

    }

    public ExecucaoComGestaoEntityManager() {

        this(true);

    }

    public ExecucaoComGestaoEntityManager(boolean pExecutarAoConstruir) {

        executarAoConstruir = pExecutarAoConstruir;

        if (executarAoConstruir) {

            execucaoPadrao();

        }
    }

    @Override
    public void executarAcoesIniciais() throws ErroEmBancoDeDados {

        if (getEm() == null) {
            throw new ErroEmBancoDeDados(FabTipoErroBancoDeDados.ERRO_DE_CONEXAO, "Erro obtendo Entity Manager");

        }
        if (getEm().getTransaction().isActive()) {
            System.out.println("[ATENÇÃO A TRANSAÇÃO DO ENTITYMANAGER JÁ ESTÁVA ABERTA]");
        } else // Iniciando Transação
        {
            if (!UtilSBPersistencia.iniciarTransacao(getEm())) {
                throw new ErroEmBancoDeDados(FabTipoErroBancoDeDados.ERRO_DE_CONEXAO, "Erro iniciando transacao");
            }
        }

    }

    /**
     *
     * @throws ErroEmBancoDeDados
     */
    @Override
    public final void executarAcoesFinais() throws ErroEmBancoDeDados {
        if (!executouAcoesFinais) {
            executouAcoesFinais = true;
            try {
                if (getEm().getTransaction().isActive()) {
                    getEm().getTransaction().commit();
                }

            } catch (Throwable t) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro finalizando tranzação ", t);
                fecharEntityManagerEmSeguranca();
                throw new ErroEmBancoDeDados(t, null);

            } finally {

                fecharEntityManagerEmSeguranca();
            }
        }

    }

    @Override
    public void reverterAcoesFinais() throws ErroEmBancoDeDados {
        UtilSBPersistencia.finzalizaTransacaoEFechaEMRevertendoAlteracoes(getEm());
        fecharEntityManagerEmSeguranca();
    }

    public ExecucaoComGestaoEntityManager(EntityManager em) {
        super(em);
    }

    public Object atualizarEntidade(Object pObjeto) throws ErroEmBancoDeDados {

        return merge(pObjeto);
    }

    protected void iniciarEntityManagerETransacao() {

    }

    protected void iniciarEntityManager() {

    }

    public void criaNovaEntidade(Object pObjeto) {
        if (UtilSBPersistencia.persistirRegistro(pObjeto, getEm())) {
            throw new UnsupportedOperationException("Erro Criando" + pObjeto);
        }
    }

    public void excluirEntidade(Object pObjeto) {

        if (!UtilSBPersistencia.exluirRegistro(pObjeto, getEm())) {
            throw new UnsupportedOperationException("Erro excluindo Entidade" + pObjeto);
        }

    }

    public Object merge(Object pObjeto) throws ErroEmBancoDeDados {

        try {
            Object retornoMerge = getEm().merge(pObjeto);

            return retornoMerge;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Executando merge em " + pObjeto, t);
            if (pObjeto != null) {
                throw new ErroEmBancoDeDados(t, (ItfBeanSimples) pObjeto);
            } else {
                throw new ErroEmBancoDeDados(t, null);
            }
        }

    }

    public boolean remover(Object pObjeto) throws ErroEmBancoDeDados {

        try {
            pObjeto = UtilSBPersistencia.loadEntidade((ItfBeanSimplesSomenteLeitura) pObjeto, getEm());
            if (pObjeto != null) {
                getEm().remove(pObjeto);
            }
            return true;

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Executando merge em " + pObjeto, t);
            if (pObjeto != null) {
                throw new ErroEmBancoDeDados(t, (ItfBeanSimples) pObjeto);
            } else {
                throw new ErroEmBancoDeDados(t, null);
            }

        }

    }

}
