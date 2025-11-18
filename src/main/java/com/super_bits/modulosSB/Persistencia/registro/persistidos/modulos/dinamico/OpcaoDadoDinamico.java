/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.dinamico;

import com.super_bits.modulosSB.Persistencia.registro.persistidos.EntidadeSimplesORM;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.ListenerEntidadePadrao;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 *
 * @author SalvioF
 */
@Entity
@InfoObjetoSB(tags = "Opção Personalizaca", plural = "Opções Personalizadas")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoJPAOpcaoPersonalizada")
@EntityListeners(ListenerEntidadePadrao.class)
public class OpcaoDadoDinamico extends EntidadeSimplesORM {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @InfoCampo(tipo = FabTipoAtributoObjeto.ID)
    private Long id;

    @Column(nullable = false, updatable = false, insertable = false)
    private String tipoJPAOpcaoPersonalizada;

    @InfoCampo(tipo = FabTipoAtributoObjeto.NOME)
    private String nome;
    @InfoCampo(tipo = FabTipoAtributoObjeto.DESCRITIVO)
    @Column(columnDefinition = "TEXT", length = 8000)
    private String descricao;

    @ManyToOne(targetEntity = TipoDadoDinamico.class)
    @InfoCampo(tipo = FabTipoAtributoObjeto.OBJETO_DE_UMA_LISTA)
    private TipoDadoDinamico tipoDado;

    private String codigoGrupoOpcoes;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getNome() {
        return nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCodigoGrupoOpcoes() {
        return codigoGrupoOpcoes;
    }

    public void setCodigoGrupoOpcoes(String codigoGrupoOpcoes) {
        this.codigoGrupoOpcoes = codigoGrupoOpcoes;
    }

    public TipoDadoDinamico getTipoDado() {
        return tipoDado;
    }

    public void setTipoDado(TipoDadoDinamico tipoDado) {
        this.tipoDado = tipoDado;
    }

}
