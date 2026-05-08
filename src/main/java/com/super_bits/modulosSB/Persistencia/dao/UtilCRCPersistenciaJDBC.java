/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.Persistencia.ConfigGeral.SBPersistencia;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author salvio
 */
public class UtilCRCPersistenciaJDBC {

    private static String urljdbc;
    private static String senhaPadrao;
    private static String usuarioPadrao;
    private static boolean bancoConfigurado = false;

    private static void configurar() {
        if (bancoConfigurado) {
            return;
        }
        Map<String, Object> configuracaoPadrao = new HashMap<>();
        SBPersistencia.getDevOps().carregarDadosConexaoPadrao(configuracaoPadrao);
        urljdbc = (String) configuracaoPadrao.get("javax.persistence.jdbc.url");
        senhaPadrao = (String) configuracaoPadrao.get("javax.persistence.jdbc.password");
        usuarioPadrao = "root";
        bancoConfigurado = true;
    }

    public static Connection getConnection() throws ErroEmBancoDeDados {
        // Exemplo com DriverManager (simples)
        configurar();
        try {
            return DriverManager.getConnection(
                    urljdbc,
                    usuarioPadrao,
                    senhaPadrao
            );
        } catch (SQLException ex) {
            throw new ErroEmBancoDeDados(ex);
        }
    }

    public void executarSQL(String pComando) throws ErroEmBancoDeDados {
        try {
            Connection conexao = getConnection();
            conexao.setAutoCommit(false);

            executarSQL(conexao, pComando);

            conexao.commit();
            conexao.close();
        } catch (SQLException ex) {
            throw new ErroEmBancoDeDados(ex);
        }
    }

    public void executarSQL(List<String> pComandos) throws ErroEmBancoDeDados {

        try {
            Connection conexao = getConnection();
            conexao.setAutoCommit(false);
            for (String comando : pComandos) {
                executarSQL(comando);
            }
            conexao.commit();
            conexao.close();
        } catch (SQLException ex) {
            throw new ErroEmBancoDeDados(ex);
        }
    }

    public static Connection executarSQL(Connection pConexao, String pComando) throws ErroEmBancoDeDados {
        if (pConexao == null) {
            throw new ErroEmBancoDeDados("Conexão nula");
        }

        try {
            try (PreparedStatement ps = pConexao.prepareStatement(pComando)) {
                int rowsAffected = ps.executeUpdate();

                // Log informativo
                System.out.printf("✓ %d registro(s) afetado(s) | %s%n",
                        rowsAffected,
                        pComando.length() > 100 ? pComando.substring(0, 100) + "..." : pComando);
            }
            return pConexao; // retorna a conexão para continuar usando na transação

        } catch (SQLException ex) {
            System.err.println("❌ Erro no comando: " + pComando);
            throw new ErroEmBancoDeDados(ex);
        }
    }

    public static List<Long> listarIds(Connection conn, String sql) throws ErroEmBancoDeDados {
        List<Long> lista = new ArrayList<>();
        try {
            try (PreparedStatement ps = conn.prepareStatement(sql)) {

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(rs.getLong(1));
                    }
                }
            }
        } catch (Throwable t) {
            throw new ErroEmBancoDeDados(t);
        }
        return lista;
    }
}
