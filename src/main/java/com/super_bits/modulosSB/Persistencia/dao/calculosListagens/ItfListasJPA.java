/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao.calculosListagens;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreReflexao;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.listas.InfoLista;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.listas.ItfListas;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaDeEntidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.objetos.estrutura.ItfLigacaoMuitosParaUm;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
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
public interface ItfListasJPA extends ItfListas {

    public default List getListagemPorCriteriaAPI(EntityManager pEm, Object[] pParametros) {

        EntityManager em = pEm;
        try {

            Field campo = this.getClass().getDeclaredField(this.toString());
            InfoLista infoLista = campo.getAnnotation(InfoLista.class);
            Class classePrincipal = null;
            boolean calculoExterno = false;
            if (infoLista != null) {
                if (!infoLista.tipoObjetoListado().equals(void.class)) {
                    calculoExterno = true;
                    classePrincipal = infoLista.tipoObjetoListado();
                } else {
                    classePrincipal = pParametros[0].getClass();
                }
            } else {
                classePrincipal = pParametros[0].getClass();
            }

            CriteriaBuilder criterioBuilder = em.getCriteriaBuilder();

            CriteriaQuery criterioQuery = null;
            Root entidadePrincipal = null;

            criterioQuery = criterioBuilder.createQuery(classePrincipal);
            entidadePrincipal = criterioQuery.from(classePrincipal);
            criterioQuery.select(entidadePrincipal);
            if (criterioQuery == null) {
                throw new UnsupportedOperationException("Impossivel determinar o tipo de pesquisa padrão a ser realizada");
            }
            Map<String, Long> valoresParametro = new HashMap<>();
            List<Predicate> condicoes = new ArrayList<>();
            int i = 0;
            for (Object parametro : pParametros) {
                ComoEntidadeSimples beanParametro = (ComoEntidadeSimples) parametro;
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
                    for (ItfLigacaoMuitosParaUm relacao : est.getMuitosParaUm()) {
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

            Query qr = em.createQuery(criterioQuery);
            for (String prString : valoresParametro.keySet()) {
                qr.setParameter(prString, valoresParametro.get(prString));
            }

            return qr.getResultList();

        } catch (NoResultException nenhumRegistroEncontrado) {
            return null;
        } catch (Throwable t) {

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro obtendo valor calculado para" + this.toString(), t);
            return null;
        } finally {

            //    UtilSBPersistencia.fecharEM(em);
        }

    }

}
