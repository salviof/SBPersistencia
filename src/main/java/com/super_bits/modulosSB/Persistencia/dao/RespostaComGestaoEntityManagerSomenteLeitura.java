/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfRespostaAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfRespostaComGestaoDeEntityManager;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.ErroRegraDeNegocio;
import javax.persistence.EntityManager;

/**
 *
 * @author novy
 */
public abstract class RespostaComGestaoEntityManagerSomenteLeitura extends RespostaComGestaoEntityManager implements ItfRespostaComGestaoDeEntityManager {

    public RespostaComGestaoEntityManagerSomenteLeitura(ItfRespostaAcaoDoSistema pResp) {
        this(pResp, true);

    }

    public RespostaComGestaoEntityManagerSomenteLeitura(ItfRespostaAcaoDoSistema pResp, boolean executarAcaoAoCriar) {

        super(pResp, new ExecucaoConsultaComGestaoEntityManagerSemResultadoEsperado() {
            @Override
            public void regraDeNegocio() {

            }
        }, false);
        if (executarAcaoAoCriar) {
            executarAcao();
        }

    }

    @Override
    public abstract void regraDeNegocio() throws ErroRegraDeNegocio;

    @Override
    public EntityManager getEMResposta() {
        return getExecucaoGestaoEM().getEm();
    }

}
