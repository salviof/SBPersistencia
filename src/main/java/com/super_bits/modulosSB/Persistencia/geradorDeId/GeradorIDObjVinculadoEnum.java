/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.geradorDeId;

import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCGeradorDeID;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeVinculadoAEnum;
import java.io.Serializable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 *
 * @author desenvolvedor
 */
public class GeradorIDObjVinculadoEnum implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor ssci, Object pObj) throws HibernateException {
        try {
            ComoEntidadeVinculadoAEnum objVinculado = ((ComoEntidadeVinculadoAEnum) pObj);

            return UtilCRCGeradorDeID.gerarIdUnicoObejtoVinculadoAFabrica(objVinculado);

        } catch (Throwable t) {
            throw new HibernateException("Erro gerando id do objeto" + pObj + t.getMessage()+"--"+t.getMessage());
        }

    }

}
