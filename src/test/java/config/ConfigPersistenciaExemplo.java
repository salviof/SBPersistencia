/*
 *   Super-Bits.com CODE CNPJ 20.019.971/0001-90

 */
package config;

import com.super_bits.modulosSB.Persistencia.ConfigGeral.ItfConfigSBPersistencia;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.testes.FabEntidadeSimplesInicio;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreDataHora;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ItfFabrica;

/**
 *
 *
 *
 * @author Sálvio Furbino <salviof@gmail.com>
 * @since 20/07/2014
 *
 */
public class ConfigPersistenciaExemplo implements ItfConfigSBPersistencia {

    @Override
    public String bancoPrincipal() {
        return "bancoModelo";
    }

    @Override
    public String[] bancosExtra() {
        return null;
    }

    @Override
    public String formatoDataBanco() {
        return UtilSBCoreDataHora.datahoraSistemaFr.toString();
    }

    @Override
    public String formatoDataUsuario() {
        return UtilSBCoreDataHora.horaUsuarioFr.toString();
    }

    @Override
    public String pastaImagensJPA() {
        return "/img";
    }

    @Override
    public void criarBancoInicial() {
        System.out.println("Metodo criação de dados iniciais não foi implementado");
    }

    @Override
    public Class<? extends ItfFabrica>[] fabricasRegistrosIniciais() {
        return new Class[]{FabEntidadeSimplesInicio.class};
    }

}
