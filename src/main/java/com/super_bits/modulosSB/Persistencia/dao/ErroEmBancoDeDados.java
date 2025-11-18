/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 *
 *
 *
 * @author desenvolvedor
 */
public class ErroEmBancoDeDados extends Throwable {

    private final String mensagemProgrador;
    private final String mensagemUsuario;
    private FabTipoErroBancoDeDados tipoErro;
    private final Map<String, String> mapaTrechoFrase = new HashMap();
    private ComoEntidadeSimples entidade;

    public ErroEmBancoDeDados(Throwable t, ComoEntidadeSimples entidade) {

        super(t);
        try {

            tipoErro = FabTipoErroBancoDeDados.getTipoErroViaMensagem(t);
            if (tipoErro != null) {

                mensagemProgrador = tipoErro.getMensagemProgramador(t, entidade);
                mensagemUsuario = tipoErro.getMensagemUsuario(t, entidade);
            } else {
                Throwable causa = ExceptionUtils.getRootCause(t);
                String causaStr = "";
                if (causa != null) {
                    causaStr = causa.getMessage() + "\n";
                }

                mensagemProgrador = t.getMessage() + "\n" + causaStr;
                mensagemUsuario = "O correu um erro inesperado ao tentar salvar a informação";

            }
        } catch (Throwable tt) {

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro tentando interprentar mensagem de erro de JPA", tt);
            throw new UnsupportedOperationException(t);
        }

    }

    public ErroEmBancoDeDados(FabTipoErroBancoDeDados pTipoErro, String pMensagem) {
        switch (pTipoErro) {
            case ERRO_DE_CONEXAO:
                mensagemUsuario = "O Banco de Dados do sistema não está acessível";
                break;
            case INDEFINIDO:
                mensagemUsuario = "O Banco de dados não está respondendo conforme o esperado";
                break;
            default:
                throw new UnsupportedOperationException("Utilize o constructor com Throable para declarar este tipo de erro");

        }
        mensagemProgrador = pMensagem;
    }

    public FabTipoErroBancoDeDados getTipoErro() {
        return tipoErro;
    }

    public void setTipoErro(FabTipoErroBancoDeDados tipoErro) {
        this.tipoErro = tipoErro;
    }

    public String getMensagemProgrador() {
        return mensagemProgrador;
    }

    public String getMensagemUsuario() {
        return mensagemUsuario;
    }

}
