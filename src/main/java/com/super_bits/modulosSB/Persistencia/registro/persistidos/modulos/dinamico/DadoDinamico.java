/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.dinamico;

import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciadoDInamico.CampoInstanciadoDinamico;

import com.super_bits.modulosSB.Persistencia.registro.persistidos.EntidadeNormal;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.ListenerEntidadePadrao;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreReflexaoObjeto;

import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampoValidadorLogico;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.ItfDadoDinamico;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.ItfTipoAtributoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.TipoAtributoMetodosBase;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciado.CampoNaoImplementado;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciado.ItfAtributoObjetoEditavel;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciado.ItfAtributoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciado.ItfCampoInstanciado;
import java.lang.reflect.Field;
import java.util.Date;
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
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 *
 * O dado CRM é um registro de um dado que irá acontecer em determinada etapa do
 * sistema, e pode ser adicionado no sistema de maneira dinamica
 *
 * @author sfurbino
 */
@InfoObjetoSB(tags = "Informação complementar de prospecto", plural = "Dados CRM")
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoJPAdadoDinamico")
@EntityListeners(ListenerEntidadePadrao.class)
public class DadoDinamico extends EntidadeNormal implements ItfDadoDinamico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, updatable = false, insertable = false)
    private String tipoJPAdadoDinamico;

    @InfoCampo(tipo = FabTipoAtributoObjeto.AAA_NOME)
    private String nome;

    @InfoCampo(tipo = FabTipoAtributoObjeto.TEXTO_SIMPLES, label = "Dado a coletar")
    @Column(length = 2000)
    private String valor;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datahora;

    @Transient
    private Field campoValorReflection;

    @ManyToOne(targetEntity = TipoDadoDinamico.class, optional = false)
    private TipoDadoDinamico tipoDadoDinamico;

    @Transient
    private CampoCRMInstanciado campoCRMInstanciado;

    @InfoCampo(tipo = FabTipoAtributoObjeto.REG_DATAINSERCAO)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraCadastrou;
    @InfoCampo(tipo = FabTipoAtributoObjeto.REG_DATAALTERACAO)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataHoraEditou;

    @Transient
    @InfoCampoValidadorLogico()
    private boolean informacaoValidada;

    @Override
    public ItfCampoInstanciado getCampoInstanciado() {
        try {
            if (campoCRMInstanciado == null) {
                if (campoValorReflection == null) {
                    init();
                }
                System.out.println(tipoDadoDinamico.getLabel());

                if (tipoDadoDinamico == null) {
                    throw new UnsupportedOperationException("não foi possível determinar o formato do campo para o dado dinamico");
                }
                if (campoCRMInstanciado == null) {

                    campoCRMInstanciado = new CampoCRMInstanciado(campoValorReflection,
                            getTipoDadoDinamico());
                }

            }
        } catch (Throwable t) {
            if (SBCore.isEmModoProducao()) {
                return new CampoNaoImplementado();
            } else {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro obtendo campo instanciado do Dado", t);
            }
        }
        return campoCRMInstanciado;
    }

    public Object getObjetoDaLista() {
        return null;
    }

    @Override
    public FabTipoAtributoObjeto getTipoCampo() {
        return getCampoInstanciado().getFabricaTipoAtributo();
    }

    @Override
    public void setNome(String pNome) {
        nome = pNome;
    }

    @Override
    public String getValorEnpacotado() {
        return valor;
    }

    @Override
    public void setValorEmpacotado(String pValor) {
        valor = pValor;
    }

    private class CampoCRMInstanciado extends CampoInstanciadoDinamico implements ItfCampoInstanciado {

        public CampoCRMInstanciado(Field campoComOValor,
                ItfAtributoObjetoEditavel campoDinamico
        ) {
            super(campoComOValor, campoDinamico);

        }

        @Override
        public String getLabel() {
            return getTipoDadoDinamico().getLabel();
        }

        @Override
        public void setValor(Object pValor) {
            try {
                if (!isProximaTentativaDeAlteracaoBloqueado()) {
                    valor = (String) TipoAtributoMetodosBase.converterValorDinamicoEmString(this, pValor);
                } else {
                    desbloquearProximaAlteracao();
                }

            } catch (Throwable ex) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro set-valor atributo dinamico" + this.toString(), ex);

            }
        }

        @Override
        public Object getValor() {

            return TipoAtributoMetodosBase.converterStringDinamicoEmValorReal(this, valor);

        }

        @Override
        public Object getParent() {
            return getInstancia();
        }

        @Override
        public boolean validarCampo() {
            return true;

        }

        @Override
        public ItfAtributoObjetoSB getAtributosCampoDinamico() {
            return getTipoDadoDinamico();
        }

    }

    @Override
    public ItfCampoInstanciado getCampoInstanciadoByNomeOuAnotacao(String pNome) {
        if (pNome.equals("valor")) {
            return getCampoInstanciado();
        }
        return super.getCampoInstanciadoByNomeOuAnotacao(pNome); //To change body of generated methods, choose Tools | Templates.
    }

    private void init() {

        datahora = new Date();

        try {
            campoValorReflection = DadoDinamico.class.getDeclaredField("valor");
            campoValorReflection.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException ex) {
            SBCore.RelatarErro(FabErro.PARA_TUDO, "A reflexão do campo valor para o dado de CRM está inacessivel", ex);
        }

    }

    public DadoDinamico() {
        super();
        init();
    }

    @Override
    public String getNome() {
        return nome;
    }

    public String getValor() {
        return valor;
    }

    /**
     * Utilize getCampoinstanciado().serValor
     *
     * @param valor
     * @deprecated
     */
    @Deprecated()
    public void setValor(String valor) {
        getCampoInstanciado().setValor(valor);
    }

    @Override
    public ItfTipoAtributoSB getCampoPropriedades() {
        return tipoDadoDinamico;
    }

    public Date getDatahora() {
        return datahora;
    }

    public void setDatahora(Date datahora) {
        this.datahora = datahora;
    }

    public TipoDadoDinamico getTipoDadoDinamico() {
        return tipoDadoDinamico;
    }

    public void setTipoDadoDinamico(TipoDadoDinamico tipoDadoDinamico) {
        this.tipoDadoDinamico = tipoDadoDinamico;
        nome = tipoDadoDinamico.getLabel();
    }

    @PrePersist
    public void teste() {
        if (!SBCore.isEmModoProducao()) {
            System.out.println("Salvando dado dinamico");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipoJPAdadoDinamico() {
        return tipoJPAdadoDinamico;
    }

    public void setTipoJPAdadoDinamico(String tipoJPAdadoDinamico) {
        this.tipoJPAdadoDinamico = tipoJPAdadoDinamico;
    }

    public Date getDataHoraCadastrou() {
        return dataHoraCadastrou;
    }

    public void setDataHoraCadastrou(Date dataHoraCadastrou) {
        this.dataHoraCadastrou = dataHoraCadastrou;
    }

    public Date getDataHoraEditou() {
        return dataHoraEditou;
    }

    public void setDataHoraEditou(Date dataHoraEditou) {
        this.dataHoraEditou = dataHoraEditou;
    }

    @Override
    public String toString() {
        try {
            DadoDinamico itemSimples = (DadoDinamico) this;
            if (getInstancia().getClass().getSimpleName().contains("$")) {
                return UtilSBCoreReflexaoObjeto.getClassExtraindoProxy(getInstancia().getClass().getSimpleName()).getSimpleName() + "_" + itemSimples.getId();
            } else {
                return getInstancia().getClass().getSimpleName() + "_" + itemSimples.getId();
            }

        } catch (Throwable t) {
            return super.toString(); //To change body of generated methods, choose Tools | Templates.
        }

    }

    @Override
    public int hashCode() {
        try {
            return toString().hashCode();
        } catch (Throwable t) {
            return super.hashCode();
        }

    }

    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object obj) {
        try {
            return obj.hashCode() == getInstancia().hashCode();
        } catch (Throwable t) {
            return super.equals(obj);
        }
    }

    public boolean isInformacaoValidada() {
        return informacaoValidada;
    }

}
