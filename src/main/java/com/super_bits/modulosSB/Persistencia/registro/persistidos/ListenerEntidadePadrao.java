/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistenciaListener;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoTemStatus;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoEntidadeSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoStatus;
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
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.entidadeEscuta.ComoListenerPersistenciaEntidade;

/**
 *
 * @author desenvolvedor
 */
public class ListenerEntidadePadrao implements ComoListenerPersistenciaEntidade {

    @Transient
    protected transient Map<String, Object> propriedadesEstadoAnterior = new HashMap<>();

    @PostLoad
    protected void objetoEstadoAnterior(ComoEntidadeSimples pEntidade) {

        if (pEntidade instanceof ComoTemStatus) {
            ComoTemStatus bst = (ComoTemStatus) pEntidade;
            ComoStatus sts = bst.getStatusPrincipal();
            if (sts != null) {
                propriedadesEstadoAnterior.put(sts.getClass().getSimpleName(), sts);

                String campoId = pEntidade.getNomeCampo(FabTipoAtributoObjeto.ID);
                propriedadesEstadoAnterior.put(campoId, pEntidade.getNome());
            }
        }
    }

    public void protegerSenhas(ComoEntidadeSimples pEntidade) {

        UtilSBPersistenciaListener.protegerSenhas(pEntidade);
    }

    @PrePersist
    @Override
    public void acaoAntesDePersistir(ComoEntidadeSimples pEntidade) {
        UtilSBPersistenciaListener.acaoPadraoAntesPersistirNovoRegistro(pEntidade);

    }

    @PreUpdate
    @Override
    public void acaoAntesDeAtualizar(ComoEntidadeSimples pEntidade) {
        UtilSBPersistenciaListener.acaoPadraoAntesDeAtualizar(pEntidade);

    }

    @PostUpdate
    @Override
    public void acaoAposAtualizar(ComoEntidadeSimples emp) {

        UtilSBPersistenciaListener.acaoPadraoDepoisDeAtualizar(emp);

    }

    @PostPersist
    @Override
    public void acaoAposPersistir(ComoEntidadeSimples emp) {
        UtilSBPersistenciaListener.acaoPadraoAposPersistirNovo(emp);
    }

    @PreRemove
    @Override
    public void acaoAntesRemover(ComoEntidadeSimples emp) {

    }

    @PostRemove
    @Override
    public void acaoAposRemover(ComoEntidadeSimples emp) {

    }

}
