/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import java.util.List;

/**
 *
 * @author Marcos Vinicius
 */
public interface ItfCRUDDataSet<T extends ItfBeanSimples> {

    public void anterior();

    public void delete();

    public int getIndexRegistroAtual();

    public int getQuantidade();

    public T getRegistro();

    public List<T> getRegistros();

    public void novo();

    public void proximo();

    public void refesh();

    public void salvar();

    public void setIndexRegistroAtual(int indexRegistroAtual);

    public void setRegistro(T registro);

}
