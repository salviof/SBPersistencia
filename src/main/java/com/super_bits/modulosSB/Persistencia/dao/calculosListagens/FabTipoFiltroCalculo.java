/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao.calculosListagens;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author SalvioF
 */
public enum FabTipoFiltroCalculo {

    SOMA_QTD, REGISTRO_UNICO, MAIOR, MENOR, LISTAGENS;

    public CriteriaQuery gerarCriteriaQuery(Class pClassePricipal, CriteriaBuilder builderCriterio) {
        CriteriaQuery criterioQuery = null;
        Root entidadePrincipal = null;
        switch (this) {
            case SOMA_QTD:
                criterioQuery = builderCriterio.createQuery(Long.class);
                entidadePrincipal = criterioQuery.from(pClassePricipal);
                criterioQuery.select(builderCriterio.count(entidadePrincipal.get("id")));
                return criterioQuery;
            case REGISTRO_UNICO:
                criterioQuery = builderCriterio.createQuery(pClassePricipal);
                criterioQuery.from(pClassePricipal);
                return criterioQuery;
            case MAIOR:
                throw new UnsupportedOperationException("Pesquisa por maior registro ainda não foi implementado");

            case MENOR:
                throw new UnsupportedOperationException("Pesquisa por menor registro ainda não foi implementado");

            case LISTAGENS:
                criterioQuery = builderCriterio.createQuery(pClassePricipal);
                criterioQuery.from(pClassePricipal);
                return criterioQuery;

            default:
                throw new UnsupportedOperationException("A pesquisa padrão por criteria do tipo " + this + " não foi implementado ainda");

        }

    }

}
