/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao.DriversConexao;

/**
 *
 * @author desenvolvedor
 */
public enum FabDriverConexao {

    JPA_NATIVO, HIBERNATE, WEB_SERVICE;

    public Class getClassesPadrao() {
        switch (this) {
            case JPA_NATIVO:
                return DriverFWBancoJPANativo.class;
            case HIBERNATE:
                return DriverFWBancoHIBERNATE.class;
            case WEB_SERVICE:

                return DriverFWBancoWEBSERVICE.class;
            default:
                throw new AssertionError(this.name());

        }
    }

}
