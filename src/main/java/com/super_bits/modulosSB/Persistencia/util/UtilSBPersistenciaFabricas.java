/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.util;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.Persistencia.geradorDeId.GERADOR_ID_ESTRATEGIA_CONHECIDA;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreListasObjeto;
import com.super_bits.modulosSB.SBCore.modulos.Mensagens.FabMensagens;
import com.super_bits.modulosSB.SBCore.modulos.Mensagens.ItfMensagem;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabrica;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimplesSomenteLeitura;
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

    private static List<ItemFabricaObjeto> listaRegistros(Class pFabrica) {
        List<ItemFabricaObjeto> lista = new ArrayList<>();
        for (Object obj : pFabrica.getEnumConstants()) {
            lista.add(new ItemFabricaObjeto((ComoFabrica) obj));

        }
        return lista;

    }

    private static void listaOrdenarPorID(List<ItemFabricaObjeto> pLista) {
        UtilSBCoreListasObjeto.ordernarPorCampo(pLista, "idEntidade");

    }

    private static void listaOrdenarOrdem(List<ItemFabricaObjeto> pLista) {
        UtilSBCoreListasObjeto.ordernarPorCampo(pLista, "ordemEnum");

    }

    public static void persistirRegistrosDaFabricaSeNaoExistir(Class pFabrica, EntityManager pEM, TipoOrdemGravacao pTipoOrdem) {
        if (pFabrica.getEnumConstants() == null) {
            ItfMensagem msg = FabMensagens.ERRO.getMsgSistema("Nenum Enum foi encontrado para persistir nesta fabrica" + pFabrica.getSimpleName());
            SBCore.getServicoMensagens().enviaMensagem(msg);
            return;
        }
        List<ItemFabricaObjeto> lista = listaRegistros(pFabrica);
        switch (pTipoOrdem) {
            case ORDERNAR_POR_ID:
                listaOrdenarPorID(lista);
                for (ItemFabricaObjeto item : lista) {

                    persistir(item.getObjeto(), pEM, item.getFabrica());

                }
                break;
            case ORDERNAR_POR_ORDEM_DE_DECLARCAO:
                listaOrdenarOrdem(lista);
                for (ItemFabricaObjeto item : lista) {
                    try {

                        persistir(item.getObjeto(), pEM, item.getFabrica());

                    } catch (Throwable t) {
                        throw new UnsupportedOperationException("Erro Persistindo registro de Fabrica de objetos:" + pFabrica.getName() + "em->" + pFabrica.getSimpleName() + "->" + item, t);
                    }
                }

                break;
            default:
                throw new AssertionError(pTipoOrdem.name());

        }
    }

    /**
     *
     * Realiza a persistencia de todos os registros obtidos com getRegistro da
     * fábrica Ordena por id, e persiste
     *
     * @param pFabrica Enum que extende ComoFabrica e retorna entidades
     * persistiveis no getRegistro
     * @param pEM Gerenciamento de sessão
     * @param pTipoOrdem Especifica a ordem da gravação (podendo ser pelo id do
     * registro, ou pela ordem de declaração do Enum)
     */
    public static void persistirRegistrosDaFabrica(Class pFabrica, EntityManager pEM, TipoOrdemGravacao pTipoOrdem) {
        System.out.println("___________________ PERISTINDO REGISTROS DA FABRICA " + pFabrica.getSimpleName() + " _______________");
        if (pFabrica.getEnumConstants() == null) {
            ItfMensagem msg = FabMensagens.ERRO.getMsgSistema("Nenum Enum foi encontrado para persistir nesta fabrica" + pFabrica.getSimpleName());
            SBCore.getServicoMensagens().enviaMensagem(msg);
            return;
        }
        List<ItemFabricaObjeto> lista = listaRegistros(pFabrica);
        switch (pTipoOrdem) {
            case ORDERNAR_POR_ID:
                listaOrdenarPorID(lista);
                for (ItemFabricaObjeto itemFAbrica : lista) {

                    persistir(itemFAbrica.getObjeto(), pEM, itemFAbrica.getFabrica());

                }
                break;
            case ORDERNAR_POR_ORDEM_DE_DECLARCAO:
                listaOrdenarOrdem(lista);
                for (ItemFabricaObjeto item : lista) {
                    try {

                        persistir((ComoEntidadeSimplesSomenteLeitura) item.getObjeto(), pEM, item.getFabrica());

                    } catch (Throwable t) {
                        throw new UnsupportedOperationException("Erro Persistindo registro de Fabrica de objetos:" + pFabrica.getName() + "em->" + pFabrica.getSimpleName() + "->" + item, t);
                    }
                }

                break;
            default:
                throw new AssertionError(pTipoOrdem.name());

        }

    }

    private static void persistir(ComoEntidadeSimplesSomenteLeitura entidade, EntityManager pEm, ComoFabrica pFabrica) {
        Object registroGerado = null;

        try {
            // UtilSBPersistencia.iniciarTransacao(pEm);
            if (entidade.getId() != null && entidade.getId() != 0) {
                registroGerado = UtilSBPersistencia.getRegistroByID((Class<? extends ComoEntidadeSimples>) entidade.getClass(), entidade.getId(), pEm);
            }

            if (registroGerado == null) {
                boolean persistirComMerge = false;
                if (UtilSBPersistenciaReflexao.possuiIdeAutogerado(entidade.getClass())) {

                    if (((ComoEntidadeSimples) entidade).getId() != null && ((ComoEntidadeSimples) entidade).getId() >= 1000
                            && !UtilSBPersistenciaReflexao.possuiIdeAutogeradoDessasEstrategias(entidade.getClass(), GERADOR_ID_ESTRATEGIA_CONHECIDA.GERADOR_ID_NOME_UNICO,
                                    GERADOR_ID_ESTRATEGIA_CONHECIDA.GERADOR_ID_BAIRRO, GERADOR_ID_ESTRATEGIA_CONHECIDA.GERADOR_ID_CIDADE, GERADOR_ID_ESTRATEGIA_CONHECIDA.OBJETO_VINCULADO_ENUM)) {
                        // id gerido pelo usuário
                        System.out.println("Persistindo" + entidade.toString());
                        if (entidade.getId() != null) {
                            persistirComMerge = true;
                        }
                    } else {
                        //id autoincremental deixa persistir nulo, e confia na sequencia lógica de declaração das fabricas
                        ((ComoEntidadeSimples) entidade).setId(null);
                    }

                }
                if (persistirComMerge) {
                    registroGerado = UtilSBPersistencia.mergeRegistro(entidade, pEm);
                } else {
                    registroGerado = UtilSBPersistencia.persistirRegistro(entidade, pEm);
                }

            } else {
                if (!pFabrica.isPermitidoAlterarObjeto()) {
                    registroGerado = UtilSBPersistencia.mergeRegistro(entidade, pEm);
                } else {

                }
            }
            if (registroGerado == null) {
                throw new UnsupportedOperationException("A entidade não pode ser armazenada " + entidade.toString() + " - " + entidade.getNome() + "" + pFabrica);
            }
            pEm.refresh(registroGerado);
            ComoEntidadeSimples objetoPersistido = (ComoEntidadeSimples) registroGerado;
            System.out.println("Gerado Registro: " + objetoPersistido.getClass().getSimpleName() + " " + objetoPersistido.getId() + " - " + objetoPersistido.getNome());
            //  UtilSBPersistencia.finalizarTransacao(pEm);
        } catch (Throwable t) {
            String textoEntidade = "Nulo";
            if (entidade != null) {
                try {
                    ComoEntidadeSimplesSomenteLeitura bsimples = (ComoEntidadeSimplesSomenteLeitura) entidade;
                    textoEntidade = entidade.getClass().getSimpleName() + "-" + " " + bsimples.getId() + " - " + bsimples.getNome();
                } catch (Throwable CastExThrowable) {

                }
            }

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Iniciando Fabricas de registro iniciais da Fabrica:" + pFabrica.getClass().getSimpleName() + "." + pFabrica.toString() + " salvando entidade:" + textoEntidade, t);
            throw new UnsupportedOperationException("Erro Iniciando Fabricas de registro iniciais da Fabrica:" + pFabrica.getClass().getSimpleName() + " salvando entidade:" + textoEntidade);

        }
    }

}
