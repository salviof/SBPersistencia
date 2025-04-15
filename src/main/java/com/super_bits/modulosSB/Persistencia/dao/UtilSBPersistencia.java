/*
 *   Super-Bits.com CODE CNPJ 20.019.971/0001-90

 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.google.common.collect.Lists;
import com.super_bits.modulosSB.Persistencia.ConfigGeral.SBPersistencia;
import com.super_bits.modulosSB.Persistencia.dao.consultaDinamica.ConsultaDinamicaDeEntidade;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreReflexaoObjeto;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringValidador;
import com.super_bits.modulosSB.SBCore.modulos.fonteDados.FabTipoSelecaoRegistro;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimplesSomenteLeitura;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OptimisticLockException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.metamodel.EntityType;
import javax.validation.constraints.NotNull;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.hibernate.proxy.HibernateProxy;

/**
 *
 * Conjunto de metodos estatiscos para auxiliar no acesso ao banco de dados
 *
 *
 * @author Sálvio Furbino <salviof@gmail.com>
 * @since 19/07/2013
 *
 */
public class UtilSBPersistencia implements Serializable, ItfDados {

    private static EntityManagerFactory emFacturePadrao;

    private static Map<String, Object> configuracoesPeristenciaPadrao = new HashMap<>();
    private static FabTipoImplementacaoJPA tipoImplementacao = FabTipoImplementacaoJPA.HIBERNATE;

    /**
     *
     * Temporário, nescessário para não criar dois entitymanager durante o modo
     * desenvolvedor
     *
     * Será substituído na versão 1.5 do modulo de persistencia do sistema
     *
     * @param fabrica
     * @param propriedades
     * @deprecated
     */
    @Deprecated
    public static void defineFabricaEntityManager(EntityManagerFactory fabrica, Map<String, Object> propriedades) {
        emFacturePadrao = fabrica;
        configuracoesPeristenciaPadrao = propriedades;
    }

    /**
     *
     */
    public static enum AVISAR {

        /**
         * Uma mensagem para o programador só acontece no modo de
         * desenvolvimento e homologação
         */
        PROGRAMADOR,
        /**
         * Uma mensagem para o usuário
         */
        USUARIO,
        /**
         * Uma mensagem do sistema é uma mensagem que deve ser arquivada em logs
         * do sistema
         */
        SISTEMA
    };

    /**
     *
     * Tipos de selação conhecidos do sistema
     *
     */
    public static enum TIPO_SELECAO_REGISTROS {

        /**
         * Selação por JPQL (Java persistence query language) (O SQL do JPA)
         */
        JPQL,
        /**
         * Seleção por SQL nativo, atenção usar este método deixa o sistema
         * incompativel com outros tipos de banco de dados
         */
        SQL,
        /**
         * Seleção de registro Like nome
         */
        LIKENOME,
        /**
         * Seleciona todos os registros
         */
        TODOS,
        /**
         * Seleciona registros por Named Querys
         */
        NAMED_QUERY,
        /**
         * Seleciona registro por Super Bits Named Querys
         */
        @Deprecated
        SBNQ;
    }

    public static void renovarFabrica() {
        if (emFacturePadrao != null) {
            if (emFacturePadrao.isOpen()) {
                emFacturePadrao.close();
                emFacturePadrao = null;
            }
        }
        getNovoEM().close();

    }

    private static final Map<String, EntityManagerFactory> BANCO_EXTRA = new HashMap<>();

    /**
     *
     * Cria um novo entity Manager, O entity Manager gera uma nova conexão com o
     * banco que só encerrada após o comando close
     *
     * @param pNomeBanco Caso em branco ou nulo retorna EM padrão.
     * @return Nulo se não for possível criar, e a entidade caso consiga
     */
    public synchronized static EntityManager getNovoEM(String pNomeBanco) {
        EntityManager novoEM = null;

        if (pNomeBanco == null || pNomeBanco.length() == 0) {
            try {
                return getNovoEM();
            } catch (Exception e) {
                return null;
            }
        }

        try {

            EntityManagerFactory fabrica = BANCO_EXTRA.get(pNomeBanco);

            if (fabrica == null) {
                System.out.println("Criando EMF" + pNomeBanco);
                fabrica = Persistence.createEntityManagerFactory(pNomeBanco);

                if (fabrica == null) {
                    throw new UnsupportedOperationException(" Erro criando EntityFactury" + pNomeBanco, null);
                } else {
                    //  fabrica.getCache().evictAll();
                    BANCO_EXTRA.put(pNomeBanco, fabrica);
                    return fabrica.createEntityManager();
                }
            }
        } catch (Throwable e) {
            SBCore.RelatarErro(FabErro.ARQUIVAR_LOG, " Erro tentando Localizar o EMF:" + pNomeBanco, e);
            return null;
        }
        return null;
    }

