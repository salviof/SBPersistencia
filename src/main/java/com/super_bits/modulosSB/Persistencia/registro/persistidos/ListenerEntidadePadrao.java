/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistenciaListener;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanComStatus;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanStatus;
import java.util.HashMap;
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
    protected transient Map<String, Object> propriedadesEstadoAnterior = new HashMap<>();

    @PostLoad
    protected void objetoEstadoAnterior(ItfBeanSimples pEntidade) {

        if (pEntidade instanceof ItfBeanComStatus) {
            ItfBeanComStatus bst = (ItfBeanComStatus) pEntidade;
            ItfBeanStatus sts = bst.getStatusPrincipal();
            propriedadesEstadoAnterior.put(sts.getClass().getSimpleName(), sts);
            String campoId = pEntidade.getNomeCampo(FabTipoAtributoObjeto.ID);
            propriedadesEstadoAnterior.put(campoId, pEntidade.getNome());
        }

    }

    public void protegerSenhas(ItfBeanSimples pEntidade) {

        UtilSBPersistenciaListener.protegerSenhas(pEntidade);
    }

    @PrePersist
    public void acaoAntesDePersistir(ItfBeanSimples pEntidade) {
        UtilSBPersistenciaListener.acaoPadraoAntesPersistirNovoRegistro(pEntidade);

    }

    @PreUpdate
    public void acaoAntesDeAtualizar(ItfBeanSimples pEntidade) {
        UtilSBPersistenciaListener.acaoPadraoAntesDeAtualizar(pEntidade);
    }

    @PostUpdate
    public void acaoAposAtualizar(ItfBeanSimples emp) {

        UtilSBPersistenciaListener.acaoPadraoDepoisDeAtualizar(emp);
    }

    @PostPersist
    public void acaoAposPersistir(ItfBeanSimples emp) {
        UtilSBPersistenciaListener.acaoPadraoAposPersistirNovo(emp);
    }

    @PreRemove
    private void acaoAntesRemover(ItfBeanSimples emp) {

    }

    @PostRemove
    public void acaoAposRemover(ItfBeanSimples emp) {

    }

}
