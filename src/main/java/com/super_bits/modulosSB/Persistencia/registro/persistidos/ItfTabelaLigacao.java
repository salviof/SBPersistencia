package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;

public interface ItfTabelaLigacao extends ItfBeanSimples {

    public ItfBeanSimples getCampo1();

    public ItfBeanSimples getCampo2();

    public ItfBeanSimples getCampoDiferente(String pParamentro);

}
