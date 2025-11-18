/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.util;

import com.super_bits.modulosSB.Persistencia.fabrica.ComoFabricaComPersistencia;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.testes.RegistroTesteSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoDaFabrica;

/**
 *
 * @author sfurbino
 */
public enum FabTeste implements ComoFabricaComPersistencia {
    @InfoObjetoDaFabrica(classeObjeto = RegistroTesteSimples.class, id = 4, nomeObjeto = "Teste quatro")
    TIPO1,
    @InfoObjetoDaFabrica(classeObjeto = RegistroTesteSimples.class, id = 5, nomeObjeto = "Teste cinco")
    TIPO2,
    @InfoObjetoDaFabrica(classeObjeto = RegistroTesteSimples.class, nomeObjeto = "teste seis")
    TIPO3;

}
