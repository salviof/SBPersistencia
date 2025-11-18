/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP;

import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import javax.persistence.Entity;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ComoLocalPostagem;

/**
 *
 * @author desenvolvedor
 */
@Entity
@InfoObjetoSB(tags = {"Endereço"}, plural = "Endereços")
public class LocalizacaoPostavel extends Localizacao implements ComoLocalPostagem {

    @InfoCampo(label = "logradouro", tipo = FabTipoAtributoObjeto.LC_LOGRADOURO)
    private String logradouro;

    @InfoCampo(label = "CEP", tipo = FabTipoAtributoObjeto.LCCEP)
    private String cep;

    @Override
    public String getLogradouro() {
        return logradouro;
    }

    @Override
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    @Override
    public String getCep() {
        return cep;
    }

    @Override
    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public boolean isLocaPostavel() {
        return (this instanceof ComoLocalPostagem);
    }

    @Override
    public ComoLocalPostagem getComoLocalPostavel() {
        if (!isLocaPostavel()) {
            throw new UnsupportedOperationException("Este local não é postável, impossível usar o framework de CEP");
        }
        return (ComoLocalPostagem) this;
    }

}
