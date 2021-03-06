/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao.consultaDinamica;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.Persistencia.dao.calculosListagens.FabTipoFiltroCalculo;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.hibernate.hql.internal.ast.tree.OrderByClause;

/**
 *
 * @author desenvolvedor
 */
public class ConsultaDinamicaDeEntidade {

    private final Class entidadePrincipal;
    private List<CondicaoConsulta> condicoes = new ArrayList<>();
    private FabTipoFiltroCalculo tipoCalculo;
    private boolean retornarValorUnico = true;
    private final Map<String, Object> valoresParametro = new HashMap<>();
    private final List<Predicate> predicadosCriteriaAPIGerados = new ArrayList<>();
    private EntityManager em;
    private boolean ordemreversa = false;

    public ConsultaDinamicaDeEntidade(Class entidadePrincipal, EntityManager pEm) {
        this.entidadePrincipal = entidadePrincipal;
        em = pEm;
    }

    public ConsultaDinamicaDeEntidade(Class entidadePrincipal) {
        this.entidadePrincipal = entidadePrincipal;
    }

    /**
     * Ainda não implmentado
     *
     * @param pOrdemReversa
     * @return
     * @deprecated
     */
    @Deprecated
    public ConsultaDinamicaDeEntidade setOrDemReversa(boolean pOrdemReversa) {
        ordemreversa = pOrdemReversa;
        return this;
    }

    public ConsultaDinamicaDeEntidade addCondicaoManyToOneContemNoIntervalo(String pCaminhoCampoCondicao, List<? extends ItfBeanSimples> pEtidadesManyToOne) {
        if (pEtidadesManyToOne.isEmpty()) {
            return this;
        }
        CondicaoConsulta condicaoManyToOneIn = new CondicaoConsulta(this, FabTipoCondicaoJPQL.MANY_TO_ONE_NESTE_INTERVALO);
        condicaoManyToOneIn.setCaminhoCampoCondicao(pCaminhoCampoCondicao);
        condicaoManyToOneIn.setValorParametro(pEtidadesManyToOne);
        condicoes.add(condicaoManyToOneIn);

        return this;
    }

    public ConsultaDinamicaDeEntidade addCondicaoManyToOneIgualA(ItfBeanSimples pEtidadeManyToOne) {
        CondicaoConsulta condManyToOneAutoConfig = new CondicaoConsulta(this, FabTipoCondicaoJPQL.MANY_TO_ONE_IGUAL_AUTO);
        condManyToOneAutoConfig.setValorParametro(pEtidadeManyToOne);
        condicoes.add(condManyToOneAutoConfig);
        return this;
    }

    public ConsultaDinamicaDeEntidade addCondicaoManyToOneIgualA(String pCaminhoCampoCondicao, ItfBeanSimples pEtidadeManyToOne) {
        CondicaoConsulta condManyToOneAutoConfig = new CondicaoConsulta(this, FabTipoCondicaoJPQL.MANY_TO_ONE_IGUAL_AUTO);
        condManyToOneAutoConfig.setCaminhoCampoCondicao(pCaminhoCampoCondicao);
        condManyToOneAutoConfig.setValorParametro(pEtidadeManyToOne);
        condicoes.add(condManyToOneAutoConfig);
        return this;
    }

    public ConsultaDinamicaDeEntidade addCondicaoManyToOneDiferenteDeNulo(String pCaminhoCampoCondicao) {
        CondicaoConsulta condManyToOneAutoConfig = new CondicaoConsulta(this, FabTipoCondicaoJPQL.MANY_TO_ONE_IGUAL_AUTO);
        condManyToOneAutoConfig.setCaminhoCampoCondicao(pCaminhoCampoCondicao);
        condManyToOneAutoConfig.setValorParametro(FabCondicaoEspecialSql.IS_NOT_NULL);

        condicoes.add(condManyToOneAutoConfig);
        return this;
    }

    public ConsultaDinamicaDeEntidade addCondicaoManyToManyContendoObjeto(String pCaminhoCampoCondicao, ItfBeanSimples pEtidadeManyToOne) {
        CondicaoConsulta condManyToOneAutoConfig = new CondicaoConsulta(this, FabTipoCondicaoJPQL.MANY_TO_MANY_CONTEM_OBJETO);
        condManyToOneAutoConfig.setCaminhoCampoCondicao(pCaminhoCampoCondicao);
        condManyToOneAutoConfig.setValorParametro(pEtidadeManyToOne);
        condicoes.add(condManyToOneAutoConfig);
        return this;
    }

