/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.centralLocalizacao;

import br.org.coletivoJava.fw.api.erp.codigoPostal.br.ERPCodigoPostalBR;
import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.Bairro;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.FabUnidadesFederativas;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.UnidadeFederativa;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;

import com.super_bits.modulosSB.SBCore.modulos.localizacao.ItfCentralLocalizacao;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanLocalizavel;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfBairro;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfCidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfLocal;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfUnidadeFederativa;
import java.util.ArrayList;
import java.util.List;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 */
public class CentralLocalizacaoSBPersistencia implements ItfCentralLocalizacao {

    @Override
    public List<ItfUnidadeFederativa> getUnidadesFederativas() {

        return (List) UtilSBPersistencia.getListaTodos(UnidadeFederativa.class, UtilSBPersistencia.getEMDoContexto());
    }

    @Override
    public List<ItfCidade> gerarListaDeCidades(String pNomePesquisa, ItfUnidadeFederativa pUnidadeFederativa) {
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
    public List<ItfBairro> gerarListaDeBairros(String pNomePesquisa, ItfCidade pCidade) {
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
    public List<ItfCidade> gerarListaDeCidades(String pNomePesquisa, ItfUnidadeFederativa pUnidadeFederativa, String parametroEspecial) {
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
    public List<ItfBairro> gerarListaDeBairros(String pNomePesquisa, ItfCidade pCidade, String parametroEspecial) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configurarPosicionamento(ItfLocal pLocal) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void configurarCep(ItfLocal pLocal) {
        SBCore.getServicoLocalizacao().getImplementacaoPadraoApiCep().getImplementacaoDoContexto().cepsPorEndereco(pLocal.toString());

    }

    @Override
    public void configurarEndereco(String cep, ItfLocal pLocal) {
        getImplementacaoPadraoApiCep().getImplementacaoDoContexto().configuraEndereco(cep, pLocal);
    }

    @Override
    public boolean salvarFlexivel(ItfBeanLocalizavel pBeanLocalizava) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ItfBairro instanciarNovoBairo(String pBairro, ItfCidade pCidade) {
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
