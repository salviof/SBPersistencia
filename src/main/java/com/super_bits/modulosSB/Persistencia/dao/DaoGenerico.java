package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.Persistencia.ConfigGeral.CSBNQ;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.ClasseTipada;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoEntidadeSimples;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 * O DAO GENERICO É UMA CLASSE TIPADA PARA OBTER UMA LISTA OU UMA ÚNICA ENTIDADE
 * DE BANCO DE DADOS, COM AS FUNÇÕES MAIS GENÉRICAS
 *
 *
 * @author Sálvio Furbino <salviof@gmail.com>
 * @since 19/07/2013
 *
 * @param <T> Classe Extendendo EntidadeSimples
 */
public class DaoGenerico<T extends ComoEntidadeSimples> extends ClasseTipada implements Serializable {

    private String nomeTabela;
    private EntityManager em;

    private void construtorComum() {

        setNomeTabela(classe.getName());
    }

    private EntityManager getEM() {
        if (em != null) {
            return em;
        } else {
            em = UtilSBPersistencia.getNovoEM();
            return em;
        }
    }

    /**
     * @param pClasse Classe que representa a Entidade na qual os dados serão
     * retornados
     */
    public DaoGenerico(Class<?> pClasse) {
        super(pClasse);
        construtorComum();
    }

    /**
     * @param pClasse Classe que representa o tipo que será recebido
     * @param pEntitiManager nome do EM especial (diferente do padrão)
     */
    public DaoGenerico(Class<?> pClasse, String pEntitiManager) {
        super(pClasse);
        em = UtilSBPersistencia.getNovoEM(pEntitiManager);
        construtorComum();

    }

    /**
     * Construtor que existe apenas por compatibilidade com o validador do cdi
     * NUNCA deve ser chamado gera exceção PARA TUDO
     */
    public DaoGenerico() {
        SBCore.RelatarErro(FabErro.PARA_TUDO, "Iniciou um Dao sem construtor", null);

    }

    /**
     *
     * @param entity Entidade que será persistida
     * @return Verdadeiro se conseguir salvar
     */
    public ComoEntidadeSimples savarRegistro(T entity) {
        return UtilSBPersistencia.persistirRegistro(entity, em);
    }

    /**
     *
     * @param entity Entidade que será EXCUIDA
     * @return Veradeiro se conseguir exluir
     */
    public boolean deletarRegistro(T entity) {
        T registroRemovivel = (T) UtilSBPersistencia.mergeRegistro(entity, em);
        return UtilSBPersistencia.exluirRegistro(registroRemovivel);
    }

    /**
     * Atualiza o registro, utiliza o métdo MERG (Se não existir cria um novo)
     *
     * @param entity Entidade com novos valores para atualização
     * @return A nova entidade Atualizada
     */
    public T atualizarRegistro(T entity) {
        return (T) UtilSBPersistencia.mergeRegistro(entity);
    }

    /**
     * Localiza registro por ID
     *
     * @param entityID
     * @return Entidade Localizada, nulo se não encontrar
     */
    public T getRegistroByID(int entityID) {
        return (T) getEM().find(classe, entityID);
    }

    /**
     * Localiza registro por id modo Lasy
     *
     * @param entityID
     * @return a entidade localizada, nulo se não encontrar
     */
    public T getRegistroByIDModoLasy(int entityID) {
        return (T) getEM().getReference(classe, entityID);
    }

    /**
     *
     * @return Todos os registros, da tabela um array sem registros se não achar
     * nada
     */
    public List<T> todos() {

        CriteriaQuery query = getEM().getCriteriaBuilder().createQuery(classe);
        query.from(classe);
        List<T> lista = getEM().createQuery(query).getResultList();
        return lista;
    }

    public List<? extends T> getTodosSubclasse() {

        CriteriaQuery query = getEM().getCriteriaBuilder().createQuery(classe);
        query.from(classe);
        List<? extends T> lista = getEM().createQuery(query).getResultList();
        return lista;
    }

    /**
     *
     * @param parametro
     * @return
     */
    public T getRegistroPeloNomeCurto(String parametro) {
        T registro = (T) UtilSBPersistencia.getRegistroByNomeCurto(classe, parametro, em);
        return registro;
    }

    /**
     *
     * @param parametro
     * @return
     */
    public List<T> getListaLikeNomeCurto(String parametro) {

        List<T> registros = (List<T>) UtilSBPersistencia.getListaRegistrosLikeNomeCurto(parametro, classe, em);
        return registros;
    }

    public List<? extends T> achaItensPorSBNQ(SBNQ pSBQuery) {

        Query query = pSBQuery.getQueryHibernate();
        try {

            if (query != null) {
                @SuppressWarnings("unchecked")
                List<T> resultado = query.getResultList();
                if (resultado == null) {
                    return new ArrayList<>();
                }
                return resultado;
            }

            return new ArrayList<>();
        } catch (Exception e) {

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "erro em DaoGenerico.achaItens por SBNQ executando: query.getResultList ", e);
            return new ArrayList<>();
        }
    }

    /**
     *
     * @param pSBNQ
     * @param parametros
     * @return
     */
    public List<T> achaItensPorSBNQ(CSBNQ.Qr pSBNQ, Object... parametros) {
        SBNQ qr = new SBNQ(pSBNQ, parametros);
        return (List<T>) achaItensPorSBNQ(qr);
    }

    /**
     *
     * @param pQryInfo
     * @return
     */
    public T achaItemPorSBNQ(SBNQ pQryInfo) {

        try {

            Query query = pQryInfo.getQueryHibernate();
            query.setMaxResults(1);

            T resultado = (T) query.getSingleResult();
            return resultado;
        } catch (Exception e) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "erro Executando SBNQ", e);
            return null;
        }
    }

    public String getNomeTabela() {
        return nomeTabela;
    }

    private void setNomeTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }

    public Object getRegistroBySQL(String pSql) {
        return (List<T>) UtilSBPersistencia.getRegistroByJPQL(pSql, em);

    }

    /**
     *
     * @param pSql String contendo o SQL
     * @param pLimite limite de registros null -1 e -0 indica infinito
     * @return
     */
    public List<T> getListaRegistrosByHSQL(String pSql, int pLimite) {
        return (List<T>) UtilSBPersistencia.getListaRegistrosByHQL(pSql, pLimite, em);

    }

    /**
     * Executa a query recebendo um int maximo e parametros a serem inseridos,
     * -1 no marametro maximo indica infinito
     *
     * @param pSql String contendo o comando no formato PJQL
     * @param maximo Maximos de registros recebidos -1 0 e null indica infinito
     * @param parametros Parametros a serem aplicados
     * @return Lista com o resultado
     */
    public List<T> getListaRegistrosByHSQL(String pSql, int maximo, Object... parametros) {
        return (List<T>) UtilSBPersistencia.getListaRegistrosByHQL(pSql, maximo, em, parametros);
    }

}
