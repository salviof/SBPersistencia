/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.dinamico;

import com.google.common.collect.Lists;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.EntidadeSimplesORM;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.ListenerEntidadePadrao;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringFiltros;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringValidador;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabrica;

import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampoValidadorLogico;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.PropriedadesReflexaoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.GrupoCampos;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.ItfGrupoCampos;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.TIPO_ORIGEM_VALOR_CAMPO;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.TIPO_PRIMITIVO;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.TipoAtributoMetodosBase;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciado.ItfAtributoObjetoEditavel;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciado.ItfAtributoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
import java.util.List;
import java.util.Map;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

/**
 *
 * @author sfurbino
 */
@Entity
@InfoObjetoSB(tags = "Tipo Dado Dinamico", plural = "Tipos de Dados dinâmicos", icone = "fa fa-pencil-square-o")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoJPATipoDadoDinamico")
@EntityListeners(ListenerEntidadePadrao.class)
public class TipoDadoDinamico extends EntidadeSimplesORM implements ItfAtributoObjetoEditavel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @InfoCampo(tipo = FabTipoAtributoObjeto.ID)
    private Long id;

    @Column(nullable = false, updatable = false, insertable = false)
    private String tipoJPATipoDadoDinamico;

    @Enumerated(EnumType.ORDINAL)
    @InfoCampo(label = "Tipo De Campo", obrigatorio = true)
    @Column(nullable = false)
    @InfoCampoValidadorLogico
    private FabTipoAtributoObjeto fabricaTipoAtributo;

    @InfoCampo(tipo = FabTipoAtributoObjeto.NOME, obrigatorio = true)
    private String nome;

    @InfoCampo(tipo = FabTipoAtributoObjeto.TEXTO_SIMPLES, obrigatorio = true)
    private String label;

    @InfoCampo(label = "Mascara")
    private String mascara;

    @InfoCampo(label = "Entidade Listada")
    private String objetReferente;

    @InfoCampo(label = "Valor padrao")
    private String valorPadrao;

    @InfoCampo(label = "Campo Correspondente")
    private String campoProspectoCorrespondente;
    private String caminhoListagem;

    private boolean unicaColeta = true;
    @InfoCampo(label = "Objeto Listado")
    private String objetoReferente;
    @Transient
    @InfoCampo(tipo = FabTipoAtributoObjeto.LISTA_OBJETOS_PARTICULARES)
    private List<ComoEntidadeSimples> listaDeOpcoes;

    @ElementCollection
    private Map<String, String> listaOpcoesTemplate;

    private final String nomeClasseObjetoOrigem;
    private String nomeClasseAtributoDeclarado;
    private String nomeClasseObjetoDestino;
    private int valorMaximo;
    private int valorMinimo;
    private String validacaoRegex;
    private char separadorDecimal;
    private boolean obrigatorio;
    private char separadorMilhar = '.';
    private int numeroDeCasasDecimais;
    private String mascaraJqueryMode;
    private boolean somenteLeitura;
    private String fraseValidacao;
    private boolean umalistagemDinamica;
    private boolean umValorCampoUnico;
    private String iconePositivo;
    private String textoPositivo;
    private String iconeNegativo;
    private String textoNegativo;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoDado")
    @InfoCampo(tipo = FabTipoAtributoObjeto.LISTA_OBJETOS_PARTICULARES)
    private List<OpcaoDadoDinamico> opcoesPersonalizada;
    @Transient
    private PropriedadesReflexaoCampo propriedades;
    private String descricao;

    public TipoDadoDinamico() {
        nomeClasseObjetoOrigem = this.getClass().getSimpleName();
    }

    private PropriedadesReflexaoCampo getPropriedades() {
        if (propriedades == null) {
            propriedades = new PropriedadesReflexaoCampo(this);
        }
        return propriedades;
    }

    @Override
    public List<String> getTemplateCamposDinamicos() {
        return Lists.newArrayList(listaOpcoesTemplate.values());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public FabTipoAtributoObjeto getFabricaTipoAtributo() {
        return fabricaTipoAtributo;
    }

    public void setUnicaColeta(boolean unicaColeta) {
        this.unicaColeta = unicaColeta;
    }

    @Override
    public String getLabel() {
        if (label == null) {
            label = getLabelPadrao();
        }
        return label;
    }

    @Override
    public String getDescricao() {
        return descricao;
    }

    @Override
    public String getMascara() {
        return mascara;
    }

    @Override
    public String getValorPadrao() {
        return valorPadrao;
    }

    @Override
    public boolean isObrigatorio() {
        return obrigatorio;
    }

    public final ItfAtributoObjetoSB geAttributoCampoVinculado() {
        if (propriedades == null) {
            propriedades = new PropriedadesReflexaoCampo(this);
        }
        return propriedades.getAtributoGerado();
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public String getIdComponente() {
        return UtilSBCoreStringFiltros.gerarUrlAmigavel(label);
    }

    @Override
    public List<ComoEntidadeSimples> getListaDeOpcoes() {
        if (getNomeClasseAtributoDeclarado() != null
                && getNomeClasseAtributoDeclarado().equals(OpcaoDadoDinamico.class.getSimpleName())) {
            return (List) getOpcoesPersonalizada();

        } else {
            return SBCore.getCentralFonteDeDados().getListaOpcoesCampo(getPropriedades());
        }
    }

    @Override
    public String getValidacaoRegex() {
        return validacaoRegex;
    }

    @Override
    public boolean isTemValidacaoRegex() {
        return UtilSBCoreStringValidador.isNAO_NuloNemBranco(validacaoRegex);
    }

    @Override
    public boolean isTemValidacaoMinimo() {
        return valorMinimo > 0;
    }

    @Override
    public boolean isTemValidacaoMaximo() {
        return valorMaximo > 0;
    }

    @Override
    public boolean isTemMascara() {
        return UtilSBCoreStringValidador.isNAO_NuloNemBranco(mascara);
    }

    @Override
    public boolean isNumeral() {
        return getTipoPrimitivoDoValor().isNumeral();
    }

    @Override
    public boolean isMoeda() {
        return getFabricaTipoAtributo().isCampoUmMoeda();
    }

    @Override
    public char getSeparadorDecimal() {
        return separadorDecimal;
    }

    @Override
    public char getSeparadorMilhar() {
        return separadorMilhar;
    }

    @Override
    public int getNumeroDeCasasDecimais() {
        return numeroDeCasasDecimais;
    }

    @Override
    public String getMascaraJqueryMode() {
        return UtilSBCoreStringFiltros.getMascaraJavaMaskParaJQueryMask(mascara);
    }

    @Override
    public String getFraseValidacao() {
        return fraseValidacao;
    }

    @Override
    public TIPO_PRIMITIVO getTipoPrimitivoDoValor() {
        return getFabricaTipoAtributo().getTipoPrimitivo();

    }

    @Override
    public TIPO_ORIGEM_VALOR_CAMPO getOrigemValor() {
        return getFabricaTipoAtributo().getTipoOrigemPadrao();
    }

    @Override
    public boolean isUmValorLivre() {
        return getFabricaTipoAtributo().getTipoOrigemPadrao().isUmValorLivre();
    }

    @Override
    public boolean isUmValorComLista() {
        return isOrigemDesteTipo(TIPO_ORIGEM_VALOR_CAMPO.VALOR_COM_LISTA);
    }

    @Override
    public boolean isUmValorMultiploLivre() {
        return isOrigemDesteTipo(TIPO_ORIGEM_VALOR_CAMPO.VALORES_LIVRE);
    }

    @Override
    public boolean isUmValorMultiploComLista() {
        return isOrigemDesteTipo(TIPO_ORIGEM_VALOR_CAMPO.VALORES_COM_LISTA);
    }

    @Override
    public String getTipoCampoSTR() {
        return getFabricaTipoAtributo().toString();
    }

    @Override
    public boolean isSomenteLeitura() {
        return somenteLeitura;
    }

    public String getCampoProspectoCorrespondente() {
        return campoProspectoCorrespondente;
    }

    public void setCampoProspectoCorrespondente(String campoProspectoCorrespondente) {
        this.campoProspectoCorrespondente = campoProspectoCorrespondente;
    }

    @Override
    public void setListaDeOpcoes(List<ComoEntidadeSimples> pLista) {
        System.out.println("SetLista de opcoes está desabilitado");
    }

    @Override
    public void setDescricao(String pDescricao) {
        descricao = pDescricao;
    }

    @Override
    public void setFraseValidacao(String pFraseValidacao) {
        fraseValidacao = pFraseValidacao;
    }

    @Override
    public void setMascara(String pMaskara) {
        mascara = pMaskara;
    }

    @Override
    public void setValidacaoRegex(String pValidacaoRegex) {
        validacaoRegex = pValidacaoRegex;
    }

    @Override
    public void setLabel(String pLabel) {
        if (nome == null) {
            nome = pLabel;
        }
        label = pLabel;
    }

    @Override
    public void setSeparadorDecimal(char pSeparadorDecimal) {
        separadorDecimal = pSeparadorDecimal;
    }

    @Override
    public void setSeparadorMilhar(char pSeparadorMilhar) {
        separadorMilhar = pSeparadorMilhar;
    }

    @Override
    public void setNumeroDeCasasDecimais(int pNumeroCasasDecimais) {
        numeroDeCasasDecimais = pNumeroCasasDecimais;
    }

    @Override
    public void setSomenteLeitura(boolean pSomenteLeitura) {
        somenteLeitura = pSomenteLeitura;
    }

    @Override
    public void setObrigatorio(boolean pObrigatorio) {
        obrigatorio = pObrigatorio;
    }

    @Override
    public String getNomeClasseAtributoDeclarado() {
        return nomeClasseAtributoDeclarado;
    }

    public void setObjetoReferente(String pObjetoLista) {
        objetReferente = pObjetoLista;
    }

    @Override
    public String getCaminhoListagem() {
        return caminhoListagem;
    }

    public void setCaminhoListagem(String caminhoListagem) {
        this.caminhoListagem = caminhoListagem;
    }

    @Override
    public void setNomeClasseAtributoDeclarado(String pObjetoEntidade) {
        objetReferente = pObjetoEntidade;
        objetoReferente = pObjetoEntidade;
        nomeClasseAtributoDeclarado = pObjetoEntidade;
    }

    @Override
    public boolean isUmCampoDinamico() {
        return true;
    }

    @Override
    public String getNomeClasseOrigemAtributo() {
        return nomeClasseObjetoOrigem;
    }

    @Override
    public void setNomeClasseOrigemAtributo(String pObjeto) {
        //   nomeClasseObjetoOrigem = pObjeto;
    }

    @Override
    public String getTipoVisualizacao() {

        return new TipoAtributoMetodosBase(getFabricaTipoAtributo()).getRegistro().getTipoVisualizacao();
    }

    private boolean isOrigemDesteTipo(TIPO_ORIGEM_VALOR_CAMPO pOrigem) {
        return getFabricaTipoAtributo().getTipoOrigemPadrao().equals(pOrigem);
    }

    @Override
    public void setFabricaTipoAtributo(FabTipoAtributoObjeto pTipoCampo) {
        if (fabricaTipoAtributo != null && pTipoCampo != null) {
            if (fabricaTipoAtributo.equals(pTipoCampo)) {
                return;
            }
        }
        fabricaTipoAtributo = pTipoCampo;

        if (fabricaTipoAtributo != null) {
            fabricaTipoAtributo.configuraPropriedadesBasicas(this);
        }
    }

    @Override
    public GrupoCampos getGrupoSubCamposExibicao() {
        return null;
    }

    @Override
    public void setLabelPadrao(String pLabel) {
        label = pLabel;
    }

    @Override
    public String getLabelPadrao() {
        return label;
    }

    @Override
    public void setNome(String pNome) {
        nome = pNome;
    }

    public List<OpcaoDadoDinamico> getOpcoesPersonalizada() {
        return opcoesPersonalizada;
    }

    public boolean isUmalistagemDinamica() {
        return umalistagemDinamica;
    }

    public void setUmalistagemDinamica(boolean umalistagemDinamica) {
        this.umalistagemDinamica = umalistagemDinamica;
        if (umalistagemDinamica) {
            setNomeClasseAtributoDeclarado(OpcaoDadoDinamico.class.getSimpleName());
        }
    }

    @Override
    public void setTextoNegativo(String pTextoNegativo) {
        textoNegativo = pTextoNegativo;
    }

    @Override
    public void setIconePositivo(String pIconePositivo) {
        iconePositivo = pIconePositivo;
    }

    @Override
    public void setIconeNegativo(String pIconeNegativo) {
        iconeNegativo = pIconeNegativo;
    }

    @Override
    public void setTextoPositivo(String pTextoPositivo) {
        textoPositivo = pTextoPositivo;
    }

    @Override
    public void setValorCampoUnico(boolean pValor) {
        umValorCampoUnico = pValor;
    }

    @Override
    public String getTextoPositivo() {
        return textoPositivo;
    }

    @Override
    public String getTextoNegativo() {
        return textoNegativo;
    }

    @Override
    public String getIconePositivo() {
        return iconePositivo;
    }

    @Override
    public String getIconeNegativo() {
        return iconeNegativo;
    }

    @Override
    public boolean isValorCampoUnico() {
        return false;
    }

    @Override
    public int getValorMaximo() {
        return valorMaximo;
    }

    @Override
    public int getValorMinimo() {
        return valorMinimo;
    }

    @Override
    public void setValorMaximo(int pValorMaximo) {
        valorMaximo = pValorMaximo;
    }

    @Override
    public void setValorMinimo(int pValorMinimo) {
        valorMinimo = pValorMinimo;
    }

    @Override
    public void setTemValidacaoLogica(boolean pValor) {

    }

    @Override
    public boolean isTemValidadacaoLogica() {
        return false;
    }

    @Override
    public void setGrupoSubCamposExibicao(ItfGrupoCampos pGrupo) {

    }

    @Override
    public void setUmValorLogico(boolean pValor) {

    }

    @Override
    public void setUmaListaDinamica(boolean pValor) {

    }

    @Override
    public boolean isUmValorLogico() {
        return false;
    }

    @Override
    public boolean isUmaListaDinamica() {
        return false;
    }

    @Override
    public void setPermitirCadastroManualEndereco(boolean permitirCadastroManualEndereco) {
        //
    }

    @Override
    public boolean isPermitirCadastroManualEndereco() {
        return false;
    }

    @Override
    public void setAtualizarValorLogicoAoPersistir(boolean pAtualizarValorLogico) {

    }

    @Override
    public boolean isAtualizarValorLogicoAoPersistir() {
        return false;
    }

    @Override
    public void setEnumVinculado(ComoFabrica pFabrica) {
        fabricaTipoAtributo = (FabTipoAtributoObjeto) pFabrica;
    }

    @Override
    public ComoFabrica getEnumVinculado() {
        return fabricaTipoAtributo;

    }

}
