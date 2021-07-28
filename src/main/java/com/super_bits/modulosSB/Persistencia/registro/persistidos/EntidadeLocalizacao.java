/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfBairro;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfLocal;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfLocalPostagem;

/**
 *
 * @author desenvolvedor
 */
public class EntidadeLocalizacao extends EntidadeSimples implements ItfLocal {

    public EntidadeLocalizacao() {
        super();
        //adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.LATITUDE), true);
        //adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.Longitude), true);
        //adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.LC_BAIRRO), true);

    }

    @Override
    public long getLongitude() {
        return (long) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.Longitude);
    }

    @Override
    public long getLatitude() {
        return (long) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LATITUDE);
    }

    @Override
    public void setLatitude(long pLatitude) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.LATITUDE, pLatitude);
    }

    @Override
    public void setLongitude(long pLongitude) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.Longitude, pLongitude);
    }

    @Override
    public ItfBairro getBairro() {
        return (ItfBairro) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_BAIRRO);
    }

    @Override
    public void setBairro(ItfBairro bairro) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_BAIRRO, bairro);
    }

    @Override
    public String getComplemento() {
        return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_COMPLEMENTO_E_NUMERO);
    }

    @Override
    public void setComplemento(String pComplemento) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_COMPLEMENTO_E_NUMERO, pComplemento);
    }

    @Override
    public boolean isLocaPostavel() {
        return (this instanceof ItfLocalPostagem);
    }

    @Override
    public ItfLocalPostagem getComoLocalPostavel() {
        if (!isLocaPostavel()) {
            throw new UnsupportedOperationException("Entidade " + this.getClass().getSimpleName() + " não é uma localização postável, impossível utilizar framework de cep");
        }
        return (ItfLocalPostagem) this;
    }

}
