/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao.consultaDinamica;

import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreReflexao;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringValidador;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaDeEntidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.objetos.estrutura.ItfLigacaoMuitosParaUm;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import java.util.Optional;

/**
 *
 * @author desenvolvedor
 */
public class CondicaoConsulta {

    private final ConsultaDinamicaDeEntidade consulta;
    private final FabTipoCondicaoJPQL tipoCondicao;
    private Object valorParametro;
    private String caminhoCampoValorParametro;
    private String caminhoCampoCondicao;
    private String nomeParametro;

    public CondicaoConsulta(ConsultaDinamicaDeEntidade pConsulta, FabTipoCondicaoJPQL pTipoCondicao) {
        consulta = pConsulta;
        tipoCondicao = pTipoCondicao;
    }

    public ConsultaDinamicaDeEntidade getConsulta() {
        return consulta;
    }

    public FabTipoCondicaoJPQL getTipoCondicao() {
        return tipoCondicao;
    }

    public Object getValorParametro() {
        return valorParametro;
    }

    public void setValorParametro(Object pValorParametro) {
        if (pValorParametro == null) {
            valorParametro = null;
            return;
        }
        switch (tipoCondicao) {
            case MANY_TO_ONE_IGUAL_AUTO:

                if (!(pValorParametro instanceof ItfBeanSimples)) {
                    throw new UnsupportedOperationException();
                }
                EstruturaDeEntidade est = MapaObjetosProjetoAtual.getEstruturaObjeto(getConsulta().getEntidadePrincipal());
                ItfBeanSimples beanParametro = (ItfBeanSimples) pValorParametro;
                if (UtilSBCoreStringValidador.isNuloOuEmbranco(caminhoCampoCondicao)) {
                    Optional<ItfLigacaoMuitosParaUm> pesquisa = est
                            .getMuitosParaUm()
                            .stream()
                            .filter((relacao)
                                    -> (UtilSBCoreReflexao.isClasseIgualOuExetende(beanParametro.getClass(),
                                    relacao.getClasseObjetoVinculado())))
                            .findFirst();
                    if (pesquisa.isPresent()) {
                        setCaminhoCampoCondicao(pesquisa.get().getNomeDeclarado());
                    } else {
                        throw new UnsupportedOperationException("Não foi possível encontrar um manyToone do tipo  " + est.getNomeEntidade() + " você precisa declarar o caminho do campo para executar esta consulta.");
                    }

                }

            case MANY_TO_ONE_NESTE_INTERVALO:
                break;
            case CAMINHO_CAMPO_IGUAL_VALOR:
                break;
            case VALOR_POSITIVO:
                break;
            default:
                throw new AssertionError(tipoCondicao.name());

        }
        this.valorParametro = pValorParametro;
    }

    public String getCaminhoCampoValorParametro() {
        return caminhoCampoValorParametro;
    }

    public void setCaminhoCampoValorParametro(String caminhoCampoValorParametro) {
        this.caminhoCampoValorParametro = caminhoCampoValorParametro;
    }

    public String getCaminhoCampoCondicao() {
        return caminhoCampoCondicao;
    }

    public String getNomeParametro() {
        if (nomeParametro == null) {
            if (!UtilSBCoreStringValidador.isNuloOuEmbranco(getCaminhoCampoCondicao())) {
                nomeParametro = getCaminhoCampoCondicao().replace(".", "");
            }
        }
        return nomeParametro;
    }

    public void setNomeParametro(String pNomeParametro) {
        if (!UtilSBCoreStringValidador.isNuloOuEmbranco(pNomeParametro)) {
            pNomeParametro = pNomeParametro.replace(".", "");
        }
        this.nomeParametro = pNomeParametro;
    }

    public void setCaminhoCampoCondicao(String pCaminhoCampoCondicao) {
        this.caminhoCampoCondicao = pCaminhoCampoCondicao;
        if (!UtilSBCoreStringValidador.isNuloOuEmbranco(pCaminhoCampoCondicao)) {
            if (UtilSBCoreStringValidador.isNuloOuEmbranco(nomeParametro)) {
                setNomeParametro(pCaminhoCampoCondicao);
            }
        }

    }

}
