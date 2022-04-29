/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.Persistencia.util.UtilSBPersistenciaReflexao;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreCriptrografia;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanComStatus;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanStatus;
import java.lang.reflect.Field;
import java.util.Date;
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
import org.coletivojava.fw.api.tratamentoErros.FabErro;

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
        FabTipoAtributoObjeto campoSenha = null;

        if (!(pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.SENHA) || pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.SENHA_SEGURANCA_MAXIMA))) {
            return;
        }

        if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.SENHA)) {
            campoSenha = FabTipoAtributoObjeto.SENHA;
        }
        if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.SENHA_SEGURANCA_MAXIMA)) {
            campoSenha = FabTipoAtributoObjeto.SENHA_SEGURANCA_MAXIMA;
        }

        try {
            Field cp = pEntidade.getCampoReflexaoByAnotacao(campoSenha);
            cp.setAccessible(true);
            String senha = (String) cp.get(pEntidade);
            if (senha != null && senha.length() < 60) {

                String senhaCriptografada = UtilSBCoreCriptrografia.criptografarTextoSimetricoSaltAleatorio(senha);

                cp.set(pEntidade, senhaCriptografada);
            }

        } catch (IllegalAccessException | IllegalArgumentException | SecurityException ex) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro adicionando criptografia na senha", ex);
        }

    }

    @PrePersist
    public void acaoAntesDePersistir(ItfBeanSimples pEntidade) {
        System.out.println("Ação Automatica Antes de Persistir");
        protegerSenhas(pEntidade);
        if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.REG_DATAINSERCAO)) {
            if (SBCore.getServicoSessao().getSessaoAtual().isIdentificado()) {
                pEntidade.getCampoInstanciadoByNomeOuAnotacao(FabTipoAtributoObjeto.REG_DATAINSERCAO.name()).setValor(new Date());
            }
        }
        if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.REG_USUARIO_INSERCAO)) {
            if (SBCore.getServicoSessao().getSessaoAtual().isIdentificado()) {
                if (UtilSBPersistenciaReflexao.isObjetoPersistivel(SBCore.getUsuarioLogado())) {
                    pEntidade.getCampoInstanciadoByNomeOuAnotacao(FabTipoAtributoObjeto.REG_USUARIO_INSERCAO.name()).setValor(SBCore.getUsuarioLogado());
                }
            }
        }

    }

    @PreUpdate
    public void acaoAntesDeAtualizar(ItfBeanSimples pEntidade) {
        protegerSenhas(pEntidade);

        if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.EMAIL)) {
            String valorEmail = (String) pEntidade.getCampoInstanciadoByAnotacao(FabTipoAtributoObjeto.EMAIL).getValor();
            valorEmail = valorEmail.replace(" ", "");
            valorEmail = valorEmail.toLowerCase();
            pEntidade.getCampoInstanciadoByAnotacao(FabTipoAtributoObjeto.EMAIL).setValor(valorEmail);
        }

        if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.REG_DATAALTERACAO)) {
            if (SBCore.getServicoSessao().getSessaoAtual().isIdentificado()) {
                pEntidade.getCampoInstanciadoByNomeOuAnotacao(FabTipoAtributoObjeto.REG_DATAALTERACAO.name()).setValor(new Date());
            }

        }
        if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.REG_USUARIO_ALTERACAO)) {
            if (SBCore.getServicoSessao().getSessaoAtual().isIdentificado()) {
                if (UtilSBPersistenciaReflexao.isObjetoPersistivel(SBCore.getUsuarioLogado())) {
                    pEntidade.getCampoInstanciadoByNomeOuAnotacao(FabTipoAtributoObjeto.REG_USUARIO_ALTERACAO.name()).setValor(SBCore.getUsuarioLogado());
                }
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

}
