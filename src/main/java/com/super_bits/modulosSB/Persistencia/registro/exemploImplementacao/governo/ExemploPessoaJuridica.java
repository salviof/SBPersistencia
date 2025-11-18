/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.exemploImplementacao.governo;

import com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP.Localizacao;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoUsuario;
import java.util.Date;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author desenvolvedor
 */
public class ExemploPessoaJuridica {

    @Id
    @InfoCampo(tipo = FabTipoAtributoObjeto.ID)
    @GeneratedValue()
    private Long id;
    @InfoCampo(tipo = FabTipoAtributoObjeto.NOME)
    private String nomeFantasia;
    @InfoCampo(tipo = FabTipoAtributoObjeto.NOME_LONGO)
    private String razaoSocial;

    @InfoCampo(tipo = FabTipoAtributoObjeto.CNPJ)
    private String cnpj;

//propriedades Entidade Normal
    @InfoCampo(tipo = FabTipoAtributoObjeto.DESCRITIVO)
    private String descritivo;
    @InfoCampo(tipo = FabTipoAtributoObjeto.REG_ATIVO_INATIVO)
    private boolean ativo;
    @InfoCampo(tipo = FabTipoAtributoObjeto.REG_DATAALTERACAO)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracao;
    @InfoCampo(tipo = FabTipoAtributoObjeto.REG_DATAINSERCAO)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataInsercao;
    //renomear
    @ManyToOne()
    private ComoUsuario usuarioAlteracao;
    @ManyToOne()
    private ComoUsuario usuarioInsercao;

    // propriedades Entidade Contato
    @InfoCampo(tipo = FabTipoAtributoObjeto.TELEFONE_FIXO_NACIONAL)
    private String telefone;

    @ManyToOne(targetEntity = Localizacao.class)
    private Localizacao localizacao;

    // Propriedades contato corporativo
    @InfoCampo(tipo = FabTipoAtributoObjeto.SITE)
    private String site;

}
