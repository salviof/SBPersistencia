package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP;

import com.super_bits.modulosSB.Persistencia.registro.persistidos.EntidadeORMNormal;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ComoLocalidade;

/**
 * The persistent class for the localidade database table.
 *
 */
@Entity
@InfoObjetoSB(tags = {"Localidade "}, plural = "Localidades")
public class Localidade extends EntidadeORMNormal implements ComoLocalidade {

    public static Localidade grandeBH = new Localidade(1l, "Grande BH", "Belo horizonte, Contagem, Betim e Região");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @InfoCampo(tipo = FabTipoAtributoObjeto.ID)
    private Long id;
    @InfoCampo(tipo = FabTipoAtributoObjeto.DESCRITIVO)
    private String descricao;
    @InfoCampo(tipo = FabTipoAtributoObjeto.NOME)
    private String nome;

    @InfoCampo(tipo = FabTipoAtributoObjeto.REG_ATIVO_INATIVO, label = "Status", descricao = "Status da Localidade(ativo/inativo)")
    private boolean ativo;

    @Temporal(javax.persistence.TemporalType.DATE)
    @InfoCampo(tipo = FabTipoAtributoObjeto.REG_DATAALTERACAO, label = "Data Alteração", descricao = "Data de alteração da localidade")
    private Date dataAlteracao = new Date();

    //bi-directional many-to-one association to Cidade
    @OneToMany(mappedBy = "localidade")
    private List<Cidade> cidades;

    public Localidade() {
        super();

    }

    public Localidade(Long pID, String pNome, String pDescricao) {
        super();

        id = pID;
        nome = pNome;
        descricao = pDescricao;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescritivo() {
        return this.descricao;
    }

    public void setDescritivo(String descricao) {
        this.descricao = descricao;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Cidade> getCidades() {
        return this.cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }

    public Cidade addCidade(Cidade cidade) {
        getCidades().add(cidade);
        cidade.setLocalidade(this);

        return cidade;
    }

    public Cidade removeCidade(Cidade cidade) {
        getCidades().remove(cidade);
        cidade.setLocalidade(null);

        return cidade;
    }

    public static Localidade getGrandeBH() {
        return grandeBH;
    }

    public static void setGrandeBH(Localidade grandeBH) {
        Localidade.grandeBH = grandeBH;
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

}
