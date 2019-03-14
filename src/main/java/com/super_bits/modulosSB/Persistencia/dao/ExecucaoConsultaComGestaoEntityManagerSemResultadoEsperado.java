/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.ErroRegraDeNegocio;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author novy
 */
public abstract class ExecucaoConsultaComGestaoEntityManagerSemResultadoEsperado extends GestaoEntityManagerAbstrata {

    public ExecucaoConsultaComGestaoEntityManagerSemResultadoEsperado() {
        // TODO desacoplar execução de consulta com JPA e execução de regra de negocio, uma  gambiarra detectada..
    }

    @Override
    public void executarAcoesIniciais() throws ErroEmBancoDeDados {
        getEm();
    }

    public EntityManager getEmConsultaComGEstao() {
        return getEm();
    }

    @Override
    public final void executarAcao() throws ErroRegraDeNegocio {
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
}
