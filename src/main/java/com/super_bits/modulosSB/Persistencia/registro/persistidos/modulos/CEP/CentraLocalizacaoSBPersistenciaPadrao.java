/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP;

import com.super_bits.modulosSB.Persistencia.centralLocalizacao.CentralLocalizacaoSBPersistencia;
import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreListasObjeto;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.localizacao.ItfCentralLocalizacao;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanLocalizavel;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfBairro;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfCidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfUnidadeFederativa;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author desenvolvedor
 *
 *
 */
public class CentraLocalizacaoSBPersistenciaPadrao extends CentralLocalizacaoSBPersistencia implements ItfCentralLocalizacao {

    private EntityManager emLocalizacao;

    @Override
    public List<ItfUnidadeFederativa> getUnidadesFederativas() {
        List<UnidadeFederativa> lista;

        emLocalizacao = UtilSBPersistencia.getEMDoContexto();

        try {
            lista = UtilSBPersistencia.getListaTodos(UnidadeFederativa.class, emLocalizacao);
        } catch (Throwable t) {

            lista = UtilSBPersistencia.getListaTodos(UnidadeFederativa.class, emLocalizacao);
        }

        return (List) lista;
    }

    @Override
    public List<ItfCidade> gerarListaDeCidades(String pNomePesquisa, ItfUnidadeFederativa pUnidadeFederativa) {
        try {

            emLocalizacao = UtilSBPersistencia.getEMDoContexto();
            if (pUnidadeFederativa == null) {
                return new ArrayList<>();
            }
            UnidadeFederativa uf = UtilSBPersistencia.loadEntidade(pUnidadeFederativa, emLocalizacao);
            if (uf == null) {
                return new ArrayList<>();
            }
            List<ItfCidade> todasCidades = uf.getCidades();

            return UtilSBCoreListasObjeto.filtrarOrdenandoMaisParecidos(todasCidades, pNomePesquisa, 5);

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro pesquisando cidades da unidade federativa", t);
            return new ArrayList<>();
        }

    }

    @Override
    public List<ItfBairro> gerarListaDeBairros(String pNomePesquisa, ItfCidade pCidade, String parametroEspecial) {
        try {

            EntityManager em = UtilSBPersistencia.getNovoEM();

            Cidade cidade = UtilSBPersistencia.loadEntidade(pCidade, emLocalizacao);

            List<ItfBairro> todosOsBairros = cidade.getBairros();

            List<Cidade> listaEncontrada = new ArrayList<>();

            for (ItfBairro bairro : todosOsBairros) {

                if (bairro.getNome().toLowerCase().contains(pNomePesquisa)) {
                    listaEncontrada.add((Cidade) bairro);
                }
            }

            UtilSBPersistencia.fecharEM(em);
            return (List) listaEncontrada;

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro pesquisando cidades da unidade federativa", t);
            return new ArrayList<>();
        }

    }

    @Override
    public boolean salvarFlexivel(ItfBeanLocalizavel pBeanLocalizavel) {
        EntityManager em = UtilSBPersistencia.getNovoEM();
        UtilSBPersistencia.iniciarTransacao(em);
        UnidadeFederativa uf = UtilSBPersistencia.mergeRegistro(pBeanLocalizavel.getLocalizacao().getBairro().getCidade().getUnidadeFederativa());
        boolean cadFlexOK = false;
        if (uf != null) {
            pBeanLocalizavel.getLocalizacao().getBairro().getCidade().setUnidadeFederativa(uf);
            Cidade cidade = UtilSBPersistencia.mergeRegistro(pBeanLocalizavel.getLocalizacao().getBairro().getCidade());
            if (cidade != null) {
                pBeanLocalizavel.getLocalizacao().getBairro().setCidade(cidade);
                Bairro bairro = UtilSBPersistencia.mergeRegistro(pBeanLocalizavel.getLocalizacao().getBairro());
                if (bairro != null) {
                    pBeanLocalizavel.getLocalizacao().setBairro(bairro);
                    cadFlexOK = true;
                }
            }

        }
        if (!cadFlexOK) {
            UtilSBPersistencia.fecharEM(em);
            return false;
        }

        return UtilSBPersistencia.finzalizaTransacaoEFechaEM(em);

    }

}
