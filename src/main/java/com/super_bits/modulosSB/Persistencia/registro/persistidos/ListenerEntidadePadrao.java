/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.Persistencia.util.UtilSBPersistenciaReflexao;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanComStatus;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanStatus;
import java.util.Date;
import java.util.Map;
import javax.persistence.PostLoad;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Transient;

/**
 *
 * @author desenvolvedor
 */
public class ListenerEntidadePadrao {

    @Transient
    private transient Map<String, Object> propriedadesEstadoAnterior;

    @PostLoad
    private void objetoEstadoAnterior(ItfBeanSimples pEntidade) {

        if (pEntidade instanceof ItfBeanComStatus) {
            ItfBeanComStatus bst = (ItfBeanComStatus) pEntidade;
            ItfBeanStatus sts = bst.getStatusPrincipal();
            propriedadesEstadoAnterior.put(sts.getClass().getSimpleName(), sts);
            String campoId = pEntidade.getNomeCampo(FabTipoAtributoObjeto.ID);
            propriedadesEstadoAnterior.put(campoId, pEntidade.getNome());
        }

    }

    @PrePersist
    public void acaoAntesDePersistir(ItfBeanSimples pEntidade) {
        System.out.println("Ação Automatica Antes de Persistir");

        if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.REG_DATAINSERCAO)) {
            pEntidade.getCampoInstanciadoByNomeOuAnotacao(FabTipoAtributoObjeto.REG_DATAINSERCAO.name()).setValor(new Date());
        }
        if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.REG_USUARIO_INSERCAO)) {
            if (UtilSBPersistenciaReflexao.isObjetoPersistivel(SBCore.getUsuarioLogado())) {
                pEntidade.getCampoInstanciadoByNomeOuAnotacao(FabTipoAtributoObjeto.REG_USUARIO_INSERCAO.name()).setValor(SBCore.getUsuarioLogado());
            }
        }

    }

    @PreUpdate
    public void acaoAntesDeAtualizar(ItfBeanSimples pEntidade) {
        if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.REG_DATAALTERACAO)) {
            pEntidade.getCampoInstanciadoByNomeOuAnotacao(FabTipoAtributoObjeto.REG_DATAALTERACAO.name()).setValor(new Date());
        }
        if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.REG_USUARIO_ALTERACAO)) {
            if (UtilSBPersistenciaReflexao.isObjetoPersistivel(SBCore.getUsuarioLogado())) {
                pEntidade.getCampoInstanciadoByNomeOuAnotacao(FabTipoAtributoObjeto.REG_USUARIO_ALTERACAO.name()).setValor(SBCore.getUsuarioLogado());
            }
        }
    }

    @PostUpdate
    public void acaoAposAtualizar(ItfBeanSimples emp) {
        if (emp instanceof ItfBeanComStatus) {
            //disparar eventos de alteração de Status, como eventos de comunicação
        }
    }

    @PostPersist
    public void acaoAposPersistir(ItfBeanSimples emp) {
        if (emp instanceof ItfBeanComStatus) {
            //disparar eventos de alteração de Status, como eventos de comunicação
        }
    }

    @PreRemove
    private void acaoAntesRemover(ItfBeanSimples emp) {

    }

    @PostRemove
    public void acaoAposRemover(ItfBeanSimples emp) {

    }

    @PostLoad
    public void acaoAposCarregar(ItfBeanSimples emp) {

    }
}
