/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao.DriversConexao;

import com.super_bits.modulosSB.Persistencia.ConfigGeral.SBPersistencia;
import com.super_bits.modulosSB.Persistencia.dao.FabInfoPersistirEntidade;
import com.super_bits.modulosSB.Persistencia.dao.InfoPerisistirEntidade;
import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import static com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia.finalizarTransacao;
import static com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia.getNovoEM;
import static com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia.iniciarTransacao;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.fonteDados.FabTipoSelecaoRegistro;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanContatoCorporativo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanContatoPessoa;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.hibernate.exception.JDBCConnectionException;

/**
 *
 * @author desenvolvedor
 */
public class DriverFWBancoJPANativo extends DriverBancoFWAbstrato {

    @Override
    public List<?> selecaoRegistros(EntityManager pEM, String pSQL, String pPQL, Integer maximo, Class pClasseEntidade, UtilSBPersistencia.TIPO_SELECAO_REGISTROS pTipoSelecao, Object... parametros) {
        // todo Se origem for uma MBPAGINA  utilizar o EntityManager da pagina
        // StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        //String nomeMetodo = stackTraceElements[3].getMethodName();
        //String nomeClasse = stackTraceElements[3].getClassName();
        if (maximo == null) {
            maximo = -1;
        }
        boolean usarCache = false;
        if (pClasseEntidade != null) {
            if (pClasseEntidade.isAnnotationPresent(Cacheable.class)) {
                usarCache = true;
            }
        }
        try {
            Boolean entityManagerPaiEnviado = false;
            EntityManager em = null;
            if (pEM == null) {
                em = getNovoEM();
            } else {
                em = pEM;
                entityManagerPaiEnviado = true;
            }

            if (maximo > SBPersistencia.getMAXIMO_REGISTROS()) {
                maximo = SBPersistencia.getMAXIMO_REGISTROS();
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Voce selecionou mais registros que o configurado como permitido pelo sistema  ", null);
            }
            // Configura o numero de parametros nativos = 0, campo importante para
            // ignorar a atribuição de parametros dinamicos nos casos de  querys pre definindas como like nome curto
            int numeroParamentrosNativos = 0;
            try {
                try {
                    String sql = "";
                    switch (pTipoSelecao) {
                        case LIKENOME:
                            sql = getJPSQL(pTipoSelecao, pClasseEntidade, parametros);
                            numeroParamentrosNativos = 1;
                            break;
                        case JPQL:
                            sql = pPQL;
                            break;
                        case SQL:
                            sql = pSQL;
                            break;
                        case TODOS:
                            sql = getJPSQL(pTipoSelecao, pClasseEntidade, parametros);
                            break;
                        default:
                            break;
                    }
                    Query consulta = null;
                    //org.hibernate.Query consulta = null;

                    if (pTipoSelecao == UtilSBPersistencia.TIPO_SELECAO_REGISTROS.SQL) {
                        // consulta = em.createNativeQuery(sql);
                        //  Session sessaoSQLHibernate = em.unwrap(Session.class);
                        //consulta = sessaoSQLHibernate.createSQLQuery(sql);
                        consulta = em.createNativeQuery(sql);
                    } else {

                        //   Session sessaoSQLHibernate = em.unwrap(Session.class);
                        //   sessaoSQLHibernate.setCacheMode(CacheMode.REFRESH);
                        // consulta = sessaoSQLHibernate.createQuery(sql);
                        consulta = em.createQuery(sql);
                    }
                    if (usarCache) {
                        consulta.setHint("org.hibernate.cacheable", true);
                        consulta.setHint("javax.persistence.cache.retrieveMode", "USE");
                    }

                    if (maximo != -1 && maximo != 0) {
                        System.out.println("SetMaximo=" + maximo);
                        consulta.setMaxResults(maximo);
                    }

                    if (parametros != null && parametros.length > numeroParamentrosNativos) {
                        int i = 0;
                        for (Object pr : parametros) {
                            consulta.setParameter(i, pr);
                            i++;
                        }
                    }
                    //consulta.setHint(AvailableSettings.SHARED_CACHE_RETRIEVE_MODE, CacheRetrieveMode.BYPASS);
                    //consulta.setHint(AvailableSettings.SHARED_CACHE_STORE_MODE, CacheStoreMode.BYPASS);
                    //consulta.setHint(AvailableSettings.SHARED_CACHE_MODE, CacheMode.IGNORE);
                    //consulta.setHint("org.hibernate.cacheable", false);
                    //    consulta.setCacheMode(CacheMode.REFRESH);
                    //   consulta.setCacheable(false);

                    List resultado = consulta.getResultList();
                    if (resultado.size() > SBPersistencia.getMAXIMO_REGISTROS()) {
                        System.out.println("este select retorna mais de" + SBPersistencia.getMAXIMO_REGISTROS() + "o sistema não deixará de executar, mas não posso deixar de perguntar Isto está certo ?? se estiver, você pode ativar o Cache com as anotações @Cacheable e @cache, para otimizar recursos");
                        System.out.println("sql");
                    }
                    return resultado;
                } catch (Throwable e) {

                    String logComplementar = "";

                    switch (pTipoSelecao) {
                        case JPQL:
                            logComplementar = " \n jpql=" + pPQL;
                            break;
                        case SQL:
                            logComplementar = " \n sql=" + pSQL;
                            break;
                        case LIKENOME:
                            break;
                        case TODOS:
                            break;
                        case NAMED_QUERY:
                            break;
                        case SBNQ:
                            break;
                        default:
                            throw new AssertionError(pTipoSelecao.name());

                    }

                    if (e.getCause() != null) {
                        if (e.getCause().getClass().getSimpleName().equals(JDBCConnectionException.class.getSimpleName())) {
                            UtilSBPersistencia.renovarFabrica();
                        }
                    }

                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro criando ou executando uma query do tipo" + pTipoSelecao + " \n " + logComplementar, e);
                    return new ArrayList();
                }

            } finally {
                if (!entityManagerPaiEnviado) {
                    System.out.println("ENtity não Enviado=" + entityManagerPaiEnviado + " Fechando conexao do tipo" + pTipoSelecao);
                    if (em != null && !entityManagerPaiEnviado) {
                        try {
                            if (em.getTransaction().isActive()) {
                                em.getTransaction().rollback();
                            }
                        } catch (Throwable t) {
                            SBCore.RelatarErroAoUsuario(FabErro.SOLICITAR_REPARO, "Erro fechando trasação de entityManager em seleção de registros (não foi enviado um entitymanager na operação)", t);
                        }

                        if (em.isOpen()) {
                            em.close();
                        }
                    }
                }
            }

        } catch (Exception e) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro " + pPQL + "-->" + pTipoSelecao, e);
            return new ArrayList();
        }

    }

    /**
     *
     * @param pInfoEntidadesPersistencia
     * @return Em caso de Merge retorna o Objeto atualizado, em outros casos
     * retorna true quando execução ocorrer com sucesso
     */
    @Override
    public Object executaAlteracaoEmBancao(InfoPerisistirEntidade pInfoEntidadesPersistencia) {

        /**
         * Função que executa Alterações em Banco de Dados
         */
        Object pEntidade = pInfoEntidadesPersistencia.getpEntidade();
        List<Object> pEntidades = pInfoEntidadesPersistencia.getpEntidades();
        EntityManager pEM = pInfoEntidadesPersistencia.getpEM();
        FabInfoPersistirEntidade pTipoAlteracao = pInfoEntidadesPersistencia.getTipoAlteracao();

        boolean entityManagerPaiEnviada = false;
        if (pEM != null) {
            entityManagerPaiEnviada = true;
        }

        try {

            EntityManager em = defineEM(pEM, null);
            if (em == null) {
                throw new Exception("Impossível definir o entityManager ");
            }

            List<Object> objetosPersistidos = new ArrayList<>();
            if (pEntidade != null) {
                objetosPersistidos.add(pEntidade);
            }
            if (pEntidades != null) {
                objetosPersistidos = pEntidades;
            }
            // Session sessaoSQLHibernate = em.unwrap(Session.class);

            try {

                try {
                    boolean controleTranzacaoAutomaticoPeloMetodo = false;
                    if (!em.getTransaction().isActive()) {
                        iniciarTransacao(em);
                        controleTranzacaoAutomaticoPeloMetodo = true;
                    }

                    boolean sucesso = false;

                    Object novoRegistro = objetosPersistidos.get(0);
                    for (Object entidade : objetosPersistidos) {
                        sucesso = false;
                        novoRegistro = entidade;
                        switch (pTipoAlteracao) {

                            case DELETE:

                                em.remove(em.getReference(entidade.getClass(), ((ItfBeanSimples) entidade).getId()));

                                sucesso = true;
                                break;
                            case INSERT:

                                em.persist(entidade);
                                sucesso = true;
                                break;
                            case MERGE:
                                try {

                                    novoRegistro = em.merge(entidade);

                                } catch (EntityExistsException tt) {

                                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Entidade já existe, para executar um novo merge, é nescessário criar um novo entityManager, ou utilizar a entidade gerenciada obtida anteriormente, caso exista uma transação ativa, o JPA obrigará execução do roolback", tt);

                                }
                                entidade = novoRegistro;
                                sucesso = true;
                                break;
                            default:
                                break;

                        }
                    }

                    if (controleTranzacaoAutomaticoPeloMetodo) {
                        sucesso = finalizarTransacao(em);

                    }
                    if (pTipoAlteracao == FabInfoPersistirEntidade.MERGE) {
                        if (sucesso) {
                            /// FabMensagens.enviarMensagemUsuario("Registro Alterado com Sucesso", FabMensagens.AVISO);
                            return novoRegistro;
                        } else {
                            SBCore.getCentralDeMensagens().enviarMsgErroAoUsuario("Ocorreu um erro Ao Atualizar o registro");
                            //FabMensagens.enviarMensagemUsuario("Ocorreu um erro Ao Atualizar o registro", FabMensagens.ERRO);
                            return null;
                        }
                    } else {
                        if (sucesso) {
                            //  FabMensagens.enviarMensagemUsuario("Registro Cadastrado com sucesso", FabMensagens.AVISO);
                        } else {
                            SBCore.getCentralDeMensagens().enviarMsgErroAoUsuario("Ocorreu um erro Ao Inserir o registro");
                        }
                        return sucesso;
                    }
                } catch (Exception e) {

                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro executando alteração em Banco" + pInfoEntidadesPersistencia.toString(), e);

                    if (!em.getTransaction().isActive()) {
                        try {
                            em.getTransaction().rollback();
                        } catch (Exception e2) {
                            System.out.println("Erro executando roolback devido a falha na inserção");
                        }
                    }

                    if (pTipoAlteracao == FabInfoPersistirEntidade.MERGE) {
                        return null;
                    } else {
                        return false;
                    }
                }

            } finally {
                if (!entityManagerPaiEnviada) {
                    UtilSBPersistencia.fecharEM(em);
                }
            }

        } catch (Exception e) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro tentando obter Entity Manager (EM)", e);
            if (pTipoAlteracao == FabInfoPersistirEntidade.MERGE) {
                return null;
            } else {
                return false;
            }
        }

    }

    @Override
    public Object selecaoRegistro(EntityManager pEM, String pSQL, String pPQL, Class pClasseRegisto, FabTipoSelecaoRegistro pTipoSelecao, FabTipoAtributoObjeto pCampo, Object... parametros) {
        boolean entityManagerPaiEnviado = false;
        try {
            EntityManager em;
            if (pEM != null) {
                em = pEM;
                entityManagerPaiEnviado = true;
            } else {
                em = getNovoEM();
            }

            try {
                try {
                    Query consulta;

                    String sql = "";
                    switch (pTipoSelecao) {
                        case ID:
                            Object resposta = em.find(pClasseRegisto, parametros[0]);
                            return resposta;

                        case NOMECURTO:
                            ItfBeanSimples registroNC = (ItfBeanSimples) pClasseRegisto.newInstance();
                            String campoNomeCurtoNC = registroNC.getNomeCampo(FabTipoAtributoObjeto.AAA_NOME);
                            String parametroNC = (String) parametros[0];
                            sql = "from " + pClasseRegisto.getSimpleName() + " where "
                                    + campoNomeCurtoNC + " = '" + parametroNC + "'";

                            break;
                        case LIKENOMECURTO:
                            ItfBeanSimples registro = (ItfBeanSimples) pClasseRegisto.newInstance();
                            String campoNomeCurto = registro.getNomeCampo(FabTipoAtributoObjeto.AAA_NOME);
                            String parametro = (String) parametros[0];
                            sql = "from " + pClasseRegisto.getSimpleName() + " where "
                                    + campoNomeCurto + " like '" + parametro + "'";
                            break;
                        case PRIMEIRO_REGISTRO:
                            sql = "from " + pClasseRegisto.getSimpleName();
                            break;
                        case SQL:
                            sql = pSQL;
                            break;
                        case ULTIMO_REGISTRO:
                            throw new UnsupportedOperationException("A busca por Ultimo Registro ainda não foi implementada");

                        case JPQL:
                            sql = pPQL;
                            break;
                        case ENCONTRAR_EMPRESA:

                            ItfBeanContatoCorporativo registroCorporativo = (ItfBeanContatoCorporativo) pClasseRegisto.newInstance();
                            String telefone = registroCorporativo.getNomeCampo(FabTipoAtributoObjeto.TELEFONE_FIXO_NACIONAL);

                            String pr = (String) parametros[0];
                            sql = "from " + pClasseRegisto.getSimpleName() + " where "
                                    + telefone + " like '" + pr + "'";

                            break;
                        case ENCONTRAR_PESSOA:
                            ItfBeanContatoPessoa buscaPessoaFisica;
                            throw new UnsupportedOperationException("A busca de pessoa ainda não foi implementada");

                        case QUANTIDADE_REGISTROS:

                            CriteriaBuilder qb = em.getCriteriaBuilder();
                            CriteriaQuery<Long> cq = qb.createQuery(Long.class);
                            cq.select(qb.count(cq.from(pClasseRegisto)));
                            return em.createQuery(cq).getSingleResult();
                        case ENCONTRAR_EMPRESA_POR_CNPJ:

                            ItfBeanSimples registroCorporativoCNPJ = (ItfBeanContatoCorporativo) pClasseRegisto.newInstance();
                            String cnpj = registroCorporativoCNPJ.getNomeCampo(FabTipoAtributoObjeto.CNPJ);

                            String prcnpj = (String) parametros[0];
                            sql = "from " + pClasseRegisto.getSimpleName() + " where "
                                    + cnpj + " = '" + prcnpj + "'";

                            break;
                        case TIPO_CAMPO_ESPECIFICO_IGUAL_A:
                            ItfBeanSimples registroSQL = (ItfBeanSimples) pClasseRegisto.newInstance();
                            String campoPersonalizado = registroSQL.getNomeCampo(pCampo);
                            String parametroSQLPersonalizado = (String) parametros[0];

                            switch (pCampo.getTipoPrimitivo()) {
                                case INTEIRO:
                                    break;
                                case LETRAS:
                                    parametroSQLPersonalizado = "'" + parametroSQLPersonalizado + "'";
                                    break;
                                case DATAS:
                                case BOOLEAN:
                                case DECIMAL:
                                case ENTIDADE:
                                case OUTROS_OBJETOS:

                                    throw new UnsupportedOperationException("A pesquisa padrao por registro de campo do tipo " + pCampo + " ainda não foi implementada por ser do tipo primitivo:" + pCampo.getTipoPrimitivo());
                                default:
                                    throw new AssertionError(pCampo.getTipoPrimitivo().name());

                            }

                            sql = "from " + pClasseRegisto.getSimpleName() + " where "
                                    + campoPersonalizado + " = " + parametroSQLPersonalizado + "";

                            break;

                        default:
                            throw new AssertionError(pTipoSelecao.name());
                    }
                    if (pTipoSelecao == FabTipoSelecaoRegistro.SQL) {
                        if (pClasseRegisto == null) {
                            System.out.println("Criando Query por string:" + sql);
                            consulta = em.createNativeQuery(sql);
                        } else {
                            System.out.println("Criando JPQL por string:" + sql);
                            consulta = em.createNativeQuery(sql, pClasseRegisto);
                        }

                    } else if (pClasseRegisto == null) {
                        consulta = em.createQuery(sql);
                    } else {
                        consulta = em.createQuery(sql, pClasseRegisto);
                    }
                    consulta.setMaxResults(1);

                    List<?> resposta = consulta.getResultList();
                    if (resposta.isEmpty()) {
                        return null;
                    }
                    return resposta.get(0);

                } catch (Throwable e) {
                    if (e.getCause() != null) {
                        if (e.getCause().getClass().getSimpleName().equals(JDBCConnectionException.class.getSimpleName())) {
                            UtilSBPersistencia.renovarFabrica();
                        }
                    }

                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro criando ou executando uma query de registro unico", e);
                    return null;
                }

            } finally {
                if (!entityManagerPaiEnviado) {
                    if (em != null) {
                        em.close();
                    }
                }
            }

        } catch (Exception e) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro definindo EM para select de registro unico", e);
            return null;
        }

    }

}
