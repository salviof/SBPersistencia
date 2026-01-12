package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.ItensGenericos.basico.GrupoUsuariosDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoEntidadeNormal;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoEntidadeTemPermissao;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.ComoUsuario;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import javax.persistence.PostPersist;
import javax.persistence.PreUpdate;

public abstract class EntidadeORMNormal extends EntidadeSimplesORM implements ComoEntidadeNormal, ComoEntidadeTemPermissao {

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public EntidadeORMNormal() {
        super();

//        adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.NOME, getNomeCurto()));
        //      adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.DESCRITIVO, "Lorem ipsum dolor smodo accumsan. Morbi egestas gravida mattis. Suspendisse luctus est a elit gravida imperdiet. Nam in lectus at odio ultricies pretium non a nibh. Suspendisse quis libero sem, sit amet egestas libero. Vestibulum gravida ipsum volutpat nisi dapibus accumsan. Pellentesque imperdiet convallis mollis. Fusce tincidunt diam tempor quam lacinia dapibus. Lorem ipsum dolor sit amet, consectetur adipiscing elit. In eget ipsum at mauris commodo tempus a in nulla. Aliquam erat volutpat. Aliquam non sem a orci tincidunt aliquet. Proin eu gravida odio. Suspendisse potenti."));
        //    adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.REG_ATIVO_INATIVO), false);
        //   adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.REG_DATAALTERACAO), false);
        //  adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.REG_DATAALTERACAO), false);
        // adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.REG_USUARIO_ALTERACAO), false);
        //adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.REG_USUARIO_INSERCAO), false);
    }

    @Override
    public String getNomeLongo() {
        camposEsperados.getCampo(FabTipoAtributoObjeto.NOME_LONGO).setValorPadrao(getNomeCurto());
        return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.NOME_LONGO);

    }

    @Override
    public String getDescritivo() {
        return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.DESCRITIVO);
    }

    @Override
    public String getImgGrande() {

        //return (String) GetValor(TC.IMG_GRANDE);
        return SBCore.getCentralDeArquivos().getEndrRemotoImagem(this, FabTipoAtributoObjeto.IMG_GRANDE);
    }

    @Override
    public String getImgMedia() {
        //return (String) GetValor(TC.IMG_MEDIA);
        return SBCore.getCentralDeArquivos().getEndrRemotoImagem(this, FabTipoAtributoObjeto.IMG_MEDIA);
    }

    @Override
    public List<String> getGaleria() {
        return SBCore.getCentralDeArquivos().getEnterecosRemotosRecursosItem(this);

    }

    @Override
    public Date getDataHoraAlteracao() {
        return (Date) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.REG_DATAALTERACAO);
    }

    /**
     * Executado após salvar o Valor, caso sobrescreva este método é nescessário
     * repetir a anotação em seu método
     *
     */
    @PostPersist
    protected void autoExecAposSalvarNovoRegistro() {

        if (getCampoByAnotacao(FabTipoAtributoObjeto.REG_DATAINSERCAO) != null) {
            setValorByTipoCampoEsperado(FabTipoAtributoObjeto.REG_DATAINSERCAO, new Date());
        }
        if (getCampoByAnotacao(FabTipoAtributoObjeto.REG_USUARIO_INSERCAO) != null) {
            if (!SBCore.getUsuarioLogado().getGrupo().equals(new GrupoUsuariosDoSistema())) {
                setValorByTipoCampoEsperado(FabTipoAtributoObjeto.REG_USUARIO_INSERCAO, SBCore.getUsuarioLogado());
            }
        }

    }

    @PreUpdate
    protected void autoExecAntesSalvarAtualizarRegistro() {
        Field campoRegAlteracao = getCampoByAnotacao(FabTipoAtributoObjeto.REG_DATAALTERACAO);
        if (campoRegAlteracao != null) {
            setValorByTipoCampoEsperado(FabTipoAtributoObjeto.REG_DATAALTERACAO, new Date());
        }

        if (getCampoByAnotacao(FabTipoAtributoObjeto.REG_USUARIO_ALTERACAO) != null) {
            if (!SBCore.getUsuarioLogado().getGrupo().equals(new GrupoUsuariosDoSistema())) {
                setValorByTipoCampoEsperado(FabTipoAtributoObjeto.REG_USUARIO_ALTERACAO, SBCore.getUsuarioLogado());
            }
        }
    }

    @Deprecated
    protected void antesDeAtulizarRegistro() {

        autoExecAntesSalvarAtualizarRegistro();
    }

    @Override
    public Date getDataHoraInsercao() {
        return (Date) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.REG_DATAINSERCAO);
    }

    @Override
    public ComoUsuario getUsuarioInsersao() {
        return (ComoUsuario) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.REG_USUARIO_INSERCAO);
    }

    @Override
    public ComoUsuario getUsuarioAlteracao() {
        return (ComoUsuario) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.REG_USUARIO_ALTERACAO);
    }

    @Override
    public void setAtivo(boolean pAtivo) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.REG_ATIVO_INATIVO, pAtivo);
    }

    @Override
    public boolean isAtivo() {
        return (boolean) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.REG_ATIVO_INATIVO);
    }

    @Override
    public void setNomeLongo(String pnomeLongo) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.NOME_COMPLETO, this);
    }

    @Override
    public void setDescritivo(String pDescritivo) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.DESCRITIVO, this);
    }

    @Override
    public boolean isTemImagemMedioAdicionada() {
        return new File(SBCore.getCentralDeArquivos().getEndrLocalImagem(this, FabTipoAtributoObjeto.IMG_MEDIA)).exists();
    }

    @Override
    public boolean isTemImagemGrandeAdicionada() {
        return new File(SBCore.getCentralDeArquivos().getEndrLocalImagem(this, FabTipoAtributoObjeto.IMG_GRANDE)).exists();
    }

}