    public ConsultaDinamicaDeEntidade addcondicaoCampoIgualA(String pCampo, Object pValor) {
        CondicaoConsulta condicaoSimples = new CondicaoConsulta(this, FabTipoCondicaoJPQL.CAMINHO_CAMPO_IGUAL_VALOR);
        condicaoSimples.setCaminhoCampoCondicao(pCampo);
        condicaoSimples.setValorParametro(pValor);
        condicoes.add(condicaoSimples);
        return this;
    }

    @SuppressWarnings({"BooleanConstructorCall", "UnnecessaryBoxing"})
    public ConsultaDinamicaDeEntidade addCondicaoPositivo(String pCampo) {
        CondicaoConsulta condPositivo = new CondicaoConsulta(this, FabTipoCondicaoJPQL.VALOR_POSITIVO);
        condPositivo.setCaminhoCampoCondicao(pCampo);
        condPositivo.setValorParametro(new Boolean(true));
        condicoes.add(condPositivo);
        return this;
    }

    @SuppressWarnings({"BooleanConstructorCall", "UnnecessaryBoxing"})
    public ConsultaDinamicaDeEntidade addCondicaoNegativo(String pCampo) {
        CondicaoConsulta condNegativo = new CondicaoConsulta(this, FabTipoCondicaoJPQL.VALOR_POSITIVO);
        condNegativo.setCaminhoCampoCondicao(pCampo);
        condNegativo.setValorParametro(new Boolean(false));
        condicoes.add(condNegativo);
        return this;
    }

    public long resultadoSomarQuantidade() {
        try {
            tipoCalculo = FabTipoFiltroCalculo.SOMA_QTD;
            retornarValorUnico = true;
            return (long) obterResultado();
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro obtendo soma de quantidade para entidade " + entidadePrincipal.getSimpleName() + " " + condicoes, t);
            return 0;
        }
    }

    private Object obterResultado() {
        boolean entityEnviado = true;
        if (em == null) {
            em = UtilSBPersistencia.getEMDoContexto();
            entityEnviado = false;
        }
        try {

            //  if (!retornarValorUnico) {
            //       throw new UnsupportedOperationException("Ainda não Implementado");
            //   }
            CriteriaBuilder construtorDeConsulta = em.getCriteriaBuilder();
            CriteriaQuery criteriosDaconsulta = tipoCalculo.gerarCriteriaQuery(entidadePrincipal, construtorDeConsulta);

            condicoes.forEach((condicao) -> {
                condicao.getTipoCondicao().aplicar(criteriosDaconsulta, condicao, construtorDeConsulta);
            });

            if (!predicadosCriteriaAPIGerados.isEmpty()) {
                criteriosDaconsulta.where(predicadosCriteriaAPIGerados.toArray(new Predicate[]{}));

            }
            if (ordemreversa) {
                if (criteriosDaconsulta.getOrderList().isEmpty()) {

                }
            }
            Query consulta = em.createQuery(criteriosDaconsulta);

            consulta.getParameters().forEach(pr
                    -> {
                try {
                    consulta.setParameter(pr.getName(), valoresParametro.get(pr.getName()));
                } catch (Throwable t) {
                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro aplicando parametros", t);
                }
            });

            switch (tipoCalculo) {
                case SOMA_QTD:
                    return consulta.getSingleResult();

                case REGISTRO_UNICO:
                    consulta.setMaxResults(1);
                    return consulta.getSingleResult();

                case MAIOR:
                    return consulta.getSingleResult();

                case MENOR:
                    return consulta.getSingleResult();
                case LISTAGENS:
                    return consulta.getResultList();

                default:
                    throw new AssertionError(tipoCalculo.name());

            }

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro executando consulta simples por CriteriaAPI", t);
        } finally {
            if (!entityEnviado) {
                UtilSBPersistencia.fecharEM(em);
            }
        }

        return null;
    }

    public List resultadoRegistros() {
        try {
            tipoCalculo = FabTipoFiltroCalculo.LISTAGENS;
            retornarValorUnico = false;
            return (List) obterResultado();
        } catch (Throwable t) {
            return new ArrayList();
        }
    }

    public Object getPrimeiroRegistro() {

        throw new UnsupportedOperationException("Não implementado -> nos ajude em coletivojava.com.br, Você pode utilizar UtilSBPersistencia.getRegistroByJPQL(pSQL) ou UtilSBPersistencia.getRegistroBySQL(pSQL)");

    }

    public Map<String, Object> getValoresParametro() {
        return valoresParametro;
    }

    public Class getEntidadePrincipal() {
        return entidadePrincipal;
    }

    public List<CondicaoConsulta> getCondicoes() {
        return condicoes;
    }

    public void adicionarPredicado(Predicate pPredicado) {
        predicadosCriteriaAPIGerados.add(pPredicado);
    }

}
