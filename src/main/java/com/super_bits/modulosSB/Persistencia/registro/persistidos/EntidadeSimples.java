package com.super_bits.modulosSB.Persistencia.registro.persistidos;

//import com.super_bits.modulosSB.webPaginas.ConfigGeral.CInfo;
//Simport com.super_bits.modulosSB.webPaginas.JSFBeans.util.OrganizadorDeArquivos;
import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreReflexaoObjeto;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringFiltros;
import com.super_bits.modulosSB.SBCore.modulos.Controller.Interfaces.acoes.ItfAcaoDoSistema;
import com.super_bits.modulosSB.SBCore.modulos.Mensagens.ItfMensagem;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciado.ItfCampoInstanciado;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.PrePersist;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.coletivojava.fw.utilCoreBase.UtilSBCoreComunicacao;

public abstract class EntidadeSimples extends EntidadeGenerica implements
        ItfBeanSimples {

    public EntidadeSimples() {
        super();

        //adcionaCampoEsperado(new CampoEsperado(TC.IMG_PEQUENA, CInfo.SITE_URL
        //CInfo.pastaImagens + "/SBPequeno.jpg"));
        //adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.AAA_NOME), true);
        //adcionaCampoEsperado(new CampoEsperado(FabTipoAtributoObjeto.ID), true);
    }

    @Override
    public String getImgPequena() {

        return SBCore.getCentralDeArquivos().getEndrRemotoImagem(this, FabTipoAtributoObjeto.IMG_PEQUENA);
    }

    @Override
    public String getNomeCurto() {
        try {
            String nomeCurto = UtilSBCoreStringFiltros.getNomeReduzido((String) getCampoInstanciadoByAnotacao(FabTipoAtributoObjeto.AAA_NOME).getValor());

            return nomeCurto;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Obtendo o campo nome da classe" + this.getClass().getSimpleName() + " certifique que o nome tenha sido anotado, e que o tipo retornado seja String", t);
        }
        return null;
    }

    public String getNomeCurtoURLAmigavel() {
        String nomeCurto = getNomeCurto();
        return UtilSBCoreStringFiltros.gerarUrlAmigavel(nomeCurto);
    }

    @Override
    public int getId() {
        Object valorId = getValorByTipoCampoEsperado(FabTipoAtributoObjeto.ID).toString();
        if (valorId != null && !"".equals(valorId)) {
            return Integer.parseInt(valorId.toString());
        } else {
            return 0;
        }
    }

    public String getNomeUnico() {
        return this.getClass().getSimpleName() + getId();
    }

    /**
     *
     * Retorna o nome do Campo anotados com InfoCampo proprio para pesquisa em
     * SQL
     *
     * Futuramente este metodo deve funcionar analizando a anotação collum do
     * Hibernate para casos onde o nome da coluna é diferente
     *
     * @param pCAmpo Nome do campos procurado
     * @return O nome do campo proprio para SQL
     */
    public String getCampoSQL(FabTipoAtributoObjeto pCAmpo) {
        try {
            Field campo = getCampoByAnotacao(FabTipoAtributoObjeto.AAA_NOME);
            if (campo == null) {

                throw new UnexpectedException("nome_curto_nao_encontrado_na_classe");

            }
            return campo.getName();
        } catch (Throwable ex) {
            SBCore.RelatarErro(FabErro.LANCAR_EXCECÃO, "Erro tentando obter o nome do campo para select ", ex);

            return null;
        }
    }

    /**
     *
     * Retorna os nomes do Campos que foram anotados para pesquisas SQL
     *
     * Futuramente este metodo deve funcionar analizando a anotação collum do
     * Hibernate para casos onde o nome da coluna é diferente
     *
     * @param pCAmpo Tipo de campo pesquisado
     * @return Lista com os nomes encontrados
     *
     */
    public List<String> getCamposSQL(FabTipoAtributoObjeto pCAmpo) {
        List<String> lista = new ArrayList<>();
        try {

            Field campo = getCampoByAnotacao(FabTipoAtributoObjeto.AAA_NOME);
            if (campo == null) {

                throw new UnexpectedException("nome_curto_nao_encontrado_na_classe");

            }

            lista.add(campo.getName());
        } catch (Throwable ex) {
            SBCore.RelatarErro(FabErro.LANCAR_EXCECÃO, "Erro tentando obter o nome do campo para select ", ex);

        }
        return lista;

    }

    /**
     *
     *
     * Substitua por: getCampoSQL(FabTipoAtributoObjeto pCAmpo) Antes que seja
     * tarde demais!
     *
     * @return
     */
    @Deprecated
    public String getCampoSQLNomeCurto() {
        return getCampoSQL(FabTipoAtributoObjeto.AAA_NOME);
    }

    public void uploadFoto(Object event) {

        SBCore.getControleDeSessao(); //    throw new UnsupportedOperationException("Ainda não implementado");
        //  FileUploadEvent evento = (Primef)
        //  String categoria = (String) event.getComponent().getAttributes()
        //           .get("catImagem");
        //    UtilSBPersistenciaArquivosDeEntidade.SalvaIMAGEM(this,
        //            event.getFile(), categoria);
        //    throw new UnsupportedOperationException("Ainda não implementado");

    }

    public List<ItfBeanSimples> listaOpcoes(ItfBeanSimples objeto) {
        return (List) UtilSBPersistencia.getListaTodos(objeto.getClass());

    }

    @PrePersist
    public void configUsuarioAlteriou() {
        System.out.println("EXECUTOU PRE PERSIST!!!!" + this.getClass().getSimpleName());
    }

    public Long getQuantidadeRegistros() {
        return UtilSBPersistencia.getQuantidadeRegistrosNaTabela(this.getClass());
    }

    @Override
    public String getNome() {
        return (String) getValorByTipoCampoEsperado(FabTipoAtributoObjeto.AAA_NOME);
    }

    @Override
    public void setNome(String pNome) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.AAA_NOME, pNome);
    }

    @Override
    public void setId(int pID) {
        setValorByTipoCampoEsperado(FabTipoAtributoObjeto.ID, pID);
    }

    @Override
    public String getNomeUnicoSlug() {
        return getNome() + "-" + getId();
    }

    @Override
    public String getIconeDaClasse() {
        return UtilSBCoreReflexaoObjeto.getIconeDoObjeto(this.getClass());
    }

    @Override
    @Deprecated
    public boolean validar() {
        return UtilSBCoreComunicacao.isTemMensagemComErro(validarComMensagens());
    }

    @Override
    @Deprecated
    public List<ItfMensagem> validarComMensagens() {

        return new ArrayList<>();
    }

    @Override
    public boolean uploadArquivoDeEntidade(ItfCampoInstanciado prcampo, byte[] pStream, String pNomeArquivo) {
        if (SBCore.getCentralDeArquivos().salvarArquivo(prcampo, pStream, pNomeArquivo)) {
            prcampo.setValor(pNomeArquivo);
            return true;
        }

        return false;

    }

    @Override
    public boolean uploadFotoTamanhoGrande(InputStream pStream) {
        return SBCore.getCentralDeArquivos().salvarImagemTamanhoGrande(this, pStream);
    }

    @Override
    public boolean uploadFotoTamanhoPequeno(InputStream pStream) {

        return SBCore.getCentralDeArquivos().salvarImagemTamanhoPequeno(this, pStream);
    }

    @Override
    public boolean uploadFotoTamanhoMedio(InputStream pStream) {
        return SBCore.getCentralDeArquivos().salvarImagemTamanhoMedio(this, pStream);
    }

    @Override
    public boolean uploadFotoTodosFormatos(InputStream pStream) {
        return SBCore.getCentralDeArquivos().salvarImagemTodosOsFormatos(this, pStream);
    }

    @Override
    public String getSlugIdentificador() {
        return gerarSlug();
    }

    @Override
    public boolean isTemImagemPequenaAdicionada() {

        return SBCore.getServicoArquivosDeEntidade().isTemImagem(this, FabTipoAtributoObjeto.IMG_PEQUENA);
    }

    @Override
    public String getXhtmlVisaoMobile() {
        return MapaObjetosProjetoAtual.getVisualizacaoDoObjeto(this.getClass());
    }

    @Override
    public String getXhtmlVisao() {
        return MapaObjetosProjetoAtual.getVisualizacaoDoObjeto(this.getClass());
    }

    @Override
    public String getXhtmlVisao(int numeroColunas) {
        return MapaObjetosProjetoAtual.getVisualizacaoDoObjeto(this.getClass());
    }

    @Override
    public List<ItfAcaoDoSistema> getAcoesDisponiveis() {
        return ItfBeanSimples.super.getAcoesDisponiveis(); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/OverriddenMethodBody
    }

}
