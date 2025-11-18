/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
import java.util.List;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author sfurbino
 */
public class InfoPerisistirEntidade {

    private Object entidade;
    private List<Object> entidades;
    private EntityManager em;
    private FabInfoPersistirEntidade tipoAlteracao;

    private enum TIPO_REGISTRO {

        UNICO, MULTIPLOS, NULO
    }

    public InfoPerisistirEntidade(Object pEntidade, List<Object> pEntidades, EntityManager pEM, FabInfoPersistirEntidade tipoAlteracao) {
        this.entidade = pEntidade;
        this.entidades = pEntidades;
        this.em = pEM;
        this.tipoAlteracao = tipoAlteracao;
    }

    private String getValoresRegistro() {
        String resposta = "";

        try {
            switch (getTipoRegistro()) {

                case UNICO:
                    ComoEntidadeSimples entidadeUnica = (ComoEntidadeSimples) entidade;
                    resposta += "nome:" + entidadeUnica.getNomeCurto() + " id:" + entidadeUnica.getId();
                    break;
                case MULTIPLOS:
                    ComoEntidadeSimples entidade = (ComoEntidadeSimples) entidades.get(0);
                    resposta += "nome:" + entidade.getNomeCurto() + "id:" + entidade.getId();
                    break;
                case NULO:
                    return "registro nulo";

            }
        } catch (Throwable erro) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro em tentativa de obter id e nomeCurto do Objeto", erro);
            resposta = "Registro sem possibilidade de leitura";
        }
        return resposta;

    }

    private TIPO_REGISTRO getTipoRegistro() {
        if (entidade != null) {
            return TIPO_REGISTRO.UNICO;

        }
        if (entidade != null) {
            if (entidades.size() > 0) {
                return TIPO_REGISTRO.MULTIPLOS;
            }
        }
        return TIPO_REGISTRO.NULO;
    }

    private String entidadesEnviadas() {
        String resposta = "Registro nulo";
        if (entidade != null) {
            resposta = entidade.getClass().getName();

        }
        if (entidades != null) {
            if (entidades.size() > 0) {
                resposta = entidades.get(0).getClass().getName();
            }
        }
        return resposta;

    }

    public InfoPerisistirEntidade(FabInfoPersistirEntidade pTipoAlteracao) {
        tipoAlteracao = pTipoAlteracao;
    }

    @Override
    public String toString() {
        String relatorio = "Informações sobre o InfoPersistencia Entidade: \n" + "Entidade: " + entidadesEnviadas() + "->"
                + getValoresRegistro();
        return relatorio;
    }

    public boolean isTemRegistroParaPersistir() {
        if (entidade != null) {
            return true;
        }
        if (entidade == null) {

            if (entidades == null) {
                return false;
            }
            if (entidades.isEmpty()) {
                return false;
            }

        }
        if (entidades != null) {
            return entidades.size() > 0;
        }
        return false;
    }

    public Object getpEntidade() {
        return entidade;
    }

    public void setpEntidade(Object pEntidade) {
        this.entidade = pEntidade;
    }

    public List<Object> getpEntidades() {
        return entidades;
    }

    public void setpEntidades(List<Object> pEntidades) {
        this.entidades = pEntidades;
    }

    public EntityManager getpEM() {
        return em;
    }

    public void setpEM(EntityManager pEM) {
        this.em = pEM;
    }

    public FabInfoPersistirEntidade getTipoAlteracao() {
        return tipoAlteracao;
    }

    public void setTipoAlteracao(FabInfoPersistirEntidade tipoAlteracao) {
        this.tipoAlteracao = tipoAlteracao;
    }

}
