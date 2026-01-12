package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP;

import com.super_bits.modulosSB.Persistencia.geradorDeId.GeradorIdCidade;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.EntidadeORMNormal;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampoVerdadeiroOuFalso;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoBairro;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoCidade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.hibernate.annotations.GenericGenerator;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoLocalidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.entidade.basico.cep.ComoUnidadeFederativa;

/**
 * The persistent class for the cidade database table.
 *
 */
@Entity
@InfoObjetoSB(tags = {"Cidade"}, plural = "Cidades")
public class Cidade extends EntidadeORMNormal implements Serializable, ComoCidade {

    @Id
    @GenericGenerator(
            name = "geradorIdCidade",
            strategy = "com.super_bits.modulosSB.Persistencia.geradorDeId.GeradorIdCidade"
    )
    @GeneratedValue(generator = "geradorIdCidade")
    @InfoCampo(tipo = FabTipoAtributoObjeto.ID)
    private Long id;

    @InfoCampo(tipo = FabTipoAtributoObjeto.NOME, label = "Cidade", descricao = "Nome da Cidade")
    @NotNull
    @Column(nullable = false)
    private String nome;

    @InfoCampo(tipo = FabTipoAtributoObjeto.LC_UNIDADE_FEDERATIVA, label = "Estado", descricao = "Estado(ex:MG)")
    @ManyToOne(targetEntity = UnidadeFederativa.class, cascade = CascadeType.ALL)
    @NotNull
    private UnidadeFederativa unidadeFederativa;

    //bi-directional many-to-one association to Bairro
    @InfoCampo(tipo = FabTipoAtributoObjeto.LISTA_OBJETOS_PUBLICOS, label = "Bairros", descricao = "Bairros da Cidade")
    @OneToMany(mappedBy = "cidade")
    private List<Bairro> bairros;

    //bi-directional many-to-one association to Localidade
    @InfoCampo(tipo = FabTipoAtributoObjeto.LC_LOCALIDADE, label = "Localidade", descricao = "Localização no Estado")
    @ManyToOne(targetEntity = Localidade.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_Localidade")
    private Localidade localidade;

    @InfoCampo(tipo = FabTipoAtributoObjeto.LISTA_OBJETOS_PUBLICOS)
    @ManyToMany(mappedBy = "cidades", targetEntity = Regiao.class)
    private List<Regiao> regioes;

    @NotNull
    @InfoCampo(tipo = FabTipoAtributoObjeto.REG_ATIVO_INATIVO, label = "Status", descricao = "Status da Cidade")
    @InfoCampoVerdadeiroOuFalso(textoNegativo = "Inativo", textoPositivo = "Ativo")
    private boolean ativo = true;

    @Temporal(javax.persistence.TemporalType.DATE)
    @InfoCampo(tipo = FabTipoAtributoObjeto.REG_DATAALTERACAO, label = "Data alteração", descricao = "Data de alteração da cidade")
    private Date dataAlteracao = new Date();

    @Temporal(javax.persistence.TemporalType.DATE)
    @InfoCampo(tipo = FabTipoAtributoObjeto.REG_DATAINSERCAO, label = "Data criação", descricao = "Data de Criação da Cidade")
    private Date dataCriacao = new Date();

    public Cidade() {

        unidadeFederativa = new UnidadeFederativa();
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long pId) {
        id = pId;
    }

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public void setNome(String pNome) {
        pNome = pNome.replace("_", " ");
        this.nome = pNome;
        configIDPeloNome();

    }

    @Override
    public List<ComoBairro> getBairros() {
        return (List) this.bairros;
    }

    public void setBairros(List<Bairro> bairros) {
        this.bairros = bairros;
    }

    public Bairro addBairro(Bairro bairro) {
        getBairros().add(bairro);
        bairro.setCidade(this);

        return bairro;
    }

    public Bairro removeBairro(Bairro bairro) {
        getBairros().remove(bairro);
        bairro.setCidade(null);

        return bairro;
    }

    @Override
    public Localidade getLocalidade() {
        return this.localidade;
    }

    @Override
    public void setLocalidade(ComoLocalidade localidade) {
        this.localidade = (Localidade) localidade;
    }

    @Override
    public UnidadeFederativa getUnidadeFederativa() {
        return unidadeFederativa;
    }

    @Override
    public String getEstadoPontoNomeCidade() {

        return getUnidadeFederativa() + getNome();

    }

    @Override
    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public Date getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(Date dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public void setUnidadeFederativa(ComoUnidadeFederativa pUnidadeFederativa) {
        unidadeFederativa = (UnidadeFederativa) pUnidadeFederativa;
    }

    public List<Regiao> getRegioes() {
        return regioes;
    }

    public void setRegioes(List<Regiao> regioes) {
        this.regioes = regioes;
    }

    @Override
    public Long configIDPeloNome() {
        try {
            GeradorIdCidade gerador = new GeradorIdCidade();
            gerador.generate(null, this);
            return id;
        } catch (Throwable t) {

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "A identificação única da cidade não pôde ser gerada neste momento", t);
            return -1l;
        }

    }

    @Override
    public String toString() {
        return String.valueOf(getId());
    }

}
