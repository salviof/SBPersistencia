package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.Persistencia.ConfigGeral.CSBNQ;

import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCStringValidador;
import java.io.Serializable;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.Query;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 */
public class SBNQ implements Serializable {

    private static final long serialVersionUID = -228553803654616046L;

    UtilSBPersistencia dados;

    public static enum TipoObj {

        ENTIDADE, SBQUERY
    }

    private TipoObj tipolista = TipoObj.ENTIDADE;
    private CSBNQ.Qr SBNQ;
    private Object[] valoresParametros;
    private Map<String, Object> parametros;
    private String sql;
    private String[] advertencias;
    private Class<?> tipoentidade;
    private SBQuery<?> sbQuery;
    private boolean minMedMaximo = false;
    private Query queryHibernate;
    private EntityManager em;

    private boolean houveAlteracoes = false;

    public static enum valida {

        ok, advertencia, impedimento
    };

    public void criaParametrosFicticios() {

    }

    private void aplicaValoresParametro() {
        /**
         * // int qtdParametrosEnvidos = getQuantidadeParametros(); Query
         * queryTemp = getQueryHibernate(); int qtdParametrosEncontrados =
         * queryTemp.getParameters().size();
         *
         * for (Parameter<?> pr : queryTemp.getParameters()) {
         *
         * Class<?> tipoParametro = pr.getParameterType(); String nomeParametro
         * = pr.getName(); Integer posicao = pr.getPosition();
         *
         * if (temParamInfo()) { queryTemp.getParameter(pr.getName()); try {
         *
         * queryTemp.setParameter(pr.getName(), parametros.get(pr.getName()));
         *
         * } catch (Exception e) {
         * SBCore.RelatarErro(FabErro.SOLICITAR_REPARO,"erro adcionando parametro
         * inc " + "erro adcionando parametro inc ", e);
         *
         * }
         *
         * } else { try { queryTemp.setParameter(pr.getPosition(),
         * valoresParametros[pr.getPosition() - 1]);
         *
         * } catch (Exception e) {
         *
         * SBCore.RelatarErro(FabErro.SOLICITAR_REPARO,"erro adcionando parametro
         * simples", e); //CentralDeMensagens.erroSQL( //	"erro adcionando
         * parametro simples" //	+ e.getMessage(), this,e); }
         *
         * }
         *
         * }
         */

    }

    private void makeQueryHibernate() {
        if (queryHibernate == null) {
            try {
                String strSql = CSBNQ.getSQL(SBNQ);
                queryHibernate = em.createQuery(strSql);
            } catch (Exception e) {
                //  SBCore.RelatarErro(FabErro.SOLICITAR_REPARO,"\"erro Criando HSQL em SBNQ.makeQueryHibernate", e);

                return;
            }
        }

        aplicaValoresParametro();

    }

    public int getQuantidadeParametros() {
        if (valoresParametros != null) {
            return valoresParametros.length;
        } else if (parametros != null) {
            return parametros.size();
        } else {
            return 0;
        }
    }

    private static String[] validaSQL() {
        // TODO Validação do SQLINFO

        return null;
    }

    public void addValorParametro(Object param) {
        if (valoresParametros == null) {
            Object[] nparametros = new Object[1];
            nparametros[0] = param;
            setValoresParametros(nparametros);
        } else {
            Object[] nparametros = new Object[valoresParametros.length + 1];
            int i = 0;
            for (Object pr : valoresParametros) {
                nparametros[i] = pr;
                i++;
            }
            nparametros[valoresParametros.length] = param;
            setValoresParametros(nparametros);

        }

    }

    public boolean semparametros() {
        if (valoresParametros == null && parametros == null) {
            return true;
        } else {
            return false;
        }

    }

    public boolean temParamInfo() {
        if (parametros == null) {
            return false;
        } else {
            return true;
        }
    }

    private void construtorComum() {
        em = UtilSBPersistencia.getNovoEM();
    }

    public SBNQ(SBQuery pQr) {
        tipolista = TipoObj.SBQUERY;
        setSbQuery(pQr);
        construtorComum();
    }

    public SBNQ(SBQuery pQr, boolean pRetornarMinMedMax) {
        tipolista = TipoObj.SBQUERY;
        minMedMaximo = pRetornarMinMedMax;
        setMinMedMaximo(pRetornarMinMedMax);
        setSbQuery(pQr);
        construtorComum();
    }

    public SBNQ(CSBNQ.Qr pSBNQ) {

        setSBNQ(pSBNQ);
        setSql(CSBNQ.getSQL(getSBNQ()));
        construtorComum();
    }

    public SBNQ(CSBNQ.Qr pSBNQ, Object pValorParametro) {
        Object[] nparametros = new Object[1];
        nparametros[0] = pValorParametro;

        setValoresParametros(nparametros);
        setSBNQ(pSBNQ);
        setSql(CSBNQ.getSQL(getSBNQ()));
        construtorComum();
    }

    public SBNQ(CSBNQ.Qr pSBNQ, Object[] pValoresParametros) {
        setValoresParametros(pValoresParametros);
        setSBNQ(pSBNQ);
        setSql(CSBNQ.getSQL(getSBNQ()));
        construtorComum();
    }

    public SBNQ(CSBNQ.Qr pSBNQ, Map<String, Object> pParametros) {
        setParametros(pParametros);
        setSBNQ(pSBNQ);
        setSql(CSBNQ.getSQL(getSBNQ()));
        construtorComum();
    }

    public SBNQ(String pSql, Map<String, Object> pParametros) {
        setParametros(pParametros);
        setSql(pSql);
        construtorComum();
    }

    public SBNQ(String pSql) {
        setSql(pSql);
        construtorComum();
    }

    public SBNQ(String pSql, Object[] pValoresParametros) {
        setValoresParametros(pValoresParametros);
        setSql(pSql);
        construtorComum();
    }

    public boolean isParametrosOK() {
        // TODO Validar a configuração dos parametros
        return true;
    }

    public Object[] getValoresParametros() {
        return valoresParametros;
    }

    public void setValoresParametros(Object[] pValoresParametros) {
        valoresParametros = pValoresParametros;
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public String getSql() {
        if (temParamInfo()) {
            return sql;
        } else {
            return UtilCRCStringValidador.substituiParametrosNomeadosPorInterroga(sql);
        }
    }

    private void setSql(String sql) {
        this.sql = sql;
    }

    public String[] getAdvertencias() {
        return advertencias;
    }

    public TipoObj getTipolista() {
        return tipolista;
    }

    public void setTipolista(TipoObj tipolista) {
        this.tipolista = tipolista;
    }

    public Class<?> getTipoentidade() {
        return tipoentidade;
    }

    public SBQuery getSbQuery() {
        return sbQuery;
    }

    public void setSbQuery(SBQuery sbQuery) {
        this.sbQuery = sbQuery;
    }

    public boolean isMinMedMaximo() {
        return minMedMaximo;
    }

    public void setMinMedMaximo(boolean minMedMaximo) {
        this.minMedMaximo = minMedMaximo;
    }

    public String getNQ() {
        return getSBNQ().toString();
    }

    public CSBNQ.Qr getSBNQ() {
        return SBNQ;
    }

    private void setSBNQ(CSBNQ.Qr sBNQ) {
        SBNQ = sBNQ;
    }

    public Query getQueryHibernate() {

        if (houveAlteracoes & queryHibernate != null) {
            makeQueryHibernate();
        }
        if (queryHibernate == null) {
            makeQueryHibernate();
        }
        return queryHibernate;

    }

}
