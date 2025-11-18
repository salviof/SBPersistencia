package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.Bairro;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.Cidade;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.LocalizacaoPostavel;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.UnidadeFederativa;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.CampoEsperado;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.contato.ComoContatoCorporativo;

import javax.persistence.EntityManager;

public abstract class EntidadeContatoCorporativo extends EntidadeEnderecavel implements
        ComoContatoCorporativo {

    /**
     *
     * Carrega a empresa localizando-a através das informações de contato, que
     * podem ser: NOME, TELEFONE, SITE
     *
     *
     * @param pTelefon_nome_cnpj Parametro
     * @param pEm Entity Manager
     * @return
     */
    public boolean loadEmpresabyContato(String pTelefon_nome_cnpj, EntityManager pEm) {
        Object registroEncontrado = UtilSBPersistencia.getEmpresa(this.getClass(), pTelefon_nome_cnpj, pEm);
        if (registroEncontrado == null) {
            return false;
        }

        copiaDados(registroEncontrado);
        return true;
    }

    public EntidadeContatoCorporativo() {
        super();
        adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.CNPJ));
        // TODO implementar EntidadeContatoCorporativo
    }

    @Override
    public String getSite() {

        return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.SITE);
    }

    @Override
    public String telefone() {
        return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.TELEFONE_FIXO_NACIONAL);
    }

    @Override
    public String responsavel() {
        return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.RESPONSAVEL);
    }

    @Override
    public void instanciarNovoEndereco() {
        setLocalizacao(new LocalizacaoPostavel());
        getLocalizacao().setBairro(new Bairro());
        getLocalizacao().getBairro().setCidade(new Cidade());
        getLocalizacao().getBairro().getCidade().setUnidadeFederativa(new UnidadeFederativa());
    }

}
