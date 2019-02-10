/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author SalvioF
 *
 */
public abstract class ExecucaoConsultaComGestaoEntityManager extends GestaoEntityManagerAbstrata {

    private Object resultado;

    public ExecucaoConsultaComGestaoEntityManager() {
        executarAcao();
    }

    @Override
    public void executarAcoesIniciais() throws ErroEmBancoDeDados {
        getEm();
    }

    public EntityManager getEmConsultaComGEstao() {
        return getEm();
    }

    @Override
    public void regraDeNegocio() {
        resultado = regraDeNegocioRetornandoResultado();
    }

    public abstract Object regraDeNegocioRetornandoResultado();

    @Override
    public void executarAcao() {
        try {
            executarAcoesIniciais();
            regraDeNegocio();
            executarAcoesFinais();
        } catch (ErroEmBancoDeDados t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro ", t);
        } finally {
            fecharEntityManagerEmSeguranca();
        }
    }

    @Override
    public void executarAcoesFinais() throws ErroEmBancoDeDados {
        fecharEntityManagerEmSeguranca();
    }

    @Override
    public void reverterAcoesFinais() throws ErroEmBancoDeDados {
        fecharEntityManagerEmSeguranca();
    }

    public Object getResultado() {
        return resultado;
    }

}
