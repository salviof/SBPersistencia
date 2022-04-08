/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.ConfigGeral;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import config.ConfigPersistenciaExemplo;
import config.ConfiguradorCoreSBSBpersistencia;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import testesFW.TesteJunitSBPersistencia;

/**
 *
 * @author sfurbino
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DevOpsPersistenciaTest extends TesteJunitSBPersistencia {

    private static DevOpsPersistencia devops;

    @Override
    protected void configAmbienteDesevolvimento() {
        SBCore.configurar(new ConfiguradorCoreSBSBpersistencia(), SBCore.ESTADO_APP.DESENVOLVIMENTO);
        SBPersistencia.configuraJPA(new ConfigPersistenciaExemplo());
    }

    @Test()
    public void atestIniciarBanco() {
        try {
            devops = new DevOpsPersistencia(new ConfigPersistenciaExemplo());
            devops.iniciarBanco(true);
        } catch (Throwable t) {
            fail(t.getMessage());
        }
    }

    /**
     * Test of getArqScriptFinal method, of class DevOpsPersistencia.
     */
    @Test
    public void testGetArqScriptFinal() {

        String result = devops.getArqScriptFinal();
        assertEquals("scriptFinal.sh", result);
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of carregaBanco method, of class DevOpsPersistencia.
     */
    @Test
    public void testCarregaBanco() {
        try {

            devops.carregaBanco();
        } catch (Throwable t) {
            fail(t.getMessage());
        }
    }

    @Test
    public void testGetHashBancoGerado() {
        try {

            assertEquals("O Hash do banco é diferente do esperado para o banco modelo", "2422935908", devops.getHashBancoGerado());
        } catch (Throwable t) {
            fail(t.getMessage());
        }
    }

    /**
     * Test of compilaBanco method, of class DevOpsPersistencia.
     */
    @Test
    public void testCompilaBanco() {
        try {

            devops.compilaBanco();
        } catch (Throwable t) {
            fail(t.getMessage());
        }
        // TODO review the generated test code and remove the default call to fail.

    }

    /**
     * Test of getHashBancoGerado method, of class DevOpsPersistencia.
     */
    /**
     * Test of criaScriptsBancoDeDAdos method, of class DevOpsPersistencia.
     */
    @Test
    public void testCriaScriptsBancoDeDAdos() {
        try {

            devops.criaScriptsBancoDeDAdos(new ConfigPersistenciaExemplo());
        } catch (Throwable t) {
            fail(t.getMessage());

        }
    }

    /**
     * Test of execScriptFinal method, of class DevOpsPersistencia.
     */
    @Test
    public void testExecScriptFinal() {
        System.out.println("execScriptFinal");
        DevOpsPersistencia instance = new DevOpsPersistencia();
        instance.execScriptFinal();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of carregaBancoSeBancoVazio method, of class DevOpsPersistencia.
     */
    @Test
    public void testCarregaBancoSeBancoVazio() {
        System.out.println("carregaBancoSeBancoVazio");
        DevOpsPersistencia instance = new DevOpsPersistencia();
        instance.carregaBancoSeBancoVazio();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of limparBanco method, of class DevOpsPersistencia.
     */
    @Test
    public void testLimparBanco() {
        System.out.println("limparBanco");
        DevOpsPersistencia instance = new DevOpsPersistencia();
        instance.limparBanco();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of loadC3p0 method, of class DevOpsPersistencia.
     */
    @Test
    public void testLoadC3p0() {
        System.out.println("loadC3p0");
        Map<String, Object> pPropriedades = null;
        DevOpsPersistencia instance = new DevOpsPersistencia();
        instance.loadC3p0(pPropriedades);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of carregarConfiguracaoBasicaPadraoMysql method, of class
     * DevOpsPersistencia.
     */
    @Test
    public void testCarregarConfiguracaoBasicaPadraoMysql() {
        System.out.println("carregarConfiguracaoBasicaPadraoMysql");
        Map<String, Object> pPropriedades = null;
        DevOpsPersistencia instance = new DevOpsPersistencia();
        instance.carregarConfiguracaoBasicaPadraoMysql(pPropriedades);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of carregarDadosConexaoPadrao method, of class DevOpsPersistencia.
     */
    @Test
    public void testCarregarDadosConexaoPadrao() {
        System.out.println("carregarDadosConexaoPadrao");
        Map<String, Object> pPropriedades = null;
        DevOpsPersistencia instance = new DevOpsPersistencia();
        instance.carregarDadosConexaoPadrao(pPropriedades);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of carregarAutoLoadPadrao method, of class DevOpsPersistencia.
     */
    @Test
    public void testCarregarAutoLoadPadrao() {
        System.out.println("carregarAutoLoadPadrao");
        Map<String, Object> pPropriedades = null;
        DevOpsPersistencia instance = new DevOpsPersistencia();
        instance.carregarAutoLoadPadrao(pPropriedades);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of iniciarBanco method, of class DevOpsPersistencia.
     */
}
