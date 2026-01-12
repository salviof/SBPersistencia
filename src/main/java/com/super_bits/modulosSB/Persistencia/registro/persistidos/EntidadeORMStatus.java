/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ComoAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabricaStatus;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.CampoEsperado;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoStatus;
import java.util.List;

/**
 *
 * @author desenvolvedor
 */
public class EntidadeORMStatus extends EntidadeSimplesORM implements ComoStatus {

    public EntidadeORMStatus() {
        super();
        adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.STATUS_ENUM), true);
    }

    @Override
    public ComoFabricaStatus getStatusEnum() {
        return (ComoFabricaStatus) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.STATUS_ENUM);
    }

    @Override
    public void setStatusEnum(ComoFabricaStatus pFab) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.STATUS_ENUM, pFab);
    }

    public List<ComoAcaoDoSistema> getAcoesDisponiveis() {
        return getStatusEnum().opcoesPorStatus();
    }

}
