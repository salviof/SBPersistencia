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
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCReflexao;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ComoAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Controller.UtilSBController;
import com.super_bits.modulosSB.SBCore.modulos.fonteDados.FabTipoSelecaoRegistro;
import com.super_bits.modulosSB.SBCore.modulos.fonteDados.ItfTokenAcessoDados;
import java.util.List;
import com.super_bits.modulosSB.SBCore.modulos.fonteDados.TokenAcessoDados;
import com.super_bits.modulosSB.SBCore.modulos.testes.UtilCRCTestes;
import javax.persistence.EntityManager;
import com.super_bits.modulosSB.SBCore.modulos.centralDados.ItfServicoRepositorioEntidades;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoEntidadeSimples;
import java.util.HashMap;
import java.util.Map;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.entidadeEscuta.ComoListenerPersistenciaEntidade;

/**
 *
 * @author SalvioF
 */
public class CentralDadosJPAPadrao implements ItfServicoRepositorioEntidades {

    private static Map<Class, List<ComoListenerPersistenciaEntidade>> operacoesEntidade = new HashMap<>();
    private static boolean listenersIniciado = false;

    @Override
    public List<?> selecaoRegistros(ItfTokenAcessoDados pEM, String pSQL, String pPQL, Integer maximo, Class tipoRegisto, FabTipoSelecaoRegistro pTipoSelecao, Object... parametros) {
        if (pSQL == null) {
            return UtilSBPersistencia.getListaRegistrosByHQL(pPQL, maximo, parametros);
        } else {
            return UtilSBPersistencia.getListaRegistrosBySQL(null, pSQL, maximo, parametros);
        }
    }

    @Override
    public <T extends ComoEntidadeSimples> T getEntidadeByID(ItfTokenAcessoDados pToken, Class<T> pClasse, Long id) {
        if (pToken == null) {
            return UtilSBPersistencia.getRegistroByID((Class<T>) pClasse, id);
        } else {

            return (T) UtilSBPersistencia.getRegistroByID(pClasse, id, pToken.getEntitiManager());
        }

    }

    @Override
    public long getQuantidadeRegistros(ItfTokenAcessoDados pToken, Class pClasseObjeto) {
        return (long) UtilSBPersistencia.getQuantidadeRegistrosNaTabela(pClasseObjeto);
    }

    @Override
    public ItfTokenAcessoDados getAcessoDadosDoContexto() {
        if (SBCore.isEmModoDesenvolvimento()) {
            ComoAcaoDoSistema acao = UtilSBController.getAcaoDoContexto();

            if (acao != null) {
                ItfRespostaComExecucaoDeRegraDeNegocio resp = (ItfRespostaComExecucaoDeRegraDeNegocio) MapaControllerEmExecucao.getRespostaDoContexto();
                if (resp != null) {
                    return new TokenAcessoDados(resp.getEm());
                }
            } else {

                return new TokenAcessoDados(UtilCRCTestes.emContextoTEste);

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

    @Override
    public List<ComoListenerPersistenciaEntidade> getListenerDeEntidade(Class<? extends ComoEntidadeSimples> classe) {
        if (!listenersIniciado) {
            //UtilCRCReflexao.getClassesComEstaAnotacao(classe, classe)
        }
        return null;
    }

}
