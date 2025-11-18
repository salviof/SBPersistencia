/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabrica;

/**
 *
 * @author sfurbino
 */
public enum FabInfoPersistirEntidade implements ComoFabrica {

    MERGE, DELETE, INSERT;

    private InfoPerisistirEntidade novaPersistencia;

    @Override
    public InfoPerisistirEntidade getRegistro() {
        return novaPersistencia;
    }

}
