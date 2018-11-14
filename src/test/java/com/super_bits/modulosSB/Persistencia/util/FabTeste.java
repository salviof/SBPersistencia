/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.util;

import com.super_bits.modulosSB.SBCore.modulos.fabrica.ItfFabrica;

/**
 *
 * @author sfurbino
 */
public enum FabTeste implements ItfFabrica {

    tipo1 {

        @Override
        public ItemTeste getRegistro() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }, tipo2 {

        @Override
        public ItemTeste getRegistro() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }, tipo3 {

        @Override
        public ItemTeste getRegistro() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    };

    @Override
    public abstract ItemTeste getRegistro();
}
