/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ItfAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ItfFabricaStatus;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.CampoEsperado;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanStatus;
import java.util.List;

/**
 *
 * @author desenvolvedor
 */
public class EntidadeStatus extends EntidadeSimples implements ItfBeanStatus {

    public EntidadeStatus() {
        super();
        adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.STATUS_ENUM), true);
    }

    @Override
    public ItfFabricaStatus getStatusEnum() {
        return (ItfFabricaStatus) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.STATUS_ENUM);
    }

    @Override
    public void setStatusEnum(ItfFabricaStatus pFab) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.STATUS_ENUM, pFab);
    }

    public List<ItfAcaoDoSistema> getAcoesDisponiveis() {
        return getStatusEnum().opcoesPorStatus();
    }

}
