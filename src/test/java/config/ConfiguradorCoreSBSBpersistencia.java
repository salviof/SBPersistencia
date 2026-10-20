/*
 *  Desenvolvido pela equipe Super-Bits.com CNPJ 20.019.971/0001-90

 */
package config;

import com.super_bits.modulosSB.SBCore.ConfigGeral.ComoContextoExecCustomizavel;
import com.super_bits.modulosSB.SBCore.ConfigGeral.ConfiguradorCoreDeProjetoJarAbstrato;

/**
 *
 * @author desenvolvedor
 */
public class ConfiguradorCoreSBSBpersistencia extends ConfiguradorCoreDeProjetoJarAbstrato {

    public ConfiguradorCoreSBSBpersistencia() {
        setIgnorarConfiguracaoAcoesDoSistema(true);
        setIgnorarConfiguracaoPermissoes(true);
    }

    @Override
    public void defineFabricasDeACao(ComoContextoExecCustomizavel pConfig) {

    }

}
