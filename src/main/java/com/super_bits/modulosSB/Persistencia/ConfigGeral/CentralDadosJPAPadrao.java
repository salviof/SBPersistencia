/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.ConfigGeral;

import com.super_bits.modulosSB.Persistencia.dao.ItfRespostaComExecucaoDeRegraDeNegocio;
import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.MapaControllerEmExecucao;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ItfAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.UtilSBController;
import com.super_bits.modulosSB.SBCore.modulos.fonteDados.FabTipoSelecaoRegistro;
import com.super_bits.modulosSB.SBCore.modulos.fonteDados.ItfTokenAcessoDados;
import java.util.List;
import com.super_bits.modulosSB.SBCore.modulos.centralDados.ItfCentralDados;
import com.super_bits.modulosSB.SBCore.modulos.fonteDados.TokenAcessoDados;
import com.super_bits.modulosSB.SBCore.modulos.testes.UtilSBCoreTestes;
import javax.persistence.EntityManager;

/**
 *
 * @author SalvioF
 */
public class CentralDadosJPAPadrao implements ItfCentralDados {

    @Override
    public List<?> selecaoRegistros(ItfTokenAcessoDados pEM, String pSQL, String pPQL, Integer maximo, Class tipoRegisto, FabTipoSelecaoRegistro pTipoSelecao, Object... parametros) {
        if (pSQL == null) {
            return UtilSBPersistencia.getListaRegistrosByHQL(pPQL, maximo, parametros);
        } else {
            return UtilSBPersistencia.getListaRegistrosBySQL(null, pSQL, maximo, parametros);
        }
    }

    @Override
    public <T> T getRegistroByID(ItfTokenAcessoDados pToken, Class<T> pClasse, int id) {
        return (T) UtilSBPersistencia.getRegistroByID(pClasse, id);

    }

    @Override
    public long getQuantidadeRegistros(ItfTokenAcessoDados pToken, Class pClasseObjeto) {
        return (long) UtilSBPersistencia.getQuantidadeRegistrosNaTabela(pClasseObjeto);
    }

    @Override
    public ItfTokenAcessoDados getAcessoDadosDoContexto() {
        if (SBCore.isEmModoDesenvolvimento()) {
            ItfAcaoDoSistema acao = UtilSBController.getAcaoDoContexto();

            if (acao != null) {
                ItfRespostaComExecucaoDeRegraDeNegocio resp = (ItfRespostaComExecucaoDeRegraDeNegocio) MapaControllerEmExecucao.getRespostaDoContexto();
                if (resp != null) {
                    return new TokenAcessoDados(resp.getEm());
                }
            } else {

                return new TokenAcessoDados(UtilSBCoreTestes.emContextoTEste);

            }
        } else {
            throw new UnsupportedOperationException("esta central de dados foi homlogada apenas para o modo Testes");

        }
        return null;

    }

    @Override
    public EntityManager gerarNovoEntityManagerPadrao() {
        return UtilSBPersistencia.getEntyManagerPadraoNovo();
    }

}
