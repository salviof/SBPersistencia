package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP;

import com.super_bits.modulosSB.Persistencia.registro.persistidos.EntidadeLocalizacao;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfBairro;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfCidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfLocal;
import javax.validation.constraints.Digits;
import org.coletivojava.fw.api.tratamentoErros.ErroPreparandoObjeto;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

@Entity
@InfoObjetoSB(tags = {"Localização"}, plural = "Localização")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipoLocalizacao")
public class Localizacao extends EntidadeLocalizacao implements ItfLocal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @InfoCampo(tipo = FabTipoAtributoObjeto.ID)
    private int id;

    @InfoCampo(tipo = FabTipoAtributoObjeto.AAA_NOME, label = "Logradouro", descricao = "Nome do Logradouro")
    @Column(length = 100)
    @NotNull
    private String nome;

    @ManyToOne(targetEntity = Bairro.class, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    @InfoCampo(tipo = FabTipoAtributoObjeto.LC_BAIRRO)
    @NotNull
    private Bairro bairro;

    @Transient
    private static List<UnidadeFederativa> unidadesFederativas;

    @InfoCampo(tipo = FabTipoAtributoObjeto.LONGITUDE)
    @Digits(fraction = 6, integer = 10000)
    private double longitude;

    @InfoCampo(tipo = FabTipoAtributoObjeto.LATITUDE)
    @Digits(fraction = 6, integer = 10000)
    private double latitude;

    @InfoCampo(tipo = FabTipoAtributoObjeto.LC_COMPLEMENTO_E_NUMERO)
    private String complemento;
    @Column(nullable = false, updatable = false, insertable = false)
    private String tipoLocalizacao;

    public Localizacao() {
        super();

    }

    @Override
    public void prepararNovoObjeto(Object... parametros) {
        try {
            super.prepararNovoObjeto(parametros); //To change body of generated methods, choose Tools | Templates.
        } catch (ErroPreparandoObjeto t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro preparando Objeto de Localizacao" + t.getMessage(), t);
        }
        bairro = new Bairro();
        bairro.setCidade(new Cidade());
        bairro.getCidade().setUnidadeFederativa(new UnidadeFederativa());
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public Bairro getBairro() {
        return bairro;
    }

    public List<UnidadeFederativa> getUnidadesFederativas() {
        if (unidadesFederativas == null) {
            unidadesFederativas = FabUnidadesFederativas.getTodos();
        }
        return unidadesFederativas;
    }

    public List<ItfCidade> getCidadesDisponiveis() {
        if (getBairro().getCidade().getUnidadeFederativa() == null) {
            throw new UnsupportedOperationException("Não é possível listar as cidades disponíveis antes de selecionar um Estado");
        }
        return getBairro().getCidade().getUnidadeFederativa().getCidades();

    }

    public List<Bairro> getBairrosDisponiveis() {
        if (getBairro().getCidade() == null) {
            throw new UnsupportedOperationException("Não é possível listar as cidades disponíveis antes de selecionar um Estado");
        }
        return (List) getBairro().getCidade().getBairros();

    }

    @Override
    public double getLongitude() {

        return this.longitude;

    }

    @Override
    public double getLatitude() {

        return this.latitude;

    }

    @Override
    public void setLatitude(double pLatitude) {

        this.latitude = pLatitude;

    }

    @Override
    public void setLongitude(double pLongitude) {

        this.longitude = pLongitude;

    }

    @Override
    public void setBairro(ItfBairro pBairro) {

        this.bairro = (Bairro) pBairro;

    }

    @Override
    public String getNome() {

        return this.nome;

    }

    @Override
    public void setNome(String pNome) {

        this.nome = pNome;

    }

    @Override
    public String getComplemento() {

        return this.complemento;

    }

    @Override
    public void setComplemento(String pComplemento) {

        this.complemento = pComplemento;

    }

    public String getTipoLocalizacao() {
        return tipoLocalizacao;
    }

    public void setTipoLocalizacao(String tipoLocalizacao) {
        this.tipoLocalizacao = tipoLocalizacao;
    }

}
