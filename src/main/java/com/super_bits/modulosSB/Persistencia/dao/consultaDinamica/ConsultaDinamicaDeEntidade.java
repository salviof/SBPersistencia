/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao.consultaDinamica;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.Persistencia.dao.calculosListagens.FabTipoFiltroCalculo;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoEntidadeSimples;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

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
    private int limite = -1;

    public ConsultaDinamicaDeEntidade(Class entidadePrincipal, EntityManager pEm) {
        this.entidadePrincipal = entidadePrincipal;
        em = pEm;
    }

    public ConsultaDinamicaDeEntidade(Class entidadePrincipal) {
        this.entidadePrincipal = entidadePrincipal;
    }

    public ConsultaDinamicaDeEntidade addCondicaoManyToOneContemNoIntervalo(String pCaminhoCampoCondicao, List<? extends ComoEntidadeSimples> pEtidadesManyToOne) {
        if (pEtidadesManyToOne.isEmpty()) {
            return this;
        }
        CondicaoConsulta condicaoManyToOneIn = new CondicaoConsulta(this, FabTipoCondicaoJPQL.MANY_TO_ONE_NESTE_INTERVALO);
        condicaoManyToOneIn.setCaminhoCampoCondicao(pCaminhoCampoCondicao);
        condicaoManyToOneIn.setValorParametro(pEtidadesManyToOne);
        condicoes.add(condicaoManyToOneIn);

        return this;
    }

    public ConsultaDinamicaDeEntidade addCondicaoManyToOneIgualA(ComoEntidadeSimples pEtidadeManyToOne) {
        CondicaoConsulta condManyToOneAutoConfig = new CondicaoConsulta(this, FabTipoCondicaoJPQL.MANY_TO_ONE_IGUAL_AUTO);
        condManyToOneAutoConfig.setValorParametro(pEtidadeManyToOne);
        condicoes.add(condManyToOneAutoConfig);
        return this;
    }

    public ConsultaDinamicaDeEntidade addCondicaoManyToOneIgualA(String pCaminhoCampoCondicao, ComoEntidadeSimples pEtidadeManyToOne) {
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

    public ConsultaDinamicaDeEntidade addCondicaoManyToOneNulo(String pCaminhoCampoCondicao) {
        CondicaoConsulta condManyToOneAutoConfig = new CondicaoConsulta(this, FabTipoCondicaoJPQL.MANY_TO_ONE_IGUAL_AUTO);
        condManyToOneAutoConfig.setCaminhoCampoCondicao(pCaminhoCampoCondicao);
        condManyToOneAutoConfig.setValorParametro(FabCondicaoEspecialSql.IS_NULL);

        condicoes.add(condManyToOneAutoConfig);
        return this;
    }

    public ConsultaDinamicaDeEntidade addCondicaoManyToManyContendoObjeto(String pCaminhoCampoCondicao, ComoEntidadeSimples pEtidadeManyToOne) {
        CondicaoConsulta condManyToOneAutoConfig = new CondicaoConsulta(this, FabTipoCondicaoJPQL.MANY_TO_MANY_CONTEM_OBJETO);
        condManyToOneAutoConfig.setCaminhoCampoCondicao(pCaminhoCampoCondicao);
        condManyToOneAutoConfig.setValorParametro(pEtidadeManyToOne);
        condicoes.add(condManyToOneAutoConfig);
        return this;
    }

    public ConsultaDinamicaDeEntidade addCondicaoDataHoraMaiorOuIgualA(String pCaminhoCampoCondicao, Date pDataHora) {
        CondicaoConsulta condicaoSimples = new CondicaoConsulta(this, FabTipoCondicaoJPQL.DATA_HORA_MAIOR_OU_IGUAL);
        condicaoSimples.setCaminhoCampoCondicao(pCaminhoCampoCondicao);
        condicaoSimples.setValorParametro(pDataHora);
        condicoes.add(condicaoSimples);
        return this;

    }

    public ConsultaDinamicaDeEntidade addCondicaoDataHoraMenorOuIgualA(String pCaminhoCampoCondicao, Date pDataHora) {
        CondicaoConsulta condicaoSimples = new CondicaoConsulta(this, FabTipoCondicaoJPQL.DATA_HORA_MENUR_OU_IGUAL);
        condicaoSimples.setCaminhoCampoCondicao(pCaminhoCampoCondicao);
        condicaoSimples.setValorParametro(pDataHora);
        condicoes.add(condicaoSimples);
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

    public long gerarResultadoSomarQuantidade() {
        return resultadoSomarQuantidade();
    }

    @Deprecated
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

    private boolean ordemIDReversa = false;
    private boolean ordemCampoPersonalizado = false;
    private String campoOrdemPersonalizada;

    public ConsultaDinamicaDeEntidade setOrdemIdReversa() {
        ordemIDReversa = true;
        return this;
    }

    public ConsultaDinamicaDeEntidade setOrdemCampoPersonalizado(String pCampo) {
        ordemCampoPersonalizado = true;
        campoOrdemPersonalizada = pCampo;
        return this;
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

            if (ordemIDReversa) {
                Root raizSql = (Root) criteriosDaconsulta.getRoots().stream().findFirst().get();
                criteriosDaconsulta.select(raizSql).orderBy(construtorDeConsulta.desc(raizSql.get("id")));
            } else if (ordemCampoPersonalizado) {
                Root raizSql = (Root) criteriosDaconsulta.getRoots().stream().findFirst().get();
                criteriosDaconsulta.select(raizSql).orderBy(construtorDeConsulta.asc(raizSql.get(campoOrdemPersonalizada)));
            }
            Query consulta = em.createQuery(criteriosDaconsulta);

            consulta.getParameters().forEach(pr
                    -> {
                try {

                    String nomeParametro = pr.getName();
                    Object valor = valoresParametro.get(nomeParametro);
                    consulta.setParameter(nomeParametro, valor);
                } catch (Throwable t) {
                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro aplicando parametros", t);
                }
            });
            if (limite == 1) {
                tipoCalculo = FabTipoFiltroCalculo.REGISTRO_UNICO;
            }
            if (limite > 1) {
                consulta.setMaxResults(limite);
            }
            switch (tipoCalculo) {
                case SOMA_QTD:
                    return consulta.getSingleResult();

                case REGISTRO_UNICO:
                    consulta.setMaxResults(1);
                    try {
                        return consulta.getSingleResult();
                    } catch (Throwable t) {
                        return null;
                    }
                case MAIOR:
                    return consulta.getSingleResult();

                case MENOR:
                    return consulta.getSingleResult();
                case LISTAGENS:
                    if (listarApenasUmRegistro) {
                        return consulta.getSingleResult();
                    }

                    return consulta.getResultList();

                default:
                    throw new AssertionError(tipoCalculo.name());

            }

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro executando consulta simples por CriteriaAPI", t);
            return null;
        } finally {
            if (!entityEnviado) {
                UtilSBPersistencia.fecharEM(em);
            }
        }

    }

    public List gerarResultados() {
        return gerarResultados(-1);
    }

    public List gerarResultados(int pLimite) {
        try {

            limite = pLimite;
            tipoCalculo = FabTipoFiltroCalculo.LISTAGENS;
            retornarValorUnico = false;
            Object result = obterResultado();
            if (result instanceof List) {
                return (List) obterResultado();
            } else {
                if (result != null) {
                    List lista = new ArrayList();
                    lista.add(result);
                    return lista;
                }
            }
            return new ArrayList();
        } catch (Throwable t) {
            return new ArrayList();
        }
    }

    /**
     * @deprecated Usar gerarResultados()
     * @return gerarResultados();
     */
    public List resultadoRegistros() {
        try {
            tipoCalculo = FabTipoFiltroCalculo.LISTAGENS;
            retornarValorUnico = false;
            return (List) obterResultado();
        } catch (Throwable t) {
            return new ArrayList();
        }
    }
    boolean listarApenasUmRegistro = false;

    public <T extends ComoEntidadeSimples> T getPrimeiroRegistro() {
        tipoCalculo = FabTipoFiltroCalculo.REGISTRO_UNICO;
        listarApenasUmRegistro = true;

        return (T) obterResultado();

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
