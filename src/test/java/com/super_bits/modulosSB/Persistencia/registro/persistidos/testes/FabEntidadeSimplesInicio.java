/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.testes;

import com.super_bits.modulosSB.Persistencia.fabrica.ComoFabricaComPersistencia;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoDaFabrica;

/**
 *
 * @author SalvioF
 */
public enum FabEntidadeSimplesInicio implements ComoFabricaComPersistencia {

    @InfoObjetoDaFabrica(classeObjeto = RegistroTesteSimples.class, nomeObjeto = "Teste 1", id = 1)
    REGISTRO1,
    @InfoObjetoDaFabrica(classeObjeto = RegistroTesteSimples.class, nomeObjeto = "Teste 2", id = 2)
    REGISTRO2,
    @InfoObjetoDaFabrica(classeObjeto = RegistroTesteSimples.class, nomeObjeto = "Teste 3", id = 3)
    REGISTRO3;

}
