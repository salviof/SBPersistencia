/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import javax.persistence.EntityManager;

/**
 *
 * @author Salvio
 */
public class SBConexaoBanco {

    private EntityManager em;
    private int timeOut;
    private boolean manterAtiva;

}
