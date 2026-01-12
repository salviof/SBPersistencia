/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.fabrica;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabrica;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoDaFabrica;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoEntidadeSimplesSomenteLeitura;
import java.util.Map;
import javax.persistence.EntityManager;
import org.coletivojava.fw.utilCoreBase.UtilCRCFabrica;

/**
 *
 * @author sfurbino
 */
public interface ComoFabricaComPersistencia extends ComoFabrica {

    /**
     *
     * Retorna o Registro executando Load do jpa
     *
     * @param pEm
     * @return
     */
    public default Object getRegistro(EntityManager pEm) {
        InfoObjetoDaFabrica infoItem = UtilCRCFabrica.getDadosDoRegistro(this);
        return UtilSBPersistencia.getRegistroByID(infoItem.classeObjeto(), (long) infoItem.id(), pEm);
    }

    public default Object getObjetoReuso(Map<String, Object> objetos, ComoFabricaComPersistencia pObjeto) {
        if (pObjeto == null) {
            return null;
        }
        Object objeto = objetos.get(pObjeto.toString());
        if (objeto == null) {
            objetos.put(pObjeto.toString(), UtilCRCFabrica.getRegistroPorEnum(pObjeto));
        }
        return objetos.get(pObjeto.toString());
    }
};
