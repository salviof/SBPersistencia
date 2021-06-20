/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringFiltros;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringValidador;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author SalvioF
 */
public abstract class UtilSBPersistenciaMysql {

    /**
     *
     * Retorna o nome da coluna vinculado ao erro de contrant do mysql
     *
     * @param pMensagemErroChave
     * @return
     */
    public static List<String> colunasVinculadas_erro_chaveDB(String pMensagemErroChave) {

        String[] chaveRegex = pMensagemErroChave.split("(UK_\\S*)");
        Pattern p = Pattern.compile("(UK_\\S*)");

        Matcher m = p.matcher(pMensagemErroChave);
        m.find();
        String codigo = m.group(0);
        codigo = codigo.replace("'", "");
        codigo = codigo.replace("`", "");
        if (chaveRegex != null && chaveRegex.length > 0) {

            List<Object> resp = UtilSBPersistencia.getListaRegistrosBySQL(
                    "SELECT * FROM information_schema.KEY_COLUMN_USAGE where CONSTRAINT_NAME=\"" + codigo + "\"", 0);
            Object[] teste = (Object[]) resp.get(0);
            List<String> resposta = new ArrayList<>();
            resposta.add((String) teste[6]);
            return resposta;
        } else {
            return new ArrayList<>();
        }
    }

    /**
     *
     * Retorna o nome da coluna vinculado ao erro de contrant do mysql
     *
     * @param pMensagemErroChave
     * @return
     */
    public static List<String> tabelaVinculadasAChaveExtrangeira(String pMensagemErroChave) {

        Pattern p;

        if (pMensagemErroChave.contains("UK")) {
            p = Pattern.compile("(UK\\S*)");
        } else {
            p = Pattern.compile("(FK\\S*)");
        }

        Matcher m = p.matcher(pMensagemErroChave);
        m.find();

        String codigo = m.group(0);

        codigo = codigo.replace("'", "");
        codigo = codigo.replace("`", "");
        codigo = UtilSBCoreStringFiltros.removeCaracteresEspeciais(codigo);
        if (codigo != null) {

            List<Object> resp = UtilSBPersistencia.getListaRegistrosBySQL(
                    "SELECT * FROM information_schema.KEY_COLUMN_USAGE where CONSTRAINT_NAME=\"" + codigo + "\"", 0);
            Object[] teste = (Object[]) resp.get(0);
            List<String> resposta = new ArrayList<>();
            if (pMensagemErroChave.contains("UK")) {
                if (teste[5] != null) {
                    resposta.add((String) teste[5]);
                }
            } else {
                if (teste[10] != null) {
                    resposta.add((String) teste[5]);
                }
            }
            return resposta;
        } else {
            return new ArrayList<>();
        }
    }

}
