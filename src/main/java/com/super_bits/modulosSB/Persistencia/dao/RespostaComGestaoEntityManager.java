/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfRespostaAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfRespostaComGestaoDeEntityManager;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.ErroRegraDeNegocio;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanEnderecavel;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import static java.lang.Thread.sleep;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 */
public abstract class RespostaComGestaoEntityManager extends RespostaComRegraDeNegocio implements ItfRespostaComGestaoDeEntityManager {

    public RespostaComGestaoEntityManager(ItfRespostaAcaoDoSistema pResp, ItfExecucaoRegraDeNegocio pMetodologiaExecucaoAlternativa, boolean executarAcaoAoCriar) {

        super(pResp, pMetodologiaExecucaoAlternativa);
        if (executarAcaoAoCriar) {
            if (isSucesso()) {
                executarAcao();
            }
        }

    }

    public void lancarErroAPartirDaResposta(ItfRespostaAcaoDoSistema pREsposta) throws ErroRegraDeNegocio {
        if (!pREsposta.isSucesso()) {
            throw new ErroRegraDeNegocio(pREsposta.getMensagens().get(0).getMenssagem());
        }
    }

    public RespostaComGestaoEntityManager(ItfRespostaAcaoDoSistema pResp, boolean executarAcaoAoCriar) {

        super(pResp, new ExecucaoComGestaoEntityManager(false) {
            @Override
            public void regraDeNegocio() {

            }

            @Override
            public void executarAcao() {

            }
        });
        if (executarAcaoAoCriar) {
            if (isSucesso()) {
                executarAcao();
            }
        }

    }

    public RespostaComGestaoEntityManager(ItfRespostaAcaoDoSistema pResp) {
        this(pResp, true);
    }

    public <T> T atualizarEntidade(T pObjeto) {
        try {

            return (T) getExecucaoGestaoEMCompleta().atualizarEntidade(pObjeto);
        } catch (ErroEmBancoDeDados ex) {
            addErro(ex.getMensagemUsuario());
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, ex.getMensagemProgrador(), ex);
        }
        return null;
    }

    public Object atualizarEntidadeConfigRetorno(Object pObjeto) {
        try {

            Object registroAtualizado = getExecucaoGestaoEMCompleta().atualizarEntidade(pObjeto);
            if (registroAtualizado != null) {
                setRetorno(registroAtualizado);
            }
            return registroAtualizado;
        } catch (ErroEmBancoDeDados ex) {
            addErro(ex.getMensagemUsuario());
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, ex.getMensagemProgrador(), ex);
        }
        return null;
    }

    /**
     *
     * @param <T>
     * @param pEntidade
     * @return
     */
    public <T extends ItfBeanSimples> T loadEntidade(T pEntidade) {
        T regAtualizado = UtilSBPersistencia.loadEntidade(pEntidade, getEmResposta());
        if (regAtualizado == null) {
            addErro("Falha: " + pEntidade + " não encontrada");
            return null;
        } else {
            return regAtualizado;
        }
    }

    public boolean removerEntidade(final ItfBeanSimples pObjeto) {

        try {
            if (isSucesso()) {
                return getExecucaoGestaoEMCompleta().remover(pObjeto);
            }
        } catch (ErroEmBancoDeDados ex) {
            addErro(ex.getMensagemUsuario());
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, ex.getMensagemProgrador(), ex);
        }
        return false;

    }

    protected class ManterConexao extends Thread {

        boolean fim = false;
        boolean liberado = false;
        private final ItfBeanSimples entidade;
        private final EntityManager em;

        public ManterConexao(ItfBeanSimples pEntidade, EntityManager pEm) {
            entidade = pEntidade;
            em = pEm;

        }

        @Override
        public final void run() {
            while (!isFim()) {
                try {
                    sleep(4000);
                    em.refresh(entidade);
                } catch (Throwable t) {

                }
            }
            liberado = true;
        }

        private synchronized boolean isFim() {
            return fim;
        }

        private synchronized boolean isLiberado() {
            return liberado;
        }

        public void parar() {
            fim = true;
            while (!liberado) {
                try {
                    sleep(1000);
                    em.refresh(entidade);
                } catch (Throwable t) {

                }

            }
        }

    }

    protected boolean carregarEndereco(ItfBeanEnderecavel pEntidadeEndereçavel) {

        if (pEntidadeEndereçavel == null) {

            addErro("Não é possivel cadastrar um endereço nulo!");
            return false;

        }

        if (pEntidadeEndereçavel.getLocalizacao().getBairro() == null
                || pEntidadeEndereçavel.getLocalizacao().getBairro().getId() != 0) {

        } else {

            pEntidadeEndereçavel.getLocalizacao().setBairro(null);

            if (pEntidadeEndereçavel.getLocalizacao().getNome() == null) {

                pEntidadeEndereçavel.setLocalizacao(null);
            }

        }

        return true;

    }

    public boolean salvarEnderecoModoFlexivel(ItfBeanEnderecavel p) {
        if (SBCore.getCentralDeLocalizacao().salvarFlexivel(p)) {
            return true;
        } else {
            addErro("Erro salvando dados do endereço");
            return false;
        }

    }

    @Override
    public final void executarAcao() {
        try {
            try {
                if (isSucesso()) {
                    executarAcoesIniciais();
                    try {
                        regraDeNegocio();
                    } catch (ErroRegraDeNegocio pRegraDeNegocio) {
                        addErro(pRegraDeNegocio.getMessage());
                    }

                    executarAcoesFinais();
                }

            } catch (ErroEmBancoDeDados pErroBanco) {
                addErro(pErroBanco.getMensagemUsuario());
            } catch (Throwable t) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Ero inesperado executando " + getAcaoVinculada(), t);
                addErro("Ero inesperado executando " + getResposta().getAcaoVinculada());
                if (getExecucaoGestaoEM().getEm() != null) {
                    if (getExecucaoGestaoEM().getEm().isOpen()) {
                        getExecucaoGestaoEM().getEm().close();
                    }
                }
                if (getResposta() != null) {
                    getResposta().addMensagemErroDisparaERetorna("Ocorreu um erro inesperado  executando" + getAcaoVinculada().getNomeAcao());
                }
            }
        } finally {
            getExecucaoGestaoEM().fecharEntityManagerEmSeguranca();
        }

    }

    @Deprecated
    public EntityManager getEmResposta() {
        return getEMResposta();
    }

    @Override
    public EntityManager getEMResposta() {
        return getExecucaoGestaoEM().getEm();
    }

    public ExecucaoComGestaoEntityManager getExecucaoGestaoEMCompleta() {
        return (ExecucaoComGestaoEntityManager) getExecucaoGestaoEM();
    }

    public ItfExecucaoRegraDeNegocioComGestaodeEntityManager getExecucaoGestaoEM() {
        return (ItfExecucaoRegraDeNegocioComGestaodeEntityManager) getExecucao();
    }

    public RespostaComGestaoEntityManager getRespostaComGestao() {
        return this;
    }

    @Override
    public ItfRespostaComGestaoDeEntityManager getComoRepostaGestaoEM() {
        return this;
    }

}
