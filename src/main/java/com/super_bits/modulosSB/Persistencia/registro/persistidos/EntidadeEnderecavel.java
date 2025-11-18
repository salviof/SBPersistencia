package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.Bairro;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.Cidade;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.Localizacao;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.LocalizacaoPostavel;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.UnidadeFederativa;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.CampoEsperado;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciado.ItfCampoInstanciado;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoTemEndereco;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ComoCidade;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ComoLocalPostagem;

/**
 *
 *
 *
 * @author desenvolvedor
 * @deprecated Utilizar classe Contato
 */
@Deprecated
public abstract class EntidadeEnderecavel extends EntidadeORMNormal implements ComoTemEndereco {

    //private LatLng localizacao;
    public EntidadeEnderecavel() {

        super();

        adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.LC_LOCALIZACAO, null));
        adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.TELEFONE_FIXO_NACIONAL, null));

    }

    private void setLongitude(Double pLongitude) {
        Localizacao endereco = (Localizacao) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_LOGRADOURO);
        if (endereco == null) {
            ItfCampoInstanciado campoEndereco = getCampoByNomeOuAnotacao(FabTipoAtributoObjeto.LC_LOGRADOURO.toString());
            {
                campoEndereco.setValor(new Localizacao());
            }

        }
        Localizacao loc = (Localizacao) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_LOGRADOURO);
        loc.setLongitude(0);

    }

    private void setLatitude(Double pLatitude) {
        Localizacao endereco = (Localizacao) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_LOGRADOURO);
        if (endereco == null) {
            ItfCampoInstanciado campoEndereco = getCampoByNomeOuAnotacao(FabTipoAtributoObjeto.LC_LOGRADOURO.toString());
            {
                campoEndereco.setValor(new Localizacao());
            }

        }
        Localizacao loc = (Localizacao) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_LOGRADOURO);
        loc.setLatitude(0);

    }

    @Override
    public String getComplemento() {
        if (isTemCampoAnotado(FabTipoAtributoObjeto.LC_LOCALIZACAO)) {
            Localizacao endereco = (Localizacao) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_LOCALIZACAO);
            return endereco.getComplemento();
        }

        if (isTemCampoAnotado(FabTipoAtributoObjeto.LC_COMPLEMENTO_E_NUMERO)) {
            return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_COMPLEMENTO_E_NUMERO);
        }
        return null;

    }

    public Localizacao getLogradouro() {

        return (Localizacao) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_LOGRADOURO);
    }

    @Override
    public String getTelefone() {
        if (isTemCampoAnotado(FabTipoAtributoObjeto.TELEFONE_FIXO_NACIONAL)) {
            return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.TELEFONE_FIXO_NACIONAL);
        }
        if (isTemCampoAnotado(FabTipoAtributoObjeto.TELEFONE_GENERICO)) {
            return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.TELEFONE_GENERICO);
        }
        if (isTemCampoAnotado(FabTipoAtributoObjeto.TELEFONE_FIXO_INTERNACIONAL)) {
            return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.TELEFONE_FIXO_INTERNACIONAL);
        }

        if (isTemCampoAnotado(FabTipoAtributoObjeto.TELEFONE_CELULAR)) {
            return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.TELEFONE_FIXO_INTERNACIONAL);
        }
        return null;
    }

    @Override
    public ComoLocalPostagem getLocalizacao() {
        try {
            Object teste;
            ComoLocalPostagem localPostagem = (ComoLocalPostagem) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_LOCALIZACAO);
            return localPostagem;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Impossível obter o campo localizacao do tipo: " + ComoLocalPostagem.class.getSimpleName() + " na classe: " + this.getClass().getSimpleName(), t);
            return null;
        }
    }

    public void setLocalizacao(ComoLocalPostagem pLocal) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.LC_LOCALIZACAO, pLocal);

    }

    @Override
    public void instanciarNovoEndereco() {

        setLocalizacao(new LocalizacaoPostavel());
        getLocalizacao().setBairro(new Bairro());
        getLocalizacao().getBairro().setCidade((ComoCidade) new Cidade());
        getLocalizacao().getBairro().getCidade().setUnidadeFederativa(new UnidadeFederativa());
    }

    /**
     * public LatLng getLocalizacao() { setLatitude((Double)
     * GetValorByTipoCampoEsperado(TC.LAT) ); setLongitude((Double)
     * GetValorByTipoCampoEsperado(TC.LONG)); setLocalizacao(); return
     * localizacao; }
     */
}