    /**
     *
     * Cria um novo entity Manager, do Banco padrão entity Manager gera uma nova
     * conexão com o banco que só encerrada após o comando close
     *
     * @return Nulo se não for possível criar, e a entidade caso consiga
     */
    public static EntityManager getEntyManagerPadraoNovo() {
        return getNovoEM();
    }

    /**
     *
     * Cria um novo entity Manager, do Banco padrão entity Manager gera uma nova
     * conexão com o banco que só encerrada após o comando close MESMO que
     * getEntyManagerPadraoNovo
     *
     * @return Nulo se não for possível criar, e a entidade caso consiga
     */
    public static EntityManager getEMPadraoNovo() {
        return getNovoEM();
    }

    /**
     *
     * Cria um novo entity Manager, do Banco padrão entity Manager gera uma nova
     * conexão com o banco que só encerrada após o comando close
     *
     * @return Nulo se não for possível criar, e a entidade caso consiga
     */
    @Deprecated
    public static EntityManager getNovoEM() {

        try {
            if (emFacturePadrao == null) {

                emFacturePadrao = Persistence.createEntityManagerFactory(SBPersistencia.getNomeBancoPadrao(), configuracoesPeristenciaPadrao);
                //         emFacturePadrao.getCache().evictAll();

            }
        } catch (Exception e) {

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro tentando criar entitymanagerFacturePAdrão=" + SBPersistencia.getNomeBancoPadrao(), e);
            try {
                emFacturePadrao = Persistence.createEntityManagerFactory(SBPersistencia.getNomeBancoPadrao());
            } catch (Throwable ee) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro tentando criar entitymanagerFActurePadrão, segunda tentativa=" + SBPersistencia.getNomeBancoPadrao(), ee);
            }
        }

