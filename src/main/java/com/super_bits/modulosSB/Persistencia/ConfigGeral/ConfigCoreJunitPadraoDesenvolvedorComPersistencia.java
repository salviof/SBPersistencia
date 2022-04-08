/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.ConfigGeral;

import com.super_bits.modulosSB.SBCore.ConfigGeral.ItfConfiguracaoCoreCustomizavel;
import com.super_bits.modulosSB.SBCore.modulos.comunicacao.CentralComunicacaoDesktop;

/**
 *
 * @author desenvolvedorninja01
 * @since 25/10/2019
 * @version 1.0
 */
public class ConfigCoreJunitPadraoDesenvolvedorComPersistencia extends ConfiguradorCoreDeProjetoJarPersistenciaAbstrato {

    @Override
    public void defineFabricasDeACao(ItfConfiguracaoCoreCustomizavel pConfig) {
        pConfig.setCentralComunicacao(CentralComunicacaoDesktop.class);
    }

}
