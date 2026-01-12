/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.entidadeEscuta.ComoListenerGestaoDeEntidade;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.ErroRegraDeNegocio;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaDeEntidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoEntidadeSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoEntidadeSimplesSomenteLeitura;
import java.util.List;
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

    public Object atualizarEntidade(ComoEntidadeSimples pObjeto) throws ErroEmBancoDeDados {
        if (pObjeto.getId() == null) {
            //throw new ErroEmBancoDeDados(FabTipoErroBancoDeDados.VALOR_INCOMPATIVEL, "Impossível atualizar registro com o id nulo! utilize Merg, ou Persistir");
        }
        return merge((ComoEntidadeSimples) pObjeto);
    }

    public Object criar(ComoEntidadeSimples pObjeto) throws ErroEmBancoDeDados {
        if (pObjeto.getId() != null) {
            throw new ErroEmBancoDeDados(FabTipoErroBancoDeDados.VALOR_INCOMPATIVEL, "Impossível criar Registro com id definido");
        }
        return merge((ComoEntidadeSimples) pObjeto);
    }

    protected void iniciarEntityManagerETransacao() {

    }

    protected void iniciarEntityManager() {

    }

    public void criaNovaEntidade(Object pObjeto) {
        if (UtilSBPersistencia.persistirRegistro(pObjeto, getEm()) != null) {
            throw new UnsupportedOperationException("Erro Criando" + pObjeto);
        }
    }

    private List<ComoListenerGestaoDeEntidade> getListener(ComoEntidadeSimples pEntidade) {
        Class classeEntidade = MapaObjetosProjetoAtual.getClasseDoObjetoByNome(pEntidade.getClass().getSimpleName());
        EstruturaDeEntidade estrutura = MapaObjetosProjetoAtual.getEstruturaObjeto(classeEntidade);
        return estrutura.getListenerPersistencia();

    }

    public boolean excluirEntidade(ComoEntidadeSimples pEntidade) throws ErroEmBancoDeDados {

        return remover(pEntidade);

    }

    public Object merge(ComoEntidadeSimples pEntidade) throws ErroEmBancoDeDados {
        boolean novoRegistro = pEntidade.getId() == null;
        List<ComoListenerGestaoDeEntidade> listeners = getListener(pEntidade);
        for (ComoListenerGestaoDeEntidade escuta : listeners) {
            if (novoRegistro) {
                escuta.acaoAntesDePersistir(pEntidade, getEm());
            } else {
                escuta.acaoAntesDeAtualizar(pEntidade, getEm());
            }
        }
        try {
            Object retornoMerge = getEm().merge(pEntidade);
            for (ComoListenerGestaoDeEntidade escuta : listeners) {
                if (novoRegistro) {
                    escuta.acaoAposPersistir(pEntidade, getEm());
                } else {
                    escuta.acaoAposAtualizar(pEntidade, getEm());
                }
            }
            return retornoMerge;

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Executando merge em " + pEntidade, t);
            if (pEntidade != null) {
                throw new ErroEmBancoDeDados(t, (ComoEntidadeSimples) pEntidade);
            } else {
                throw new ErroEmBancoDeDados(t, null);
            }
        }

    }

    public boolean remover(ComoEntidadeSimples pEntidade) throws ErroEmBancoDeDados {
        List<ComoListenerGestaoDeEntidade> listeners = getListener(pEntidade);
        try {
            for (ComoListenerGestaoDeEntidade escuta : listeners) {
                escuta.acaoAntesRemover(pEntidade, getEm());
            }
            pEntidade = UtilSBPersistencia.loadEntidade((ComoEntidadeSimplesSomenteLeitura) pEntidade, getEm());
            if (pEntidade != null) {
                getEm().remove(pEntidade);
            }
            for (ComoListenerGestaoDeEntidade escuta : listeners) {
                escuta.acaoAposRemover(pEntidade, getEm());
            }
            return true;

        } catch (Throwable t) {

            if (pEntidade != null) {
                throw new ErroEmBancoDeDados(t, pEntidade);
            } else {
                throw new ErroEmBancoDeDados(t, null);
            }

        }

    }

}
