/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos;

/**
 *
 * @author salvio
 */
public interface ItfEntidadeExtensivelMultiplasSequencias {

    public boolean isEntidadeExtendida();

    public default String nomeSequenciaIdentificacao() {
        return this.getClass().getSimpleName().split("\\$")[0];
    }

    public default Long getIdSequenciaInicial() {
        if (!isEntidadeExtendida()) {
            return 1l;
        } else {
            return 1000000l;
        }
    }

}
