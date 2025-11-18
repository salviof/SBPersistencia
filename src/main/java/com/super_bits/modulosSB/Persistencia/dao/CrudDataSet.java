package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.Mensagens.FabMensagens;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * Clasee para seleção e navegação e CRUD de registros
 *
 * @author Salvio
 * @param <T>
 */
public class CrudDataSet<T extends ComoEntidadeSimples> extends DaoGenerico<T> implements ItfCRUDDataSet<T> {

    public static enum TIPO_DADOS {

        SBNQ, SQL, PSQL
    }

    /**
     * CADASTRANDO (ESTADO QUANDO ESTÁ NO MODO CRIAÇÃO DE NOVO REGISTRO)
     * ATUALIZANDO (PERMITE SALVAR AS ALTERAÇÕES (ESTE MODO É O PADRÃO)
     * VISUALIZANDO (AS ALTERAÇÕES FICAM BLOQUEADAS)
     *
     */
    public static enum ESTADO_DATASET {

        CADASTRANDO, ATUALIZANDO, VISUALIZANDO
    }

    private T registro;
    private Integer indexRegistroAtual;
    private List<T> registros;
    private SBNQ queryInfo;
    private Boolean dadosInicializados;
    private UtilSBPersistencia.TIPO_SELECAO_REGISTROS tipoDados;
    private EntityManager em;
    private Object parametros;
    private String sql;
    private Boolean bloquearAlteracoes;
    private ESTADO_DATASET estadoDataSet;

    public void bloquearAlterações() {
        bloquearAlteracoes = true;
        estadoDataSet = ESTADO_DATASET.VISUALIZANDO;
    }

    public void desbloquearAlterações() {
        bloquearAlteracoes = false;
        estadoDataSet = ESTADO_DATASET.VISUALIZANDO;
    }

    private synchronized void carregarDadosIniciais() {

        if (!dadosInicializados) {
            System.out.println("DADOS INICIALIZADOS=VERDADEIRO!!!");
            indexRegistroAtual = 0;
            dadosInicializados = true;

            refesh();

        }

    }

    public CrudDataSet(SBNQ pQueryInfo, Class<T> classe) {

        super(classe);
        dadosInicializados = false;
        bloquearAlteracoes = false;
        tipoDados = UtilSBPersistencia.TIPO_SELECAO_REGISTROS.SBNQ;
        queryInfo = pQueryInfo;
        indexRegistroAtual = 0;
        registros = new ArrayList<>();

    }

    public CrudDataSet(Class<T> classe) {
        super(classe);
        bloquearAlteracoes = false;
        dadosInicializados = false;
        indexRegistroAtual = 0;
        tipoDados = UtilSBPersistencia.TIPO_SELECAO_REGISTROS.TODOS;
        registros = new ArrayList<>();
    }

    public CrudDataSet(Class<T> classe, EntityManager pEM) {
        super(classe);
        bloquearAlteracoes = false;
        dadosInicializados = false;
        indexRegistroAtual = 0;
        tipoDados = UtilSBPersistencia.TIPO_SELECAO_REGISTROS.TODOS;
        registros = new ArrayList<>();
        em = pEM;
        System.out.println("isOpem? on init" + em.isOpen());
    }

    public CrudDataSet(Class<T> classe, UtilSBPersistencia.TIPO_SELECAO_REGISTROS pTipoDAdos, String pSQL, Object... pParametros) {
        super(classe);
        bloquearAlteracoes = false;
        dadosInicializados = false;
        registros = new ArrayList<>();
        tipoDados = pTipoDAdos;
        indexRegistroAtual = 0;
        parametros = pParametros;
        sql = pSQL;

    }

    @Override
    public void delete() {
        UtilSBPersistencia.exluirRegistro(registro);
    }

    @Override
    public void proximo() {
        carregarDadosIniciais();
        indexRegistroAtual++;

        if (indexRegistroAtual > registros.size() - 1) {
            SBCore.enviarAvisoAoUsuario("Não existe um próximo registro");

        }
        configRegistroAtual();

    }

