/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP;

import com.super_bits.modulosSB.Persistencia.registro.persistidos.EntidadeSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfCidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfUnidadeFederativa;
import java.util.List;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author desenvolvedor
 */
@Entity
@InfoObjetoSB(tags = {"Estado do Brasil"}, plural = "Estados")
@Cacheable(true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UnidadeFederativa extends EntidadeSimples implements ItfUnidadeFederativa {

    @Id
    @GenericGenerator(
            name = "geradorIdNomeUnico",
            strategy = "com.super_bits.modulosSB.Persistencia.geradorDeId.GeradorIdNomeUnico"
    )
    @GeneratedValue(generator = "geradorIdNomeUnico")
    @InfoCampo(tipo = FabTipoAtributoObjeto.ID)
    private Long id;
    @InfoCampo(tipo = FabTipoAtributoObjeto.NOME_LONGO, label = "Estado")
    @NotNull
    private String nome;
    @InfoCampo(tipo = FabTipoAtributoObjeto.NOME, label = "Estado")
    private String UF;
    @OneToMany(targetEntity = Cidade.class, cascade = CascadeType.ALL, mappedBy = "unidadeFederativa")
    private List<Cidade> cidades;

    public UnidadeFederativa() {
        super();
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        configIDPeloNome();
    }

    @Override
    public String getNome() {

        return nome;

    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;

    }

    public String getUF() {
        return UF;
    }

    public void setUF(String UF) {
        this.UF = UF;
        configIDPeloNome();
    }

    @Override
    public List<ItfCidade> getCidades() {
        return (List) cidades;
    }

    @Override
    public String getSigla() {
        return UF;

    }

    @Override
    public void setSigla(String pSigla) {
        UF = pSigla;
        configIDPeloNome();
    }

    @Override
    public void setCidades(List<ItfCidade> pCidades) {
        cidades = (List) pCidades;
    }

    @Override
    public Long configIDPeloNome() {
        return super.configIDPeloNome(); //To change body of generated methods, choose Tools | Templates.
    }

}
