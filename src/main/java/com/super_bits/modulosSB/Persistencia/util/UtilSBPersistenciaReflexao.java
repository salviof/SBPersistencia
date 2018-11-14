/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.util;

import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import javax.persistence.Entity;

/**
 *
 * @author salvioF
 */
public class UtilSBPersistenciaReflexao {

    public static boolean isObjetoPersistivel(ItfBeanSimples pObjeto) {
        if (pObjeto == null) {
            return false;
        }
        try {
            return pObjeto.getClass().getAnnotation(Entity.class) != null;
        } catch (Throwable t) {
            return false;
        }
    }

}
