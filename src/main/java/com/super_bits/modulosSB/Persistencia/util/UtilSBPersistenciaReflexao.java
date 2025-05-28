/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.util;

import com.super_bits.modulosSB.Persistencia.geradorDeId.GERADOR_ID_ESTRATEGIA_CONHECIDA;
import com.super_bits.modulosSB.Persistencia.geradorDeId.GeradorIdNomeUnico;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreReflexao;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import java.lang.reflect.Field;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.coletivojava.fw.utilCoreBase.UtilSBCoreReflexaoSimples;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

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

    public static boolean possuiIdeAutogeradoDessasEstrategias(Class<?> entityClass, String... pEstrategias) {

        List<Class> classes = UtilSBCoreReflexao.getClassesComHierarquiaAteCotendoEstaAnotacao(entityClass, Entity.class);
        for (Class classe : classes) {
            for (Field field : classe.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(GenericGenerator.class)) {
                    GenericGenerator valorgerado = field.getAnnotation(GenericGenerator.class);
                    for (String estrategia : pEstrategias) {
                        if (valorgerado.strategy().contains(estrategia)) {
                            return true;
                        }
                    }

                }
            }
        }
        return false;
    }

    public static boolean possuiIdeAutogerado(Class<?> entityClass) {
        List<Class> classes = UtilSBCoreReflexao.getClassesComHierarquiaAteCotendoEstaAnotacao(entityClass, Entity.class);
        for (Class classe : classes) {
            for (Field field : classe.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class) && field.isAnnotationPresent(GeneratedValue.class)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getNomeEntidade(Object entity) {
        Class<?> entidadeClasse = entity.getClass();

        if (Hibernate.isInitialized(entity) && Hibernate.getClass(entity) != entidadeClasse) {
            // Se for um proxy, pega a classe real da entidade
            entidadeClasse = Hibernate.getClass(entity);
        }

        // Pega o nome da classe da entidade
        String nomeClasse = entidadeClasse.getSimpleName();
        return nomeClasse;
    }

}
