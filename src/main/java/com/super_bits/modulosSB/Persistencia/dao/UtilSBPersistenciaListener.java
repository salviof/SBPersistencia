/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.Persistencia.util.UtilSBPersistenciaReflexao;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreCriptrografia;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanComStatus;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanStatus;
import java.lang.reflect.Field;
import java.util.Date;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author salvio
 */
public class UtilSBPersistenciaListener {

    public static void acaoPadraoAposCarregarEntidade(ItfBeanSimples pEntidade) {

        try {

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Falha executando ações padrão antes de Atualizar a entidade " + pEntidade.getClass().getSimpleName(), t);
        }

    }

    public static void acaoPadraoAntesDeAtualizar(ItfBeanSimples pEntidade) {

        try {
            protegerSenhas(pEntidade);

            if (pEntidade.isTemCampoAnotado(FabTipoAtributoObjeto.EMAIL)) {
                String valorEmail = (String) pEntidade.getCampoInstanciadoByAnotacao(FabTipoAtributoObjeto.EMAIL).getValor();
                if (valorEmail != null) {
                    valorEmail = valorEmail.replace(" ", "");
                    valorEmail = valorEmail.toLowerCase();
                    pEntidade.getCampoInstanciadoByAnotacao(FabTipoAtributoObjeto.EMAIL).setValor(valorEmail);
                }
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
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Falha executando ações padrão antes de Atualizar a entidade " + pEntidade.getClass().getSimpleName(), t);
        }

    }

    public static void acaoPadraoDepoisDeAtualizar(ItfBeanSimples pEntidade) {

        try {
            if (pEntidade instanceof ItfBeanComStatus) {
                //disparar eventos de alteração de Status, como eventos de comunicação
            }
        } catch (Throwable t) {

        }
    }

    public static void acaoPadraoAntesPersistirNovoRegistro(ItfBeanSimples pEntidade) {

        try {
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
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Falha executando ações padrão antes de Persistir a entidade " + pEntidade.getClass().getSimpleName(), t);
        }

    }

    public static void acaoPadraoAposPersistirNovo(ItfBeanSimples pEntidade) {

    }

    public static void protegerSenhas(ItfBeanSimples pEntidade) {
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

}
