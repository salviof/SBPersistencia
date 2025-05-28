/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.super_bits.modulosSB.Persistencia.geradorDeId;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.ItfEntidadeExtensivel;
import com.super_bits.modulosSB.Persistencia.util.UtilSBPersistenciaReflexao;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import org.hibernate.id.IdentifierGenerator;

/**
 *
 * @author salvio
 */
public class GeradorIdDuploControleIncremental implements IdentifierGenerator {

    private static List<String> chavesExistentes = new ArrayList<>();

    @Override
    public synchronized Serializable generate(SharedSessionContractImplementor session, Object entity) throws HibernateException {
        // Obtém o valor do discriminador

        String nomeEntidade = UtilSBPersistenciaReflexao.getNomeEntidade(entity);

        try {
            Connection conn = session.connection();
            if (!chavesExistentes.contains(nomeEntidade)) {
                PreparedStatement selectStmt = conn.prepareStatement(
                        "SELECT proximo_id FROM controle_id_especial WHERE nome_classe = ?");
                selectStmt.setString(1, nomeEntidade);
                ResultSet rs = selectStmt.executeQuery();
                if (!rs.next()) {
                    if (entity instanceof ItfEntidadeExtensivel) {
                        if (((ItfEntidadeExtensivel) entity).isEntidadeExtendida()) {
                            UtilSBPersistencia.executaSQL("INSERT IGNORE INTO controle_id_especial (nome_classe, proximo_id) VALUES ('" + nomeEntidade + "', 1000000);");
                        } else {
                            UtilSBPersistencia.executaSQL("INSERT IGNORE INTO controle_id_especial (nome_classe, proximo_id) VALUES ('" + nomeEntidade + "', 1);");
                        }
                    } else {
                        UtilSBPersistencia.executaSQL("INSERT IGNORE INTO controle_id_especial (nome_classe, proximo_id) VALUES ('" + nomeEntidade + "', 1);");
                    }
                }
                chavesExistentes.add(nomeEntidade);
            }
            PreparedStatement selectStmt = conn.prepareStatement(
                    "SELECT proximo_id FROM controle_id_especial WHERE nome_classe = ? FOR UPDATE");
            selectStmt.setString(1, nomeEntidade);
            ResultSet rs = selectStmt.executeQuery();

            if (!rs.next()) {
                throw new HibernateException("Registro de controle_id_especial não encontrado para: " + nomeEntidade);
            }

            long proximoId = rs.getLong(1);
            rs.close();
            selectStmt.close();

            PreparedStatement updateStmt = conn.prepareStatement(
                    "UPDATE controle_id_especial SET proximo_id = ? WHERE nome_classe = ?");
            updateStmt.setLong(1, proximoId + 1);
            updateStmt.setString(2, nomeEntidade);
            updateStmt.executeUpdate();
            updateStmt.close();

            return proximoId;

        } catch (SQLException e) {
            throw new HibernateException("Erro ao gerar ID customizado", e);
        }
    }
}
