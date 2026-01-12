/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoBairro;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoLocal;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoLocalPostagem;

/**
 *
 * @author desenvolvedor
 */
public class EntidadeLocalizacao extends EntidadeSimplesORM implements ComoLocal {

    public EntidadeLocalizacao() {
        super();
        //adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.LATITUDE), true);
        //adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.Longitude), true);
        //adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.LC_BAIRRO), true);

    }

    @Override
    public double getLongitude() {
        return (double) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LONGITUDE);
    }

    @Override
    public double getLatitude() {
        return (double) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LATITUDE);
    }

    @Override
    public void setLatitude(double pLatitude) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.LATITUDE, pLatitude);
    }

    @Override
    public void setLongitude(double pLongitude) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.LONGITUDE, pLongitude);
    }

    @Override
    public ComoBairro getBairro() {
        return (ComoBairro) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_BAIRRO);
    }

    @Override
    public void setBairro(ComoBairro bairro) {
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
        return (this instanceof ComoLocalPostagem);
    }

    @Override
    public ComoLocalPostagem getComoLocalPostavel() {
        if (!isLocaPostavel()) {
            throw new UnsupportedOperationException("Entidade " + this.getClass().getSimpleName() + " não é uma localização postável, impossível utilizar framework de cep");
        }
        return (ComoLocalPostagem) this;
    }

}