    @Override
    public void anterior() {

        carregarDadosIniciais();
        indexRegistroAtual--;
        if (indexRegistroAtual < 0) {
            SBCore.enviarMensagemUsuario("Não existe um registro anterior a este", FabMensagens.AVISO);
            indexRegistroAtual = 0;
        }

        configRegistroAtual();
    }

    private void configRegistroAtual() {
        System.out.println("configurando registro atual, foram selecionados" + registros.size());
        if (getRegistros().size() > 0) {

            setRegistro(getRegistros().get(getIndexRegistroAtual()));
            if (bloquearAlteracoes) {
                estadoDataSet = ESTADO_DATASET.VISUALIZANDO;
            } else {
                estadoDataSet = ESTADO_DATASET.ATUALIZANDO;
            }

        } else {
            System.out.println("NENHUM REGISTRO PARA CONFIGURAR O REGISTRO ATUAL, SETANDO NOVO CADASTRO");
            novo();
        }

    }

    @Override
    public void refesh() {
        // if (em == null) {
        //       em = UtilSBPersistencia.getNovoEM();
        //  }
        //    em.close();

        switch (tipoDados) {
            case SQL:
                setRegistros((List) UtilSBPersistencia.getListaRegistrosBySQL(sql, -1, parametros));
                break;
            case LIKENOME:
                registros = (List) UtilSBPersistencia.getListaRegistrosLikeNomeCurto(sql, classe, em);
                break;
            case SBNQ:
                registros = (List) achaItensPorSBNQ(queryInfo.getSBNQ(), parametros);
                break;
            case JPQL:
                registros = (List) UtilSBPersistencia.getListaRegistrosByHQL(sql, -1, em, parametros);
                break;

            default:
                tipoDados = UtilSBPersistencia.TIPO_SELECAO_REGISTROS.TODOS;
                registros = (List) UtilSBPersistencia.getListaTodos(classe, em);
                break;
        }
        configRegistroAtual();

    }

    @Override
    public T getRegistro() {
        carregarDadosIniciais();
        return registro;
    }

    @Override
    public void setRegistro(T registro) {
        this.registro = registro;
    }

    @Override
    public int getIndexRegistroAtual() {
        if (indexRegistroAtual == null) {
            indexRegistroAtual = 0;
        }
        return indexRegistroAtual;
    }

    @Override
    public void setIndexRegistroAtual(int indexRegistroAtual) {
        this.indexRegistroAtual = indexRegistroAtual;
    }

    @Override
    public void salvar() {

        switch (estadoDataSet) {
            case CADASTRANDO:
                UtilSBPersistencia.persistirRegistro(registro, em);
                break;
            case ATUALIZANDO:
                UtilSBPersistencia.mergeRegistro(registro, em);
                break;
            case VISUALIZANDO:
                SBCore.enviarMensagemUsuario("A alteração de cadastro deste formulário está bloqueada, desbloqueie e tente novamente.", FabMensagens.AVISO);
                break;
        }

    }

    @Override
    public void novo() {
        carregarDadosIniciais();
        try {
            registro = null;
            T teste = (T) classe.newInstance();
            registro = teste;
            estadoDataSet = ESTADO_DATASET.CADASTRANDO;
        } catch (InstantiationException | IllegalAccessException e) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro gerando nova instancia de objeto em DaoGenerico", e);
            SBCore.enviarMensagemUsuario("Erro iniciando novo REgistro" + e.getMessage(), FabMensagens.ERRO);
        }
    }

    @Override
    public int getQuantidade() {
        return registros.size();
    }

    @Override
    public List<T> getRegistros() {
        carregarDadosIniciais();
        return registros;
    }

    public void setRegistros(List<T> registros) {
        this.registros = registros;

    }

    public void setEm(EntityManager em) {
        this.em = em;
    }

}
