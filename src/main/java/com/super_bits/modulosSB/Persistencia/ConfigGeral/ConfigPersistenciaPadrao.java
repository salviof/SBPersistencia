/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.ConfigGeral;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ItfFabrica;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author desenvolvedor
 */
public abstract class ConfigPersistenciaPadrao implements ItfConfigSBPersistencia {

    @Override
    public String bancoPrincipal() {
        Properties proppriedadesBasicas = new Properties();
        InputStream stream = this.getClass().getResourceAsStream("SBProjeto.prop");
        try {
            if (stream == null) {
                return SBCore.getGrupoProjeto() + "Model";

            }
            proppriedadesBasicas.load(stream);
            stream.close();
        } catch (Throwable t) {
            return SBCore.getGrupoProjeto() + "Model";
        }
        return proppriedadesBasicas.getProperty("NOME_BANCO");

    }

    @Override
    public String[] bancosExtra() {
        return new String[0];
    }

    @Override
    public String formatoDataBanco() {
        return "yyy-MM-dd";
    }

    @Override
    public String formatoDataUsuario() {
        return "dd/MM/yy";
    }

    @Override
    public String pastaImagensJPA() {
        return "/resources/img";
    }

    /**
     *
     * Metodo chamado no inicio da aplicação para atualizar os valores fixos das
     * tabelas,
     *
     */
    @Override
    public void criarBancoInicial() {
        //configure aqui os comando de SQL que devem ser executados no momento do start da aplicação

    }

    @Override
    public abstract Class<? extends ItfFabrica>[] fabricasRegistrosIniciais();

}
