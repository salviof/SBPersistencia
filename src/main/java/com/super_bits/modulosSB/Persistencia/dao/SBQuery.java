package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

public abstract class SBQuery<T> {

    /// observação importante sobre nomeclatura: ÇsubistituiÇ (subistitui) e "_" substitui "."
    private UtilSBPersistencia dados;

    protected abstract String defineSql();

    protected abstract Map<String, Object> defineParametros();
    private Boolean temParametros = false;
    private Map<String, Object> parametros = defineParametros();
    private boolean minimoMediaMaximo = false;

    private void construtorComum() {
        if (parametros != null) {
            temParametros = true;
        }
    }

    public SBQuery() {
        construtorComum();
    }

    public Map<String, Object> getParametros() {
        return parametros;
    }

    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }

    public Boolean getTemParametros() {
        return temParametros;
    }

    public void setTemParametros(Boolean temParametros) {
        this.temParametros = temParametros;
    }

    private String nomeCampoEmSQL(String pNomecampo) {

        int i = 0;
        String resposta = "";
        for (String parte : pNomecampo.split("Ç")) {
            if (i == 0) {
                resposta = parte;
            } else if ((i % 2) == 0) {
                resposta = resposta + ")" + parte;
            } else {
                resposta = resposta + "(" + parte;
            }
            i++;
        }

        if (pNomecampo.charAt(pNomecampo.length() - 1) == 'Ç') {
            resposta = resposta + ")";
        }
        resposta = resposta.replace("_", ".");
        return resposta;
    }

    private String[] getCamposSQL() {
        Class<?> classe = this.getClass();

        Field[] camposReflexao = classe.getDeclaredFields();
        String[] resposta = new String[camposReflexao.length];
        int i = 0;
        for (Field cp : camposReflexao) {
            resposta[i] = nomeCampoEmSQL(cp.getName());
            i++;
        }
        return resposta;
    }

    private String makeSQL() {
        String sql = " select ";
        String[] campos = getCamposSQL();
        int i = 0;
        for (String cp : campos) {
            if (i > 0) {
                sql = sql + " , ";
            }
            sql = sql + cp;
            i++;
        }
        sql = sql + defineSql();

        return sql;
    }

    public void setValor(Object pObj, int pOrdem) {

        Class<?> classe = this.getClass();
        Object[] teste = this.getClass().getTypeParameters();

        Field[] camposReflexao = classe.getDeclaredFields();
        int i = 0;
        for (Field cp : camposReflexao) {
            cp.setAccessible(true);
            if (i == pOrdem) {
                try {
                    cp.set(this, pObj);
                } catch (IllegalArgumentException e) {

                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "erro ao tentar Injetar valor em Query" + this.getClass().getName(), e);

                } catch (IllegalAccessException e) {

                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "erro ao tentar Injetar valor em Query" + this.getClass().getName(), e);

                }
            }
            i++;
        }

    }

    private List<? extends T> executaQuery(String pSql) {
        EntityManager em;

        em = UtilSBPersistencia.getNovoEM();

        Query qr = em.createQuery(pSql);
        Iterator<?> respostaBanco = qr.getResultList().iterator();
        List<T> lista = new ArrayList<T>();
        while (respostaBanco.hasNext()) {
            Object[] objetos = (Object[]) respostaBanco.next();

            try {
                @SuppressWarnings("unchecked")
                T novoRegistro = (T) this.getClass().newInstance();
                int i = 0;
                for (Object campo : objetos) {
                    ((SBQuery<?>) novoRegistro).setValor(campo, i);
                    i++;
                }
                lista.add(novoRegistro);

            } catch (InstantiationException e) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Classe do tipo SBQuery malformada" + this.getClass().getName(), e);

            } catch (IllegalAccessException e) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, " Classe do tipo SBQuery malformada" + this.getClass().getName(), e);

            }
        }
        em.close();
        return lista;
    }

    protected String getStrMenMedMAx() {
        String sql = makeSQL();
        String[] variaveis = {"min(", "MIN(", "AVG(", "avg(", "MAX(", "max("};
        for (String parte : variaveis) {
            if (sql.contains(parte)) {
                return parte.replace("(", "");
            }
        }

        return null;
    }

    public boolean temMinMedMaxNoSql() {
        if (getStrMenMedMAx() == null) {
            return false;
        } else {
            return true;
        }
    }

    private String makeSqlMinMedMax(String pTipo) {

        if (!temMinMedMaxNoSql()) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Query não contem Minimo Maximo e Médio" + this.getClass().getSimpleName(), null);

        }
        String sql = makeSQL();
        return sql.replace(getStrMenMedMAx(), pTipo);

    }

    public List<? extends T> getMinimo() {

        return executaQuery(makeSqlMinMedMax("min"));
    }

    public List<? extends T> getMedia() {
        return executaQuery(makeSqlMinMedMax("avg"));
    }

    public List<? extends T> getMaximo() {
        return executaQuery(makeSqlMinMedMax("max"));
    }

    public List<? extends T> retorno() {
        return executaQuery(makeSQL());

    }

    public boolean isMinimoMediaMaximo() {
        return minimoMediaMaximo;
    }

    public void setMinimoMediaMaximo(boolean minimoMediaMaximo) {
        this.minimoMediaMaximo = minimoMediaMaximo;
    }

}
