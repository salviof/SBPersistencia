/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfRespostaAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfRespostaComGestaoDeEntityManager;

/**
 *
 * @author desenvolvedor
 */
public interface ItfRespostaComExecucaoDeRegraDeNegocio extends ItfExecucaoRegraDeNegocio, ItfRespostaAcaoDoSistema, ItfRespostaComGestaoDeEntityManager {

    public ItfExecucaoRegraDeNegocio getExecucao();

    public ItfRespostaAcaoDoSistema getResposta();

}
