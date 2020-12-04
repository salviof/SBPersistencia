/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.ConfigGeral;

import com.super_bits.modulosSB.Persistencia.centralLocalizacao.CentralLocalizacaoSBPersistencia;
import com.super_bits.modulosSB.Persistencia.centralOrigemDados.CentralAtributosSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.ConfiguradorCoreDeProjetoJarAbstrato;
import com.super_bits.modulosSB.SBCore.ConfigGeral.FabTipoProjeto;
import com.super_bits.modulosSB.SBCore.ConfigGeral.ItfConfiguracaoCoreCustomizavel;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.comunicacao.CentralComunicacaoDesktop;

/**
 *
 * @author desenvolvedor
 */
public abstract class ConfiguradorCoreDeProjetoJarPersistenciaAbstrato extends ConfiguradorCoreDeProjetoJarAbstrato {

    public ConfiguradorCoreDeProjetoJarPersistenciaAbstrato(boolean buscarArquivoConfiguracaoEmResource) {
        super(buscarArquivoConfiguracaoEmResource);

    }

    public ConfiguradorCoreDeProjetoJarPersistenciaAbstrato() {
        super(true);
    }

    @Override
    public void defineClassesBasicas(ItfConfiguracaoCoreCustomizavel pConfiguracao) {
        super.defineClassesBasicas(pConfiguracao); //To change body of generated methods, choose Tools | Templates.
        setIgnorarConfiguracaoPermissoes(false);
        setIgnorarConfiguracaoAcoesDoSistema(false);
        if (SBCore.isEmModoDesenvolvimento()) {
            pConfiguracao.setCentralComunicacao(CentralComunicacaoDesktop.class);
        }
        pConfiguracao.setCentralDados(CentralDadosJPAPadrao.class);
        pConfiguracao.setCentralAtributoDados(CentralAtributosSBPersistencia.class);
        pConfiguracao.setCentralDeLocalizacao(CentralLocalizacaoSBPersistencia.class);

        pConfiguracao.setCentralDados(CentralDadosJPAPadrao.class);
        pConfiguracao.setTipoProjeto(FabTipoProjeto.MODEL_E_CONTROLLER);

    }

}
