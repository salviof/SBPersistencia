/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimplesSomenteLeitura;

/**
 *
 * @author desenvolvedor
 */
public interface ItfEntidade extends ItfBeanSimplesSomenteLeitura {

    public void acoesAoAtualizar();

    public void acoesAoSalvarNovo();

}
