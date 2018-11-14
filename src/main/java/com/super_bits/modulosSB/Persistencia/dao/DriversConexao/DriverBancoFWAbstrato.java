/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao.DriversConexao;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import static com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia.getNovoEM;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import javax.persistence.EntityManager;

import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 */
public abstract class DriverBancoFWAbstrato implements ItfDriverBanco {

    /**
     * Define qual EM será utilizada de acordo com os parametros enviados
     *
     * @param pEmEnviada (EntityManager que será retornado caso não seja nulo)
     * @param pNomeEntityManager (Nome do PersistenceUnit, caso seja diferente
     * do PersistenceUnit padrão)
     * @return O novo entityManager, ou o EM enviado
     */
    protected EntityManager defineEM(EntityManager pEmEnviada, String pNomeEntityManager) {

        try {
            if (pEmEnviada != null) {
                return pEmEnviada;
            } else {
                return getNovoEM(pNomeEntityManager);
            }

        } catch (Exception e) {

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro selecionando o tipo de entidade a ser retornada", e);
            return null;
        }
    }

    public String getJPSQL(UtilSBPersistencia.TIPO_SELECAO_REGISTROS tipoSelecao, Class tipoRegisto, Object[] parametros) {
        try {
            switch (tipoSelecao) {
                case JPQL:
                    break;
                case SQL:
                    break;
                case LIKENOME:

                    ItfBeanSimples registro = (ItfBeanSimples) tipoRegisto.newInstance();
                    String campoNomeCurto = registro.getNomeCampo(FabTipoAtributoObjeto.AAA_NOME);
                    String parametro = (String) parametros[0];
                    return "from " + tipoRegisto.getSimpleName() + " where "
                            + campoNomeCurto + " like '%" + parametro + "%' order by " + campoNomeCurto;

                case TODOS:
                    ItfBeanSimples registroTodos = (ItfBeanSimples) tipoRegisto.newInstance();
                    String campoNomeCurtoTodos = registroTodos.getNomeCampo(FabTipoAtributoObjeto.AAA_NOME);
                    return "from " + tipoRegisto.getSimpleName() + " order by " + campoNomeCurtoTodos;
                case NAMED_QUERY:
                    break;
                case SBNQ:
                    break;
                default:
                    throw new AssertionError(tipoSelecao.name());

            }
            return null;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "", t);
            return null;
        }

    }

}
