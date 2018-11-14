/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import java.util.List;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.StatelessSession;

/**
 *
 * @author Marcos Vinicius
 */
public class UtilSBHibernateTools {

    public static Query getConsultaHibernate(EntityManager pEm, String pJPQL) {

        Session sessaoSQLHibernate = pEm.unwrap(Session.class);
        return sessaoSQLHibernate.createQuery(pJPQL);

    }

    public static List getQueryStatLess(EntityManager pEm, String pSql, Class pClasse, Object... parametros) {

        boolean entidadeEnvida = true;
        try {

            try {
                if (pEm == null) {

                    pEm = UtilSBPersistencia.getNovoEM();
                    entidadeEnvida = false;
                }

                Session sessaoSQLHibernate = pEm.unwrap(Session.class);

                StatelessSession sstatlessHibernate = sessaoSQLHibernate.getSessionFactory().openStatelessSession();

                SQLQuery qr = sstatlessHibernate.createSQLQuery(pSql);
                if (pClasse != null) {
                    qr.addEntity(pClasse);
                }

                if (parametros != null) {
                    int i = 0;
                    for (Object pr : parametros) {
                        qr.setParameter(i, pr);
                        i++;
                    }
                }
                return qr.list();
            } catch (Throwable erro) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro execurtando SQL via Hibernate Stateless" + pSql, erro);
                return null;
            }
        } finally {
            if (!entidadeEnvida) {
                if (pEm.isOpen()) {
                    pEm.close();
                }
            }
        }
    }

    public static List getQueryStatLess(String pSQL, Class pClasse, Object... parametros) {
        return getQueryStatLess(UtilSBPersistencia.getNovoEM(), pSQL, pClasse, parametros);
    }

}