        //for (String propriedade : emFacturePadrao.getProperties().keySet()) {
        //   System.out.println("Propriedade" + propriedade + "Tipo Objeto:" + emFacturePadrao.getProperties().get(propriedade).getClass().getSimpleName()
        //          + "=" + emFacturePadrao.getProperties().get(propriedade).toString());
        // }
        try {

            try {
                if (emFacturePadrao == null) {
                    throw new Exception("Erro tentando criar EntityManager, a Fabrica de EM não pode ser construída");
                }

                EntityManager emcriada = emFacturePadrao.createEntityManager();

                if (emcriada == null || !emFacturePadrao.isOpen() || !emcriada.isOpen()) {
                    throw new Exception("Erro tentando criar EntityManager");
                }
                return emcriada;
            } catch (Throwable e) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro tentando criar entitymanager" + SBPersistencia.getNomeBancoPadrao(), e);
                System.out.println("Recirando Factury na tentativa de gerar um novo EM");
                emFacturePadrao = Persistence.createEntityManagerFactory(SBPersistencia.getNomeBancoPadrao());
                return emFacturePadrao.createEntityManager();
            }

        } finally {
            //   emFacturePadrao.getCache().evictAll();
        }
    }

    public static EntityManager getNovoEMIniciandoTransacao() {
        EntityManager em = getNovoEM();
        em.getTransaction().begin();
        return em;
    }

    public static boolean fecharEM(EntityManager em) {
        if (em == null) {
            return true;
        }
        if (em.isOpen()) {
            try {
                try {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                } catch (Throwable t) {
                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro executando rooll back em tranação ativa na tentativa de fechar um entityManager", t);
                }

                em.close();

                return true;
            } catch (Throwable t) {

                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro fechando entityManager", t);
                return false;
            }
        } else {
            return true;
        }
    }

    public static boolean finzalizaTransacaoEFechaEMRevertendoAlteracoes(@NotNull EntityManager em) {
        try {
            if (em == null) {
                throw new UnsupportedOperationException("O entity manager está nulo");

            }
            if (!em.isOpen()) {
                throw new UnsupportedOperationException("O entity manager está fechado");
            }
            if (!em.getTransaction().isActive()) {
                throw new UnsupportedOperationException("Não existe Transação ativa neste EntityManager");
            }
            em.getTransaction().rollback();

            em.close();
            return true;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Ocorreu um erro ao finalizar a tranzação", t);
            return false;
        }
    }

    public static boolean finzalizaTransacaoEFechaEM(@NotNull EntityManager em) {
        try {
            if (em == null) {
                throw new UnsupportedOperationException("O entity manager está nulo");

            }
            if (!em.isOpen()) {
                throw new UnsupportedOperationException("O entity manager está fechado");
            }
            if (!em.getTransaction().isActive()) {
                throw new UnsupportedOperationException("Não existe Transação ativa neste EntityManager");
            }
            em.getTransaction().commit();

            em.close();
            return true;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Ocorreu um erro ao finalizar a tranzação", t);
            return false;
        }

    }

    public static boolean iniciarTransacao(@NotNull EntityManager pEM) {

        try {
            if (pEM == null) {
                throw new UnsupportedOperationException("O entity manager está nulo");

            }
            if (!pEM.isOpen()) {
                throw new UnsupportedOperationException("O entity manager está fechado");
            }
            pEM.getTransaction().begin();

            return true;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Ocorreu um erro ao Iniciar a tranzação", t);
            return false;
        }

    }

    /**
     *
     * ATENÇÃO -> NÃO FECHA O ENTITYMANAGER <- Apenas Commita
     *
     * @param em
     * @return
     */
    public static boolean finalizarTransacao(@NotNull EntityManager em) {

        try {
            if (em == null) {
                throw new UnsupportedOperationException("O entity manager está nulo");
            }
            if (!em.isOpen()) {
                throw new UnsupportedOperationException("O entity manager está fechado");
            }
            if (!em.getTransaction().isActive()) {
                throw new UnsupportedOperationException("Não existe transação ativa para finalizar");
            }
            em.getTransaction().commit();
            // Session sessaoSQLHibernate = em.unwrap(Session.class);
            // sessaoSQLHibernate.getTransaction().commit();

            return true;
        } catch (Throwable t) {

            try {
                if (em != null) {
                    if (em.getTransaction().isActive()) {
                        em.getTransaction().rollback();
                    }
                }
            } catch (Throwable tt) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Ocorreu um erro ao executar o RollBack", t);
            }

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Ocorreu um erro ao finalizar a tranzação", t);
            return false;
        }

    }

    public UtilSBPersistencia() {

        if (emFacturePadrao == null) {
            emFacturePadrao = Persistence.createEntityManagerFactory(SBPersistencia.getNomeBancoPadrao());
            //   emFacturePadrao.getCache().evictAll();
        }

    }

    public static EntityManager getEMDoContexto() {
        return SBCore.getCentralDados().getAcessoDadosDoContexto().getEntitiManager();
    }

    public static EntityManagerFactory getEmfabricaPadrao() {
        if (emFacturePadrao == null) {
            emFacturePadrao = Persistence.createEntityManagerFactory(SBPersistencia.getNomeBancoPadrao());
            //   emFacturePadrao.getCache().evictAll();
        }

        return emFacturePadrao;
    }

    /**
     * Codigo padrão para realizar Select de Registros
     *
     *
     * @param pNomeEM Nome do entity maneger extra
     * @param pEM Entity manager a ser utilizado
     * @param pSQL sql que será executado
     * @param pPQL PQL (Persistence query Language)
     * @param maximo Maximo de registros retornados (equivale a limit x) no fim
     * do select
     * @param tipoRegisto Tipo de registro selecionado
     * @param pTipoSelecao
     * @param parametros
     * @return Uma seleção de regostros encontrados, ou uma lista sem registro
     * caso Não encontre nada ou aconteça um erro
     */
    private static List<?> selecaoRegistros(EntityManager pEM, String pSQL, String pPQL, Integer maximo, Class tipoRegisto, TIPO_SELECAO_REGISTROS pTipoSelecao, Object... parametros) {
        return SBPersistencia.getDriverFWBanco().selecaoRegistros(pEM, pSQL, pPQL, maximo, tipoRegisto, pTipoSelecao, parametros);
    }

    private static List<?> selecaoRegistros(EntityManager pEM, String pSQL, String pPQL, Integer pMaximoPagina, int pPagina, Class tipoRegisto, TIPO_SELECAO_REGISTROS pTipoSelecao, Object... parametros) {
        return SBPersistencia.getDriverFWBanco().selecaoRegistros(pEM, pSQL, pPQL, pMaximoPagina, pPagina, tipoRegisto, pTipoSelecao, parametros);
    }

    /**
     *
     * @param pNomeEM Nome do Entity Manager especial (Não é obrigatório)
     * @param pEM Entity Manager que será utilizado (Não é obrigatório)
     * @param pSQL Sql a ser executado
     * @param pPQL Persistence Query Linguage
     * @param maximo Maximo
     * @param tipoRegisto Tipo de registro
     * @param pTipoSelecao Tipo de seleção
     * @param parametros Parametros
     * @return O registro encontrado, ou um null caso não encontre ou aconteça
     * algum erro em sql
     */
    private static Object selecaoRegistro(EntityManager pEM, String pSQL, String pPQL, Class pClasseRegisto, FabTipoSelecaoRegistro pTipoSelecao, Object... parametros) {
        return SBPersistencia.getDriverFWBanco().selecaoRegistro(pEM, pSQL, pPQL, pClasseRegisto, pTipoSelecao, null, parametros);
    }

    private static Object selecaoRegistro(EntityManager pEM, String pSQL, String pPQL, Class pClasseRegisto, FabTipoSelecaoRegistro pTipoSelecao, FabTipoAtributoObjeto pCampo, Object... parametros) {
        return SBPersistencia.getDriverFWBanco().selecaoRegistro(pEM, pSQL, pPQL, pClasseRegisto, pTipoSelecao, pCampo, parametros);
    }

    /**
     * Realizar Merg de Objeto (cria se o key primario não existir, e atualiza
     * caso exista).
     *
     * *Enviando uma Entidade o sistema não irá gerenciar a tranção, ou seja,
     * não vai abrir nem fechar
     *
     * @param obj Objeto que será salvo em banco
     * @param pEm Entity manager que será utilizado
     * @return Objeto atualizado apos ser persistido em banco,e nulo caso ocorra
     * falha
     */
    public static <I extends ItfBeanSimples> I mergeRegistro(Object obj, EntityManager pEm) {

        return (I) SBPersistencia.getDriverFWBanco().executaAlteracaoEmBancao(new InfoPerisistirEntidade(obj, null, pEm, FabInfoPersistirEntidade.MERGE));

    }

    /**
     * Realizar Merg de Objeto (cria se o primario não existir, e atualiza caso
     * exista).
     *
     * *Este método irá criar um novo Entity Manager, e fechar em seguida
     *
     *
     * @param object
     * @return Objeto atualizado apos ser persistido em banco,e nulo caso ocorra
     * falha
     */
    public static <I extends ItfBeanSimples> I mergeRegistro(Object object) {
        if (object == null) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "O registro enviado para Persistencia é nulo ", null);
            return null;
        }
        return (I) SBPersistencia.getDriverFWBanco().executaAlteracaoEmBancao(new InfoPerisistirEntidade(object, null, null, FabInfoPersistirEntidade.MERGE));

    }

    /**
     *
     * Cria uma conexão. inicia uma transação Executa o merge em cada objeto
     * enviado. só efetua o commit caso todos tenham sido atualizados, a nao ser
     * que o reverterem caso de falha senja falso Neste caso, cria-se uma
     * conexão para cada update
     *
     * @param pObjetos
     * @param pReverterEmCasoDeFalha
     * @return Um array de obejtos apenas com os objetos que foram salvos, um
     * array em branco caso nenhum tenha sido salvo
     */
    public static Object[] MergeListaRegistros(Object[] pObjetos, boolean pReverterEmCasoDeFalha) {
        Object[] resp = new Object[pObjetos.length];
        int i = 0;
        for (Object registro : pObjetos) {
            resp[i] = mergeRegistro(registro);
            i++;
        }
        return resp;

    }

    public static Object[] MergeListaRegistros(Object[] pObjetos) {
        return MergeListaRegistros(pObjetos, false);

    }

    /**
     * Realizar INSERT persistindo o Objeto no Banco
     *
     * @param pObj Objeto a ser persistido em banco
     * @param pEM Entidade utilizada
     * @return retorna True se persistido e False se algum erro acontecer
     */
    public static boolean persistirRegistro(Object pObj, EntityManager pEM) {
        return (boolean) SBPersistencia.getDriverFWBanco().executaAlteracaoEmBancao(new InfoPerisistirEntidade(pObj, null, pEM, FabInfoPersistirEntidade.INSERT));
    }

    /**
     *
     * @param pObj
     * @param pEM
     * @return True caso consiga salvar a alteração, ou false caso não consiga
     * executar o insert
     */
    public static boolean persistirRegistro(Object pObj) {
        return (boolean) SBPersistencia.getDriverFWBanco().executaAlteracaoEmBancao(new InfoPerisistirEntidade(pObj, null, null, FabInfoPersistirEntidade.INSERT));
    }

    /**
     *
     * @param pObj Registro que será excluído
     * @return true se conseiguir exculir, false se não conseguir
     */
    public static boolean exluirRegistro(Object pObj) {

        return (boolean) SBPersistencia.getDriverFWBanco().executaAlteracaoEmBancao(new InfoPerisistirEntidade(pObj, null, null, FabInfoPersistirEntidade.DELETE));
    }

    /**
     *
     * @param pObj Registro que será excluído
     * @return true se conseiguir exculir, false se não conseguir
     */
    public static boolean exluirRegistro(Object pObj, EntityManager em) {
        return (boolean) SBPersistencia.getDriverFWBanco().executaAlteracaoEmBancao(new InfoPerisistirEntidade(pObj, null, em, FabInfoPersistirEntidade.DELETE));
    }

    /**
     * @param pNomeCurto String Que deseja localizar no Banco
     * @param classe Classe utilizada
     * @return Lista com registros like nomecurto
     */
    public static List getListaRegistrosLikeNomeCurto(String pNomeCurto, Class classe) {
        return selecaoRegistros(null, null, null, null, classe, TIPO_SELECAO_REGISTROS.LIKENOME, pNomeCurto);
    }

    /**
     *
     *
     * @see #getListaRegistrosLikeNomeCurto(java.lang.String, java.lang.Class)
     *
     *
     * @param pNomeCurto
     * @param pClasse
     * @param pEM
     * @return
     */
    public static List getListaRegistrosLikeNomeCurto(String pNomeCurto, Class pClasse, EntityManager pEM) {
        return selecaoRegistros(pEM, null, null, null, pClasse, TIPO_SELECAO_REGISTROS.LIKENOME, pNomeCurto);
    }

    public static <T> List<T> getListaTodos(Class<T> pClasse) {

        if (pClasse == null) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "A classe não foi especificada em getLista Todos", null);
            return null;
        }

        return (List<T>) selecaoRegistros(null, null, null, null, pClasse, TIPO_SELECAO_REGISTROS.TODOS);
    }

    public static <T> List<T> getListaTodos(Class<T> pClasse, EntityManager pEm) {

        if (pClasse == null) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "A classe não foi especificada em getListaTodos", null);
            return null;
        }

        return (List<T>) selecaoRegistros(pEm, null, null, null, pClasse, TIPO_SELECAO_REGISTROS.TODOS);
    }

    public static <T> List<T> getListaTodos(Class<T> pClasse, EntityManager pEm, int pLimite) {

        if (pClasse == null) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "A classe não foi especificada em getListaTodos", null);
            return null;
        }

        return (List<T>) selecaoRegistros(pEm, null, null, pLimite, pClasse, TIPO_SELECAO_REGISTROS.TODOS);
    }

    public static <T> List<T> getListaTodos(Class<T> pClasse, EntityManager pEm, int pLimite, int inicio) {

        if (pClasse == null) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "A classe não foi especificada em getListaTodos", null);
            return null;
        }

        return (List<T>) selecaoRegistros(pEm, null, null, pLimite, pClasse, TIPO_SELECAO_REGISTROS.TODOS);
    }

    /**
     * @param pHQl HQO (Comando no formato Hibernate query language)
     * @param pMaximo
     * @param parametros parametros
     * @return lista de registros
     */
    public static List getListaRegistrosByHQL(String pHQl, int pMaximo, Object... parametros) {
        return selecaoRegistros(null, null, pHQl, pMaximo, null, UtilSBPersistencia.TIPO_SELECAO_REGISTROS.JPQL, parametros);
    }

    /**
     *
     *
     * @param pHQl HQO (Comando no formato Hibernate query language)
     * @param pMaximo
     * @param pEM Entity manager utilizado
     * @param parametros parametros
     * @return lista de registros
     */
    public static List getListaRegistrosByHQL(String pHQl, int pMaximo, EntityManager pEM, Object... parametros) {
        return selecaoRegistros(pEM, null, pHQl, pMaximo, null, UtilSBPersistencia.TIPO_SELECAO_REGISTROS.JPQL, parametros);
    }

    /**
     * @param pHQl HQO (Comando no formato Hibernate query language)
     * @param pMaximo
     * @param parametros parametros
     * @return lista de registros
     */
    public static List getListaRegistrosBySQL(String pSQl, int pMaximo, Object... parametros) {
        return selecaoRegistros(null, pSQl, null, pMaximo, null, UtilSBPersistencia.TIPO_SELECAO_REGISTROS.SQL, parametros);
    }

    public static List getListaRegistrosBySQL(EntityManager pEm, String pSQl, int pMaximo, Object... parametros) {
        return selecaoRegistros(pEm, pSQl, null, pMaximo, null, UtilSBPersistencia.TIPO_SELECAO_REGISTROS.SQL, parametros);
    }

    /**
     *
     * @param pClasse Classe referente
     * @param parametro nome curto que será localizado
     * @return registro encontrado
     */
    public static Object getRegistroByNomeCurto(Class pClasse, String parametro) {
        return selecaoRegistro(null, null, null, pClasse, FabTipoSelecaoRegistro.NOMECURTO, parametro);
    }

    public static Object getRegistroByLikeNomeCurto(Class pClasse, String parametro) {
        return selecaoRegistro(null, null, null, pClasse, FabTipoSelecaoRegistro.LIKENOMECURTO, parametro);
    }

    public static <T> T getRegistroByLikeNomeCurto(Class<T> pClasse, String parametro, EntityManager pEm) {
        return (T) selecaoRegistro(pEm, null, null, pClasse, FabTipoSelecaoRegistro.LIKENOMECURTO, parametro);
    }

    public static Object getRegistroByNomeSlug(Class pClasse, String parametro, EntityManager pEm) {

        List<Integer> codigosEncontrados = new ArrayList<>();
        List<String> textosEncontrados = new ArrayList<>();
        if (parametro == null) {
            return null;
        }
        String[] valores = parametro.split("-");

        for (String valor : valores) {

            if (valor != null && !valor.isEmpty()) {
                if (UtilSBCoreStringValidador.isContemApenasNumero(valor)) {

                    codigosEncontrados.add(Integer.valueOf(valor));
                } else {
                    textosEncontrados.add(valor);
                }
            }
        }

        for (Integer codigo : Lists.reverse(codigosEncontrados)) {
            Object resp = selecaoRegistro(pEm, null, null, pClasse, FabTipoSelecaoRegistro.ID, codigo);
            if (resp != null) {
                return resp;
            }
        }
        for (String texto : textosEncontrados) {
            if (texto != null && !texto.isEmpty()) {
                Object valor = selecaoRegistro(pEm, null, null, pClasse, FabTipoSelecaoRegistro.LIKENOMECURTO, texto);
                if (valor != null) {
                    return valor;
                }
            }

        }

        return null;
    }

    /**
     *
     * @param pClasse Classe referente
     * @param parametro nome curto localizado
     * @param pNomeEM Entity Manager especial (diferente da entidade padrão)
     * @return registro encontrado
     */
    public static Object getRegistroByNomeCurto(Class pClasse, String parametro, EntityManager pEM) {
        return selecaoRegistro(pEM, null, null, pClasse, FabTipoSelecaoRegistro.NOMECURTO, parametro);
    }

    /**
     *
     * @param pSQL String SQL que será executado
     * @param pNomeEM Registro pelo sql
     * @return
     */
    public static Object getRegistroBySQL(String pSQL, EntityManager pEM) {
        return selecaoRegistro(pEM, pSQL, null, null, FabTipoSelecaoRegistro.SQL);
    }

    /**
     *
     * @param pSQL String SQL que será executado
     * @return
     */
    public static Object getRegistroBySQL(String pSQL) {
        return selecaoRegistro(null, pSQL, null, null, FabTipoSelecaoRegistro.SQL);
    }

    /**
     *
     * @param pSQL Stirng com JPQLsql
     * @param pNomeEM Nome do entity Manager
     * @return
     */
    public static Object getRegistroByJPQL(String pSQL, EntityManager pEM) {
        return selecaoRegistro(pEM, null, pSQL, null, FabTipoSelecaoRegistro.JPQL);
    }

    /**
     *
     * @param pSQL Sting com sql
     * @param limite limite de registros
     * @return
     */
    public static Object getRegistroByJPQL(String pSQL) {
        return selecaoRegistro(null, null, pSQL, null, FabTipoSelecaoRegistro.JPQL);
    }

    public static Object getRegistroByJPQL(String pSQL, Class pClasse) {
        return selecaoRegistro(null, null, pSQL, pClasse, FabTipoSelecaoRegistro.JPQL);
    }

    public static Object getRegistroByJPQL(String pSQL, Class pClasse, EntityManager pEM) {
        return selecaoRegistro(pEM, null, pSQL, pClasse, FabTipoSelecaoRegistro.JPQL);
    }

    /**
     *
     * @param <T>
     * @param pClasse Classe do registro
     * @param id id do registro
     * @param pEM
     * @param pNomeEM nome da entidade
     * @return registro encontrado
     */
    public static <T> T getRegistroByID(Class<T> pClasse, Long id, EntityManager pEM) {
        return (T) selecaoRegistro(pEM, null, null, pClasse, FabTipoSelecaoRegistro.ID, id);

    }

    /**
     *
     * @param pClasse Classe do registro
     * @param id id do registro
     * @param pNomeEM nome da entidade
     * @return registro encontrado
     */
    public static Object getRegistroByTipoCampoIgualA(Class pClasse, FabTipoAtributoObjeto pCampo, Object valorParametro) {
        return selecaoRegistro(null, null, null, pClasse, FabTipoSelecaoRegistro.TIPO_CAMPO_ESPECIFICO_IGUAL_A, pCampo, valorParametro);

    }

    /**
     *
     * Carrega uma entidade apartir de um Bean Simples
     *
     * -> Um objeto que ainda não foi carregado pelo hibernate possui as funções
     * referentes a lista limitadas, para casos onde o Objeto é construido fora
     * do banco de dados
     *
     * @param <I>
     * @param pBeanSimples O bean que será carregado
     * @param pEM Entity manager utlizado
     * @return
     */
    public static <I extends ItfBeanSimples> I loadEntidade(ItfBeanSimplesSomenteLeitura pBeanSimples, EntityManager pEM) {
        try {
            if (pBeanSimples == null) {
                throw new UnsupportedOperationException("Tentativa de carregar o Registro JPA enviando o valor nulo");
            }
            if (pEM == null) {
                throw new UnsupportedOperationException("O entity manager enviado para load em entidade é nulo. entidade->" + pBeanSimples);
            }
            if (pBeanSimples.getId() == null) {
                return (I) pBeanSimples;
            }
            Class classe = pBeanSimples.getClass();
            if (pBeanSimples instanceof HibernateProxy) {
                classe = UtilSBCoreReflexaoObjeto.getClassExtraindoProxy(pBeanSimples.getClass().getSimpleName());
            } else {
                String caracteres = "_$";

                if (pBeanSimples.getClass().getSimpleName().contains(caracteres)) {
                    String nomeDaClasse = pBeanSimples.getClass().getSimpleName().split("[" + java.util.regex.Pattern.quote(caracteres) + "]+")[0];
                    classe = MapaObjetosProjetoAtual.getClasseDoObjetoByNome(nomeDaClasse);
                    if (classe == null) {
                        throw new UnsupportedOperationException("A classe " + nomeDaClasse + " não foi encontrada a partir do nome" + pBeanSimples.getClass().getSimpleName());
                    }
                }
            }
            if (classe.getAnnotation(Entity.class) == null) {
                return (I) pBeanSimples;
            }
            Long id = -1l;

            try {

                id = pBeanSimples.getId();

            } catch (Throwable t) {

                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro obtendo o id do beanSimplesS", t);

            }
            return (I) selecaoRegistro(pEM, null, null, classe, FabTipoSelecaoRegistro.ID, id);
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Tentativa de carregar entidade apartide de um bean nulo", t);
        }
        return null;

    }

    /**
     *
     * Retorna o primeiro registro da tabela
     *
     * @param pClasse Classe Entity que representa a tabela
     * @param pEM
     * @return
     */
    public static <I extends ItfBeanSimples> I getRegistroByPrimeiro(Class pClasse, EntityManager pEM) {
        return (I) selecaoRegistro(pEM, null, null, pClasse, FabTipoSelecaoRegistro.PRIMEIRO_REGISTRO, null);

    }

    /**
     *
     * Localiza um único registro do tipo empresa procurando por: _____________
     * Em caso de numero: Telefones CNPJ e ID __1______________________________
     * Em caso de String pelo nome, site, e e-mail
     *
     * @param pClasse
     * @param pParametro
     * @param pEM
     * @return
     */
    public static Object getEmpresa(Class pClasse, String pParametro, EntityManager pEM) {
        return selecaoRegistro(pEM, null, null, pClasse, FabTipoSelecaoRegistro.ENCONTRAR_EMPRESA, pParametro);

    }

    public static Object getEmpresaPorCNPJ(Class pClasse, String pParametro, EntityManager pEM) {
        return selecaoRegistro(pEM, null, null, pClasse, FabTipoSelecaoRegistro.ENCONTRAR_EMPRESA_POR_CNPJ, pParametro);

    }

    public static List getEmpresas(Class pClasse, String pParametro, EntityManager pEM) {
        boolean isNumerico = false;

        if (UtilSBCoreStringValidador.isContemApenasNumero(pParametro)) {
            List resposta = new ArrayList();
            Object empresa = UtilSBPersistencia.getRegistroByID(pClasse, Long.parseLong(pParametro), pEM);
            if (empresa != null) {
                resposta.add(empresa);
            }
            return resposta;
        } else {
            return UtilSBPersistencia.getListaRegistrosLikeNomeCurto(pParametro, pClasse, pEM);
        }

    }

    public static Object getPessoa(Class pClasse, String pParametro, EntityManager pEM) {
        throw new UnsupportedOperationException("Ainda não implementado");

    }

    public static List<Object> getPessoas(Class pClasse, String pParametro, EntityManager pEM) {
        throw new UnsupportedOperationException("Ainda não implementado");

    }

    /**
     *
     *
     * @param pClasse Classe do registro
     * @param id id do registro
     * @return regustro encontrado
     */
    public static Object getRegistroByID(Class pClasse, Long id) {
        return selecaoRegistro(null, null, null, pClasse, FabTipoSelecaoRegistro.ID, id);
    }

    public static Class<?> getEntityByTag(String pTag) {
        EntityManager em = UtilSBPersistencia.getNovoEM();
        //((Dados) BeansUtil.getAppBean("dados")).getEm();
        Set<EntityType<?>> lista = em.getMetamodel().getEntities();
        for (EntityType<?> teste : lista) {
            System.out.println(teste.getJavaType().toString());
            Class<?> classe = teste.getJavaType();
            System.out.println(teste.getClass().getName());
            return classe;
        }
        em.close();
        return null;
    }

    public static List<Class> getTodasEntidades() {
        EntityManager em = UtilSBPersistencia.getNovoEM();
        //((Dados) BeansUtil.getAppBean("dados")).getEm();
        Set<EntityType<?>> lista = em.getMetamodel().getEntities();
        List<Class> entidades = new ArrayList<>();
        for (EntityType<?> entidade : lista) {
            System.out.println(entidade.getJavaType().toString());
            Class<?> classe = entidade.getJavaType();
            System.out.println(entidade.getClass().getName());
            entidades.add(entidade.getJavaType());
        }
        em.close();
        return entidades;
    }

    public static Class getEntidadeByNomeClasse(String nomeEntidade) {
        return getEntidadeByNomeClasse(nomeEntidade, getNovoEM());
    }

    public static Class getEntidadeByNomeClasse(String nomeEntidade, EntityManager pEm) {
        if (nomeEntidade == null) {
            throw new UnsupportedOperationException("Erro tentativa de obter entidade com nome com parametro nulo");
        }

        //((Dados) BeansUtil.getAppBean("dados")).getEm();
        Set<EntityType<?>> lista = pEm.getMetamodel().getEntities();
        for (EntityType<?> entidade : lista) {
            Class<?> classe = entidade.getJavaType();
            if (classe.getSimpleName().equals(nomeEntidade)) {
                return classe;
            }
        }
        pEm.close();
        return null;
    }

    public static List<Class> getTodasEntidades(String nomePersistenceUnit) {
        EntityManager em = UtilSBPersistencia.getNovoEM(nomePersistenceUnit);
        //((Dados) BeansUtil.getAppBean("dados")).getEm();
        Set<EntityType<?>> lista = em.getMetamodel().getEntities();
        List<Class> entidades = new ArrayList<>();
        for (EntityType<?> entidade : lista) {
            System.out.println(entidade.getJavaType().toString());
            Class<?> classe = entidade.getJavaType();
            System.out.println(entidade.getClass().getName());
            entidades.add(entidade.getJavaType());
        }
        em.close();
        return entidades;
    }

    public static List<?> getListaBySBNQ(SBNQ pSBNQ) {
        return pSBNQ.getQueryHibernate().getResultList();

    }

    private static boolean executarSQLComGestaoEntidade(String pSQl) {
        EntityManager entityManager = UtilSBPersistencia.getEMPadraoNovo();
        try {

            entityManager.getTransaction().begin();

            Query q = entityManager.createNativeQuery(pSQl);
            int resgistrosAlterados = q.executeUpdate();

            return UtilSBPersistencia.finzalizaTransacaoEFechaEM(entityManager);
        } //catch (Op e) {
        //      SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro executando comando SQL" + pSQl, e);
        //      return false;
        //  }
        catch (OptimisticLockException esperavaumRegsitro) {
            return false;
        } catch (Exception e) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro executando comando SQL" + pSQl, e);
            return false;
        } finally {
            UtilSBPersistencia.fecharEM(entityManager);
        }
    }

    private static boolean executarSQLComGestaoTerceirizada(EntityManager entityManager, String pSQl) {
        try {

            Query q = entityManager.createNativeQuery(pSQl);
            int resgistrosAlterados = q.executeUpdate();

            return true;
        } //catch (Op e) {
        //      SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro executando comando SQL" + pSQl, e);
        //      return false;
        //  }
        catch (OptimisticLockException esperavaumRegsitro) {
            return false;
        } catch (Exception e) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro executando comando SQL" + pSQl, e);
            return false;
        }
    }

    private static boolean executaSQLcmd(EntityManager pEm, String pSQl) {

        boolean entityManagerEnviado = pEm != null;
        if (!SBCore.isEmModoProducao()) {
            System.out.println("Executando: \n " + pSQl);
        }
        if (entityManagerEnviado) {
            return executarSQLComGestaoTerceirizada(pEm, pSQl);
        } else {
            return executarSQLComGestaoEntidade(pSQl);
        }

    }

    public static boolean executaSQL(String pSql) {
        return executarSQLComGestaoEntidade(pSql);
    }

    public static boolean executaSQL(EntityManager pEm, String pSql) {
        return executaSQLcmd(pEm, pSql);
    }

    public static Long getQuantidadeRegistrosNaTabela(Class pClasse) {
        return (Long) selecaoRegistro(null, null, null, pClasse, FabTipoSelecaoRegistro.QUANTIDADE_REGISTROS, null);
    }

    public static Long getQuantidadeRegistrosNaTabela(Class pClasse, EntityManager pEM) {
        return (Long) selecaoRegistro(pEM, null, null, pClasse, FabTipoSelecaoRegistro.QUANTIDADE_REGISTROS, null);
    }

    public static Object superMerge(ItfBeanSimples pEntidade, EntityManager em) {

        throw new UnsupportedOperationException();

    }

    public static void isEntidadeFoiCarregada(ItfBeanSimples entidade, EntityManager pEm) {
        PersistenceUnitUtil unitUtil = pEm.getEntityManagerFactory().getPersistenceUnitUtil();

        unitUtil.isLoaded(entidade);

    }

    public static ConsultaDinamicaDeEntidade gerarConsultaDeEntidade(Class pEntidade) {
        return new ConsultaDinamicaDeEntidade(pEntidade);
    }

    public static ConsultaDinamicaDeEntidade gerarConsultaDeEntidade(Class pEntidade, EntityManager em) {
        return new ConsultaDinamicaDeEntidade(pEntidade, em);
    }

    public EntityManager getEntityManagerDoContexto() {
        return SBCore.getCentralDados().getAcessoDadosDoContexto().getEntitiManager();
    }

}
