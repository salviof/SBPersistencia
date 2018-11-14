/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.polimofirsmoRegraDeNegocio;

import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import java.util.List;

/**
 *
 * @author desenvolvedor
 */
public class TipoPolimorfismoObjetoRegraDeNegocio {

    private int id;
    private Class classePrincipal;
    private Class classeExemplo;
    private String nome;
    private String descricao;
    private List<FabTipoAtributoObjeto> camposDesejaveis;
    private List<FabTipoAtributoObjeto> camposObrigatorios;

}
