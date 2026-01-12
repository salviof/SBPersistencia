/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.centralLocalizacao;

import br.org.coletivoJava.fw.api.erp.codigoPostal.br.ERPCodigoPostalBR;
import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.Bairro;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.UnidadeFederativa;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;

import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoEntidadeLocalizavel;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoBairro;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoCidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoLocal;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoUnidadeFederativa;
import java.util.ArrayList;
import java.util.List;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import com.super_bits.modulosSB.SBCore.modulos.localizacao.CmoServicoLocalizacao;

/**
 *
 * @author desenvolvedor
 */
public class CentralLocalizacaoSBPersistencia implements CmoServicoLocalizacao {

    @Override
    public List<ComoUnidadeFederativa> getUnidadesFederativas() {

        return (List) UtilSBPersistencia.getListaTodos(UnidadeFederativa.class, UtilSBPersistencia.getEMDoContexto());
    }

    @Override
    public List<ComoCidade> gerarListaDeCidades(String pNomePesquisa, ComoUnidadeFederativa pUnidadeFederativa) {
        try {
            if (pUnidadeFederativa == null) {
                throw new UnsupportedOperationException("O parametro estado não foi enviado para pesquisa de cidades");
            }

            if (pNomePesquisa == null) {
                throw new UnsupportedOperationException("O parametro StringPesquisa não foi enviado para pesquisa de cidades");
            }
            return UtilSBPersistencia.getListaRegistrosByHQL(" select from Cidade where unidadeFederativa.id=?", -1, pUnidadeFederativa.getId());

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro pesquisando Cidades do Estado" + pUnidadeFederativa, t);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ComoBairro> gerarListaDeBairros(String pNomePesquisa, ComoCidade pCidade) {
        try {
            if (pCidade == null) {
                throw new UnsupportedOperationException("O parametro estado não foi enviado para pesquisa de cidades");
            }

            if (pNomePesquisa == null) {
                throw new UnsupportedOperationException("O parametro StringPesquisa não foi enviado para pesquisa de cidades");
            }
            return UtilSBPersistencia.getListaRegistrosByHQL(" select from Bairro where cidade.id=?", -1, pCidade.getId());

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro pesquisando Cidades do Estado" + pCidade, t);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ComoCidade> gerarListaDeCidades(String pNomePesquisa, ComoUnidadeFederativa pUnidadeFederativa, String parametroEspecial) {
        try {
            if (pUnidadeFederativa == null) {
                throw new UnsupportedOperationException("O parametro estado não foi enviado para pesquisa de cidades");
            }

            if (pNomePesquisa == null) {
                throw new UnsupportedOperationException("O parametro StringPesquisa não foi enviado para pesquisa de cidades");
            }
            return UtilSBPersistencia.getListaRegistrosByHQL(" select from Cidade where unidadeFederativa.id=?", 0, pUnidadeFederativa.getId());

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro pesquisando Cidades do Estado" + pUnidadeFederativa, t);
            return new ArrayList<>();
        }
    }

    @Override
    public List<ComoBairro> gerarListaDeBairros(String pNomePesquisa, ComoCidade pCidade, String parametroEspecial) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configurarPosicionamento(ComoLocal pLocal) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configurarCep(ComoLocal pLocal) {
        SBCore.getServicoLocalizacao().getImplementacaoPadraoApiCep().getImplementacaoDoContexto().cepsPorEndereco(pLocal.toString());

    }

    @Override
    public void configurarEndereco(String cep, ComoLocal pLocal) {
        getImplementacaoPadraoApiCep().getImplementacaoDoContexto().configuraEndereco(cep, pLocal);
    }

    @Override
    public boolean salvarFlexivel(ComoEntidadeLocalizavel pBeanLocalizava) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ComoBairro instanciarNovoBairo(String pBairro, ComoCidade pCidade) {
        Bairro novoBairro = new Bairro();
        novoBairro.setCidade(pCidade);
        novoBairro.setNome(pBairro);
        novoBairro.configIDPeloNome();

        return novoBairro;
    }

    @Override
    public ERPCodigoPostalBR getImplementacaoPadraoApiCep() {
        return ERPCodigoPostalBR.API_FREE_REDUNTANTE;
    }

}
