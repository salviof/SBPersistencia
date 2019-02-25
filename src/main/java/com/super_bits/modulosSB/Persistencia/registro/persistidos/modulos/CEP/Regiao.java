/*
 *  Super-Bits.com CODE CNPJ 20.019.971/0001-90

 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP;

import com.super_bits.modulosSB.Persistencia.registro.persistidos.EntidadeSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.InfoGrupoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfLocal;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfRegiao;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 * ATENÇÃO A DOCUMENTAÇÃO DA CLASSE É OBRIGATÓRIA O JAVADOC DOS METODOS PUBLICOS
 * SÃO OBRIGATÓRIOS E QUANDO EXISTIR UMA INTERFACE DOCUMENTADA UMA REFERENCIA
 * DEVE SER CRIADA, A SINTAXE DE REFERENCIA É: @see_ NomeDAClasse#Metodo
 * DOCUMENTE DE FORMA OBJETIVA E EFICIENTE, NÃO ESQUEÇA QUE VOCÊ FAZ PARTE DE
 * UMA EQUIPE.
 *
 * @author <a href="mailto:salviof@gmail.com">Salvio Furbino</a>
 * @since 12/12/2015
 * @version 1.0
 *
 */
@Entity
@InfoObjetoSB(tags = {"Regiao", "Regiões"}, plural = "Regiões")
public class Regiao extends EntidadeSimples implements ItfRegiao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @InfoCampo(tipo = FabTipoAtributoObjeto.AAA_NOME, label = "Nome regiao", descricao = "Nome da região(ex: Triângulo Mineiro)", obrigatorio = true)
    @Column(unique = true)
    private String nomeRegiao;

    @InfoCampo(tipo = FabTipoAtributoObjeto.LISTA_OBJETOS_PUBLICOS, label = "Cidade", descricao = "Lista das cidades que compõem o estado selecionado")
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "regiao_cidades",
            joinColumns = @JoinColumn(name = "regiao_id"),
            inverseJoinColumns = @JoinColumn(name = "cidade_id"))
    @InfoGrupoCampo(camposDeclarados = {"unidadeFederativa", "nome"})
    private List<Cidade> cidades;

    @InfoCampo(tipo = FabTipoAtributoObjeto.LISTA_OBJETOS_PUBLICOS, label = "Bairros", descricao = "Bairros de uma dada região")
    @ManyToMany
    private List<Bairro> bairros;

    @InfoCampo(tipo = FabTipoAtributoObjeto.AAA_DESCRITIVO, label = "Sigla", descricao = "Sigla da Região")
    private String sigla;

    @InfoCampo(tipo = FabTipoAtributoObjeto.AAA_DESCRITIVO, label = "Quantidade Cidades", descricao = "Quantidade de cidades de uma região")
    private int quantidadeCidades;

    @InfoCampo(tipo = FabTipoAtributoObjeto.DATA, label = "Data Criação", descricao = "Data de cricação da região")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date criadoEm;

    @InfoCampo(tipo = FabTipoAtributoObjeto.DATA, label = "Data Alteração", descricao = "Data de alteração da região")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date alteradoEm;

    @InfoCampo(tipo = FabTipoAtributoObjeto.REG_ATIVO_INATIVO, label = "Status", descricao = "Status da Região (ativo/inativo)", somenteLeitura = true)
    @NotNull
    private boolean ativo;

    public String getNomeRegiao() {
        return nomeRegiao;
    }

    public void setNomeRegiao(String nomeRegiao) {
        this.nomeRegiao = nomeRegiao;
    }

    public List<Cidade> getCidades() {
        return cidades;
    }

    public void setCidades(List<Cidade> cidades) {
        this.cidades = cidades;
    }

    public List<Bairro> getBairros() {
        return bairros;
    }

    public void setBairros(List<Bairro> bairros) {
        this.bairros = bairros;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public int getQuantidadeCidades() {
        return quantidadeCidades;
    }

    public void setQuantidadeCidades(int quantidadeCidades) {
        this.quantidadeCidades = quantidadeCidades;
    }

    public Date getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(Date criadoEm) {
        this.criadoEm = criadoEm;
    }

    public Date getAlteradoEm() {
        return alteradoEm;
    }

    public void setAlteradoEm(Date alteradoEm) {
        this.alteradoEm = alteradoEm;
    }

    /**
     *
     * Verifica se o Local Pertence a região
     *
     *
     * @param pLocalidade
     * @return
     */
    public boolean isLocalidadeDaRegiao(ItfLocal pLocalidade) {

        boolean temBairro = true;
        boolean temCidade = true;
        boolean temEstado = true;
        if (getCidades().isEmpty()) {
            temCidade = false;
        }
        if (getBairros().isEmpty()) {
            temBairro = false;
        }

        if (temCidade) {

            if (getCidades().contains((Cidade) pLocalidade.getBairro().getCidade())) {
                if (temBairro) {
                    return getBairros().contains((Bairro) pLocalidade.getBairro());

                } else {
                    return true;
                }

            }

        } else if (temBairro) {
            return getBairros().contains((Bairro) pLocalidade.getBairro());
        }
        return false;

    }

}
