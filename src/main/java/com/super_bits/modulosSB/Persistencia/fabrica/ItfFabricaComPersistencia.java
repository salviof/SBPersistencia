/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.fabrica;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ItfFabrica;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimplesSomenteLeitura;
import java.util.Map;
import javax.persistence.EntityManager;
import org.coletivojava.fw.utilCoreBase.UtilSBCoreFabrica;

/**
 *
 * @author sfurbino
 */
public interface ItfFabricaComPersistencia extends ItfFabrica {

    /**
     *
     * Retorna o Registro executando Load do jpa
     *
     * @param pEm
     * @return
     */
    public default Object getRegistro(EntityManager pEm) {
        return UtilSBPersistencia.loadEntidade((ItfBeanSimplesSomenteLeitura) getRegistro(), pEm);
    }

    public default Object getObjetoReuso(Map<String, Object> objetos, ItfFabricaComPersistencia pObjeto) {
        if (pObjeto == null) {
            return null;
        }
        Object objeto = objetos.get(pObjeto.toString());
        if (objeto == null) {
            objetos.put(pObjeto.toString(), UtilSBCoreFabrica.getRegistroPorEnum(pObjeto));
        }
        return objetos.get(pObjeto.toString());
    }
};
