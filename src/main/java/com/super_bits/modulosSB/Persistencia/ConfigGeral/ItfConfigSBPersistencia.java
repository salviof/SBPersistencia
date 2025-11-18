/*
 *   Super-Bits.com CODE CNPJ 20.019.971/0001-90

 */
package com.super_bits.modulosSB.Persistencia.ConfigGeral;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabrica;

/**
 *
 * @author Salvio
 */
public interface ItfConfigSBPersistencia {

    public String bancoPrincipal();

    public String[] bancosExtra();

    public String formatoDataBanco();

    public String formatoDataUsuario();

    public String pastaImagensJPA();

    /**
     *
     * As Fabricas de registro iniciais são classes que extendem ComoFabrica e
     * possuem enunciados de registros iniciais do sistema, que devem ser
     * carregados no início da aplicação
     *
     * @return Fabricas do Projeto
     */
    public Class<? extends ComoFabrica>[] fabricasRegistrosIniciais();

    public void criarBancoInicial();

    public default void criarRegraDeNegocioInicial() {

    }

    public default boolean isExibirLogBancoDeDados() {
        return SBCore.isEmModoDesenvolvimento();

    }

}
