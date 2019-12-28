/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao.consultaDinamica;

import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaDeEntidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;

/**
 *
 * @author desenvolvedor
 */
public enum FabTipoCondicaoJPQL {

    MANY_TO_ONE_IGUAL_AUTO,
    MANY_TO_ONE_NESTE_INTERVALO,
    MANY_TO_MANY_CONTEM_OBJETO,
    CAMINHO_CAMPO_IGUAL_VALOR,
    VALOR_POSITIVO;

    public void aplicar(CriteriaQuery pCriterio, CondicaoConsulta pCondicao, CriteriaBuilder pBuilder) {
        EstruturaDeEntidade est = MapaObjetosProjetoAtual.getEstruturaObjeto(pCondicao.getConsulta().getEntidadePrincipal());
        Root entidadePrincipal = (Root) pCriterio.getRoots().iterator().next();
        String nomeCampoPesquisa = pCondicao.getCaminhoCampoCondicao();
        switch (this) {
            case MANY_TO_ONE_IGUAL_AUTO:
                ItfBeanSimples beanParametro = (ItfBeanSimples) pCondicao.getValorParametro();

                ParameterExpression<Integer> prQuery = pBuilder.parameter(Integer.class, pCondicao.getNomeParametro());
                pCondicao.getConsulta().getValoresParametro().put(pCondicao.getNomeParametro(), beanParametro.getId());
                Path caminhoCondicacaoManyToOneIgual = getPachCampoCondicao(entidadePrincipal, nomeCampoPesquisa);
                Predicate condicao = pBuilder.equal(caminhoCondicacaoManyToOneIgual, prQuery);

                pCondicao.getConsulta().adicionarPredicado(condicao);

                break;

            case CAMINHO_CAMPO_IGUAL_VALOR:

                Object valorParametro = pCondicao.getValorParametro();

                ParameterExpression prCampo = null;

                if (valorParametro instanceof Integer) {
                    prCampo = pBuilder.parameter(Integer.class, nomeCampoPesquisa);

                    Predicate condicaoCaminhoCampoIgualValor = pBuilder.equal(entidadePrincipal.get(nomeCampoPesquisa), prCampo);

                    pCondicao.getConsulta().getValoresParametro().put(nomeCampoPesquisa, valorParametro);
                    pCondicao.getConsulta().adicionarPredicado(condicaoCaminhoCampoIgualValor);
                } else if (valorParametro instanceof String) {
                    prCampo = pBuilder.parameter(String.class, nomeCampoPesquisa);
                    Predicate condicaoCaminhoCampoIgualValor = pBuilder.equal(entidadePrincipal.get(nomeCampoPesquisa), prCampo);
                    pCondicao.getConsulta().getValoresParametro().put(nomeCampoPesquisa, valorParametro);
                    pCondicao.getConsulta().adicionarPredicado(condicaoCaminhoCampoIgualValor);
                } else {
                    throw new UnsupportedOperationException("a consulta ainda não suporta parametros do tipo " + valorParametro.getClass().getSimpleName());
                }

                break;

            case VALOR_POSITIVO:
                Boolean parametroPositivo = (Boolean) pCondicao.getValorParametro();
                String nomeCampoPesquisaPositivo = pCondicao.getCaminhoCampoCondicao();
                prCampo = pBuilder.parameter(Boolean.class, nomeCampoPesquisaPositivo);
                Predicate condicaoVerdadeiroFalso = null;
                if (parametroPositivo) {

                    //pBuilder.equal(entidadePrincipal.get(nomeCampoPesquisaPositivo), prCampo);
                    condicaoVerdadeiroFalso = pBuilder.isTrue(entidadePrincipal.get(nomeCampoPesquisaPositivo));

                } else {
                    condicaoVerdadeiroFalso = pBuilder.isFalse(entidadePrincipal.get(nomeCampoPesquisaPositivo));
                }

                pCondicao.getConsulta().getValoresParametro().put(nomeCampoPesquisaPositivo, parametroPositivo);
                pCondicao.getConsulta().adicionarPredicado(condicaoVerdadeiroFalso);

                break;
            case MANY_TO_ONE_NESTE_INTERVALO:

                ParameterExpression<Collection> prQueryIgualIntervalo
                        = pBuilder.parameter(Collection.class, pCondicao.getNomeParametro());

                List<ItfBeanSimples> listaSimples = (List<ItfBeanSimples>) pCondicao.getValorParametro();
                List<Integer> listaCondicao = new ArrayList<>();
                listaSimples.stream().forEach(itemLista -> {
                    listaCondicao.add(itemLista.getId());
                });

                pCondicao.getConsulta().getValoresParametro().put(pCondicao.getNomeParametro(), listaCondicao);

                Path caminhoCondicacao = getPachCampoCondicao(entidadePrincipal, nomeCampoPesquisa);

                Predicate condicaoIn = caminhoCondicacao.in(prQueryIgualIntervalo);

                pCondicao.getConsulta().adicionarPredicado(condicaoIn);

                break;
            case MANY_TO_MANY_CONTEM_OBJETO:
                ItfBeanSimples beanParametroMC = (ItfBeanSimples) pCondicao.getValorParametro();

                Join join = entidadePrincipal.join(nomeCampoPesquisa, JoinType.INNER);
                Path<Long> campoIdObjetos = join.get("id");

                Predicate predPets = campoIdObjetos.in(beanParametroMC.getId());
                pCondicao.getConsulta().getValoresParametro().put(pCondicao.getNomeParametro(), beanParametroMC);
                pCondicao.getConsulta().adicionarPredicado(predPets);

                break;
            default:
                throw new AssertionError(this.name());

        }
    }

    private Path getPachCampoCondicao(Root pEntidade, String pCaminho) {
        String[] etapasCaminhoCampo = pCaminho.split("\\.");
        Path expressao = pEntidade;
        int i = 0;
        if (etapasCaminhoCampo.length > 1) {

            for (String etapaCaminho : etapasCaminhoCampo) {
                i++;
                if (i == etapasCaminhoCampo.length) {
                    expressao = expressao.get(etapaCaminho).get("id");
                } else {
                    expressao = expressao.get(etapaCaminho);
                }
            }
        } else {
            expressao = pEntidade.get(pCaminho).get("id");
        }
        return expressao;
    }
}
