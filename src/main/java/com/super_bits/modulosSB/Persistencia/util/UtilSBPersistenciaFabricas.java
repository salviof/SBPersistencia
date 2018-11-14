/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.util;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreItens;
import com.super_bits.modulosSB.SBCore.modulos.Mensagens.FabMensagens;
import com.super_bits.modulosSB.SBCore.modulos.Mensagens.ItfMensagem;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ItfFabrica;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimplesSomenteLeitura;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author sfurbino
 */
public abstract class UtilSBPersistenciaFabricas {

    public enum TipoOrdemGravacao {
        ORDERNAR_POR_ID, ORDERNAR_POR_ORDEM_DE_DECLARCAO
    }

    private static List<ItfBeanSimples> listaRegistros(Class pFabrica) {
        List<ItfBeanSimples> lista = new ArrayList<>();

        for (Object obj : pFabrica.getEnumConstants()) {
            ItfBeanSimples registro = (ItfBeanSimples) ((ItfFabrica) obj).getRegistro();
            lista.add(registro);

        }
        return lista;

    }

    private static List<ItfBeanSimples> listaOrdenadaPorID(List<ItfBeanSimples> pLista) {
        return UtilSBCoreItens.ordernarPorId(pLista);

    }

    private static List<ItfBeanSimples> listaOrdenadaPorOrdemDEclaracao(Class pFabrica) {
        return listaRegistros(pFabrica);
    }

    /**
     *
     * Realiza a persistencia de todos os registros obtidos com getRegistro da
     * fábrica Ordena por id, e persiste
     *
     * @param pFabrica Enum que extende ItfFabrica e retorna entidades
     * persistiveis no getRegistro
     * @param pEM Gerenciamento de sessão
     * @param pTipoOrdem Especifica a ordem da gravação (podendo ser pelo id do
     * registro, ou pela ordem de declaração do Enum)
     */
    public static void persistirRegistrosDaFabrica(Class pFabrica, EntityManager pEM, TipoOrdemGravacao pTipoOrdem) {
        System.out.println("___________________ PERISTINDO REGISTROS DA FABRICA " + pFabrica.getSimpleName() + " _______________");
        if (pFabrica.getEnumConstants() == null) {
            ItfMensagem msg = FabMensagens.ERRO.getMsgSistema("Nenum Enum foi encontrado para persistir nesta fabrica" + pFabrica.getSimpleName());
            SBCore.getCentralDeMensagens().enviaMensagem(msg);
            return;
        }

        switch (pTipoOrdem) {
            case ORDERNAR_POR_ID:
                for (Object entidade : listaOrdenadaPorID(listaRegistros(pFabrica))) {
                    if (pEM.find(entidade.getClass(), ((ItfBeanSimples) entidade).getId()) == null) {
                        persistir(entidade, pEM, pFabrica);
                    }

                }
                break;
            case ORDERNAR_POR_ORDEM_DE_DECLARCAO:
                for (Object entidade : listaRegistros(pFabrica)) {
                    try {

                        persistir(entidade, pEM, pFabrica);

                    } catch (Throwable t) {
                        throw new UnsupportedOperationException("Erro Persistindo registro de Fabrica de objetos:" + pFabrica.getName() + "em->" + pFabrica.getSimpleName() + "->" + entidade, t);
                    }
                }

                break;
            default:
                throw new AssertionError(pTipoOrdem.name());

        }

    }

    private static void persistir(Object entidade, EntityManager pEm, Class pFabrica) {
        Object registroGerado = null;

        try {
            registroGerado = UtilSBPersistencia.mergeRegistro(entidade, pEm);
            if (registroGerado == null) {
                throw new UnsupportedOperationException("A entidade não pode ser armazenada");
            }
            pEm.refresh(registroGerado);
            ItfBeanSimples objetoPersistido = (ItfBeanSimples) registroGerado;
            System.out.println("Gerado Registro: " + objetoPersistido.getClass().getSimpleName() + " " + objetoPersistido.getId() + " - " + objetoPersistido.getNome());

        } catch (Throwable t) {
            String textoEntidade = "Nulo";
            if (entidade != null) {
                try {
                    ItfBeanSimplesSomenteLeitura bsimples = (ItfBeanSimplesSomenteLeitura) entidade;
                    textoEntidade = entidade.getClass().getSimpleName() + "-" + " " + bsimples.getId() + " - " + bsimples.getNome();
                } catch (Throwable CastExThrowable) {

                }
            }

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Iniciando Fabricas de registro iniciais da Fabrica:" + pFabrica.getSimpleName() + " salvando entidade:" + textoEntidade, t);
            throw new UnsupportedOperationException("Erro Iniciando Fabricas de registro iniciais da Fabrica:" + pFabrica.getClass().getSimpleName() + " salvando entidade:" + textoEntidade);

        }
    }

}
