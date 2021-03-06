/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.ErroRegraDeNegocio;
import javax.persistence.EntityManager;

/**
 *
 * @author desenvolvedor
 */
public interface ItfExecucaoRegraDeNegocio {

    public abstract void regraDeNegocio() throws ErroRegraDeNegocio;

    /**
     * Executa a ação de gestão de Entity Manager
     */
    public void executarAcao() throws ErroEmBancoDeDados, ErroRegraDeNegocio;

    /**
     * Executa as operções finais, e
     *
     * @throws ErroEmBancoDeDados informações preciosas sobre o erro ao executar
     * operações finais (como commit)
     */
    public void executarAcoesFinais() throws ErroEmBancoDeDados;

    public void reverterAcoesFinais() throws ErroEmBancoDeDados;

    public void executarAcoesIniciais() throws ErroEmBancoDeDados;

    public EntityManager getEm();

}
