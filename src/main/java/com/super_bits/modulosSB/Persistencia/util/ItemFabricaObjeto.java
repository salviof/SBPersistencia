/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.super_bits.modulosSB.Persistencia.util;

import com.super_bits.modulosSB.SBCore.modulos.fabrica.ItfFabrica;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.ItemSimples;

/**
 *
 * @author salvio
 */
public class ItemFabricaObjeto extends ItemSimples implements ItfBeanSimples {

    @InfoCampo(tipo = FabTipoAtributoObjeto.ENUM_FABRICA)
    private final ItfFabrica fabrica;

    private final ItfBeanSimples objeto;
    @InfoCampo(tipo = FabTipoAtributoObjeto.QUANTIDADE)
    private int ordemEnum;
    @InfoCampo(tipo = FabTipoAtributoObjeto.ID)
    private Long idEntidade;
    @InfoCampo(tipo = FabTipoAtributoObjeto.NOME)
    private String nome;

    public ItemFabricaObjeto(ItfFabrica fabrica) {
        this.fabrica = fabrica;
        this.objeto = (ItfBeanSimples) fabrica.getRegistro();
        this.ordemEnum = ((Enum) fabrica).ordinal();
        this.idEntidade = objeto.getId();
        this.nome = fabrica.toString();

    }

    @Override
    public Long getId() {
        return idEntidade;
    }

    @Override
    public void setId(Long pID) {
        throw new UnsupportedOperationException("Não permitido alterar o id do item Fabrica");
    }

    @Override
    public String getNome() {
        return fabrica.toString();
    }

    public ItfFabrica getFabrica() {
        return fabrica;
    }

    public ItfBeanSimples getObjeto() {
        return objeto;
    }

    public int getOrdemEnum() {
        return ordemEnum;
    }

    public void setOrdemEnum(int ordemEnum) {
        this.ordemEnum = ordemEnum;
    }

}
