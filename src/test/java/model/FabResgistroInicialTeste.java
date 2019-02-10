/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import com.super_bits.modulosSB.Persistencia.fabrica.ItfFabricaComPersistencia;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoDaFabrica;

/**
 *
 * @author desenvolvedor
 */
public enum FabResgistroInicialTeste implements ItfFabricaComPersistencia {

    @InfoObjetoDaFabrica(id = 1, nomeObjeto = "Registro 1")
    REGISTRO1,
    @InfoObjetoDaFabrica(id = 2, nomeObjeto = "Registro 2")
    REGISTRO2

}
