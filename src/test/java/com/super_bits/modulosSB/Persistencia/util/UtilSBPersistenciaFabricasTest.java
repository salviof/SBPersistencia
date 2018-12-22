/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.util;

import config.ConfiguradorCoreSBSBpersistencia;
import config.ConfigPersistenciaExemplo;
import com.super_bits.modulosSB.Persistencia.ConfigGeral.SBPersistencia;
import testesFW.TesteJunitSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author sfurbino
 */
public class UtilSBPersistenciaFabricasTest extends TesteJunitSBPersistencia {

    public UtilSBPersistenciaFabricasTest() {
    }

    @Before
    public void setUp() {
    }

    /**
     * Test of persistirRegistrosDaFabrica method, of class
     * UtilSBPersistenciaFabricas.
     */
    @Test
    public void testPersistirRegistrosDaFabrica() {
        UtilSBPersistenciaFabricas.persistirRegistrosDaFabrica(FabTeste.class, getEMTeste(), UtilSBPersistenciaFabricas.TipoOrdemGravacao.ORDERNAR_POR_ID);
    }

    @Override
    protected void configAmbienteDesevolvimento() {
        SBCore.configurar(new ConfiguradorCoreSBSBpersistencia(), SBCore.ESTADO_APP.DESENVOLVIMENTO);
        SBPersistencia.configuraJPA(new ConfigPersistenciaExemplo());
    }

}
