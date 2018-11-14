/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.beanView;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author sfurbino
 */
public class AutoComplete {

    public static enum TIPO_PESQUISA {

        EMPRESA, PESSOA_FISICA, NOME, NOME_ID
    }

    private final List<ItfBeanSimples> listaInicial;
    private final TIPO_PESQUISA tipoPesquisa;
    private final int minimoPesquisa = 4;
    private final EntityManager em;

    private final Class entidade;

    public AutoComplete(TIPO_PESQUISA pTipoPesquisa, Class pEntidade, EntityManager pEM) {
        this.listaInicial = new ArrayList<>();
        entidade = pEntidade;
        tipoPesquisa = pTipoPesquisa;
        em = pEM;
    }

    private List<ItfBeanSimples> makeListaInicial(String parametro) {

        switch (tipoPesquisa) {
            case EMPRESA:
                break;
            case PESSOA_FISICA:
                break;
            case NOME:
                return UtilSBPersistencia.getListaRegistrosLikeNomeCurto("%" + parametro + "%", entidade, em);
            case NOME_ID:
                break;
            default:
                throw new AssertionError(tipoPesquisa.name());

        }
        return new ArrayList<>();
    }

    private List<ItfBeanSimples> atualizaLista(String parametro) {
        // TODO, pesquisa em cima da pesquisa inicial para economizar selects
        return makeListaInicial(parametro);
    }

    public List<ItfBeanSimples> pesquisa(String parametro) {
        if (parametro.length() == 4) {
            return makeListaInicial(parametro);
        } else {
            return atualizaLista(parametro);
        }

    }

}
