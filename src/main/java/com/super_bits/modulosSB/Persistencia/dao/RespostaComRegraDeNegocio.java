/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.UtilGeral.MapaControllerEmExecucao;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCStringValidador;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfResposta;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfRespostaAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.ItfRespostaComGestaoDeEntityManager;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ComoAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.permissoes.ItfAcaoFormulario;
import com.super_bits.modulosSB.SBCore.modulos.Mensagens.ItfMensagem;
import com.super_bits.modulosSB.SBCore.modulos.TratamentoDeErros.ErroRegraDeNegocio;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author desenvolvedor
 */
public abstract class RespostaComRegraDeNegocio implements ItfRespostaComExecucaoDeRegraDeNegocio {

    private final ItfRespostaAcaoDoSistema resposta;
    private final ItfExecucaoRegraDeNegocio execucaoRegraDENegocio;

    public RespostaComRegraDeNegocio(ItfRespostaAcaoDoSistema resposta, ItfExecucaoRegraDeNegocio rexecucaoRegraDENegocio) {

        this.resposta = resposta;
        this.execucaoRegraDENegocio = rexecucaoRegraDENegocio;

    }

    @Override
    public EntityManager getEm() {
        return execucaoRegraDENegocio.getEm();
    }

    @Override
    public void executarAcao() throws ErroEmBancoDeDados, ErroRegraDeNegocio {
        execucaoRegraDENegocio.executarAcao();
    }

    @Override
    public void executarAcoesFinais() throws ErroEmBancoDeDados {
        if (isSucesso()) {
            execucaoRegraDENegocio.executarAcoesFinais();
        } else {
            execucaoRegraDENegocio.reverterAcoesFinais();
        }
    }

    @Override
    public void reverterAcoesFinais() throws ErroEmBancoDeDados {
        try {
            execucaoRegraDENegocio.reverterAcoesFinais();
        } finally {
            MapaControllerEmExecucao.removerResposta(this);
        }
    }

    @Override
    public void executarAcoesIniciais() throws ErroEmBancoDeDados {
        try {
            execucaoRegraDENegocio.executarAcoesIniciais();
        } finally {
            MapaControllerEmExecucao.registrarRegra(this);
        }
    }

    @Override
    public ItfResposta.Resultado getResultado() {
        return resposta.getResultado();
    }

    @Override
    public List<ItfMensagem> getMensagens() {
        return resposta.getMensagens();
    }

    @Override
    public Class getTipoRetorno() {
        return resposta.getTipoRetorno();
    }

    @Override
    public Object getRetorno() {
        return resposta.getRetorno();
    }

    @Override
    public ItfRespostaAcaoDoSistema setRetornoDisparaERetorna(Object pObjetoResultante) {
        return resposta.setRetornoDisparaERetorna(pObjetoResultante);
    }

    @Override
    public ItfRespostaAcaoDoSistema setRetorno(Object pObjetoResultante) {
        return resposta.setRetorno(pObjetoResultante);
    }

    @Override
    public ItfRespostaAcaoDoSistema addMensagemAvisoDisparaERetorna(String pMensagem) {
        return resposta.addMensagemErroDisparaERetorna(pMensagem);
    }

    @Override
    public ItfRespostaAcaoDoSistema addMensagemDisparaERetorna(ItfMensagem pMensagem) {
        return resposta.addMensagemDisparaERetorna(pMensagem);
    }

    @Override
    public ItfRespostaAcaoDoSistema addMensagemErroDisparaERetorna(String pMensagem) {
        return resposta.addMensagemErroDisparaERetorna(pMensagem);
    }

    @Override
    public ItfRespostaAcaoDoSistema addMensagemAlertaDisparaERetorna(String pMensagem) {
        return resposta.addMensagemAlertaDisparaERetorna(pMensagem);
    }

    @Override
    public ItfRespostaAcaoDoSistema dispararMensagens() {
        return resposta.dispararMensagens();
    }

    @Override
    public ItfRespostaAcaoDoSistema addMensagem(ItfMensagem pMensagem) {
        return resposta.addMensagem(pMensagem);
    }

    @Override
    public ItfRespostaAcaoDoSistema addAlerta(String Pmensagem) {
        return resposta.addAlerta(Pmensagem);
    }

    @Override
    public final ItfRespostaAcaoDoSistema addAviso(String Pmensagem) {
        return resposta.addAviso(Pmensagem);
    }

    @Override
    public final ItfRespostaAcaoDoSistema addErro(String Pmensagem) {
        return resposta.addErro(Pmensagem);
    }

    @Override
    public ItfRespostaAcaoDoSistema setProximoFormulario(ItfAcaoFormulario pFormulario) {
        return resposta.setProximoFormulario(pFormulario);
    }

    @Override
    public ItfAcaoFormulario getAcaoProximoFormulario() {
        return resposta.getAcaoProximoFormulario();
    }

    @Override
    public boolean isTemProximoFormulario() {
        return resposta.isTemProximoFormulario();
    }

    @Override
    public final boolean isSucesso() {
        return resposta.isSucesso();
    }

    @Override
    public final ComoAcaoDoSistema getAcaoVinculada() {
        return resposta.getAcaoVinculada();
    }

    @Override
    public ItfRespostaAcaoDoSistema getResposta() {
        return resposta;
    }

    @Override
    public ItfExecucaoRegraDeNegocio getExecucao() {
        return execucaoRegraDENegocio;
    }

    @Override
    public boolean isTemAlerta() {
        return resposta.isTemAlerta();
    }

    @Override
    public void setUrlDestino(String pDestino) {
        resposta.setUrlDestino(pDestino);
    }

    @Override
    public void setUrlDestinoFalha(String pDestinoFalha) {
        resposta.setUrlDestino(pDestinoFalha);
    }

    @Override
    public void setUrlDestinoSucesso(String pDestinoSucesso) {
        resposta.setUrlDestinoSucesso(pDestinoSucesso);
    }

    @Override
    public String getUrlDestino() {
        return resposta.getUrlDestino();
    }

    @Override
    public ItfRespostaComGestaoDeEntityManager getComoRepostaGestaoEM() {
        return this;
    }

    @Override
    public boolean isTemUrlDestino() {
        return UtilCRCStringValidador.isNAO_NuloNemBranco(getUrlDestino());
    }

}
