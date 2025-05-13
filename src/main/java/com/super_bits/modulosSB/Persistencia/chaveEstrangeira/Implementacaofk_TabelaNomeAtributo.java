/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.super_bits.modulosSB.Persistencia.chaveEstrangeira;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * TODO QUANDO migrar para hibernate 6, a versão 5 não suporta adequação dos
 * nomes das chaves estrangeiras, pois não impplementa toPhysicalForeignKeyName
 * em PhysicalNamingStrategyStandardImpl
 *
 * @author salvio
 */
public class Implementacaofk_TabelaNomeAtributo extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return super.toPhysicalTableName(name, context); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

}
