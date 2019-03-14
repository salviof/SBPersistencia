/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

/**
 *
 * @author novy
 */
public interface ItfExecucaoRegraDeNegocioComGestaodeEntityManager extends ItfExecucaoRegraDeNegocio {

    public void fecharEntityManagerEmSeguranca();
}
