package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;

public interface ItfTabelaLigacao extends ComoEntidadeSimples {

    public ComoEntidadeSimples getCampo1();

    public ComoEntidadeSimples getCampo2();

    public ComoEntidadeSimples getCampoDiferente(String pParamentro);

}
