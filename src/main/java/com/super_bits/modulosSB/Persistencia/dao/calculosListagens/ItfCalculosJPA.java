/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao.calculosListagens;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreReflexao;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.calculos.InfoCalculo;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.calculos.ItfCalculos;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaDeEntidade;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.LigacaoMuitosParaUm;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author SalvioF
 */
public interface ItfCalculosJPA extends ItfCalculos {

    public default Object getCalculoPorCriteriaAPI(Object[] pParametros, FabTipoFiltroCalculo pTipo) {

        EntityManager em = null;
        try {

            Field campo = this.getClass().getDeclaredField(this.toString());
            InfoCalculo infocalc = campo.getAnnotation(InfoCalculo.class);
            Class classePrincipal = null;
            boolean calculoExterno = false;
            if (infocalc != null) {
                if (!infocalc.classePrincipal().equals(void.class)) {
                    calculoExterno = true;
                    classePrincipal = infocalc.classePrincipal();
                } else {
                    classePrincipal = pParametros[0].getClass();
                }
            } else {
                classePrincipal = pParametros[0].getClass();
            }
            em = UtilSBPersistencia.getNovoEM();
            CriteriaBuilder criterioBuilder = em.getCriteriaBuilder();

            CriteriaQuery criterioQuery = null;
            Root entidadePrincipal = null;

            criterioQuery = pTipo.gerarCriteriaQuery(classePrincipal, criterioBuilder);
            entidadePrincipal = (Root) criterioQuery.getRoots().iterator().next();

            if (criterioQuery == null) {
                throw new UnsupportedOperationException("Impossivel determinar o tipo de pesquisa padrão a ser realizada");
            }
            Map<String, Integer> valoresParametro = new HashMap<>();
            List<Predicate> condicoes = new ArrayList<>();
            int i = 0;
            for (Object parametro : pParametros) {
                ItfBeanSimples beanParametro = (ItfBeanSimples) parametro;
                Class classePesquisa = null;
                if (i == 0) {
                    if (calculoExterno) {
                        classePesquisa = beanParametro.getClass();
                    }

                } else {
                    classePesquisa = parametro.getClass();
                }
                if (classePesquisa != null) {
                    EstruturaDeEntidade est = MapaObjetosProjetoAtual.getEstruturaObjeto(classePrincipal);
                    for (LigacaoMuitosParaUm relacao : est.getMuitosParaUm()) {
                        if (UtilSBCoreReflexao.isClasseIgualOuExetende(beanParametro.getClass(), relacao.getClasseObjetoVinculado())) {

                            String nomecampoPesquisa = relacao.getNomeDeclarado();
                            ParameterExpression<Integer> prQuery = criterioBuilder.parameter(Integer.class, nomecampoPesquisa);
                            valoresParametro.put(nomecampoPesquisa, beanParametro.getId());
                            Predicate condicao = criterioBuilder.equal(entidadePrincipal.get(nomecampoPesquisa).get("id"), prQuery);
                            condicoes.add(condicao);

                        }
                    }

                }
                i++;
            }

            if (!condicoes.isEmpty()) {
                criterioQuery.where(condicoes.toArray(new Predicate[]{}));

            }
            switch (pTipo) {
                case SOMA_QTD:

                    break;
                case REGISTRO_UNICO:
                    criterioQuery.orderBy(criterioBuilder.asc(entidadePrincipal.get("id")));
                    break;
                case MAIOR:
                    break;
                case MENOR:
                    break;
                default:
                    throw new AssertionError(pTipo.name());

            }
            Query qr = em.createQuery(criterioQuery);
            for (String prString : valoresParametro.keySet()) {
                qr.setParameter(prString, valoresParametro.get(prString));
            }
            switch (pTipo) {
                case SOMA_QTD:

                    return qr.getSingleResult();

                case REGISTRO_UNICO:

                    qr.setMaxResults(1);
                    return qr.getSingleResult();

                case MAIOR:
                    break;
                case MENOR:
                    break;
                default:
                    throw new AssertionError(pTipo.name());

            }

        } catch (NoResultException nenhumRegistroEncontrado) {
            return null;
        } catch (Throwable t) {

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro obtendo valor calculado para" + this.toString(), t);
            return null;
        } finally {

            UtilSBPersistencia.fecharEM(em);

        }
        return null;

    }

    @Override
    public default Long getSomaPadrao(Object... pParametros) {

        return (Long) getCalculoPorCriteriaAPI(pParametros, FabTipoFiltroCalculo.SOMA_QTD);

    }

}
