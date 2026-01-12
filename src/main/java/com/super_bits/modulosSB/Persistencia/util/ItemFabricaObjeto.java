/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.super_bits.modulosSB.Persistencia.util;

import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabrica;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoEntidadeSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.EntidadeSimples;

/**
 *
 * @author salvio
 */
public class ItemFabricaObjeto extends EntidadeSimples implements ComoEntidadeSimples {

    @InfoCampo(tipo = FabTipoAtributoObjeto.ENUM_FABRICA)
    private final ComoFabrica fabrica;

    private final ComoEntidadeSimples objeto;
    @InfoCampo(tipo = FabTipoAtributoObjeto.QUANTIDADE)
    private int ordemEnum;
    @InfoCampo(tipo = FabTipoAtributoObjeto.ID)
    private Long idEntidade;
    @InfoCampo(tipo = FabTipoAtributoObjeto.NOME)
    private String nome;

    public ItemFabricaObjeto(ComoFabrica fabrica) {
        this.fabrica = fabrica;
        this.objeto = (ComoEntidadeSimples) fabrica.getRegistro();
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

    public ComoFabrica getFabrica() {
        return fabrica;
    }

    public ComoEntidadeSimples getObjeto() {
        return objeto;
    }

    public int getOrdemEnum() {
        return ordemEnum;
    }

    public void setOrdemEnum(int ordemEnum) {
        this.ordemEnum = ordemEnum;
    }

}
