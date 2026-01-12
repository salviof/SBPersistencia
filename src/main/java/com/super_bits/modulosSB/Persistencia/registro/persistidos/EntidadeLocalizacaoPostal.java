/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.CampoEsperado;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoLocalPostagem;

/**
 *
 * @author desenvolvedor
 */
public class EntidadeLocalizacaoPostal extends EntidadeLocalizacao implements ComoLocalPostagem {

    public EntidadeLocalizacaoPostal() {
        super();
        adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.LC_LOCALIZACAO), true);
        adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.LCCEP), true);

    }

    @Override
    public String getLogradouro() {
        return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_LOGRADOURO);
    }

    @Override
    public String getCep() {
        return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LCCEP);
    }

    @Override
    public void setLogradouro(String pLogradouro) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_LOGRADOURO, pLogradouro);
    }

    @Override
    public void setCep(String pCep) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.LCCEP, pCep);
    }

}
