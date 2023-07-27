package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP;

import com.super_bits.modulosSB.Persistencia.geradorDeId.GeradorIdBairro;
import com.super_bits.modulosSB.Persistencia.geradorDeId.GeradorIdCidade;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.EntidadeSimples;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.GenericGenerator;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoObjetoSB;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfBairro;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.cep.ItfCidade;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 * The persistent class for the bairro database table.
 *
 */
@Entity
@InfoObjetoSB(tags = {"Bairro"}, plural = "Bairros")
public class Bairro extends EntidadeSimples implements ItfBairro {

    @Id
    @GenericGenerator(
            name = "geradorIdBairro",
            strategy = "com.super_bits.modulosSB.Persistencia.geradorDeId.GeradorIdBairro"
    )
    @GeneratedValue(generator = "geradorIdBairro")
    @InfoCampo(tipo = FabTipoAtributoObjeto.ID)
    private int id;

    @InfoCampo(tipo = FabTipoAtributoObjeto.AAA_NOME, label = "Bairro")
    @NotNull
    private String nome;

    @InfoCampo(tipo = FabTipoAtributoObjeto.LC_CIDADE)
    @ManyToOne(targetEntity = Cidade.class, cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE})
    @NotNull
    private Cidade cidade;

    private String coordenadas;

    public Bairro() {
        cidade = new Cidade();

    }

    @Override
    public int getId() {
//TODO A VERIFICAR        configIDPeloNome();

        return this.id;
    }

    @Override
    public void setId(int pid) {
        id = pid;

    }

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public void setNome(String nome) {
        this.nome = nome;
        configIDPeloNome();

    }

    @Override
    public ItfCidade getCidade() {
        return (ItfCidade) this.cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    @Override
    public List<Long> getCordenadas() {
        throw new UnsupportedOperationException("Ainda não foi implementado");

    }

    @Override
    public void setCidade(ItfCidade pCidade) {
        cidade = (Cidade) pCidade;
    }

    @Override
    public int configIDPeloNome() {
        try {
            GeradorIdBairro gerador = new GeradorIdBairro();
            gerador.generate(null, this);
            return id;
        } catch (Throwable t) {

            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "A identificação única do bairro não pôde ser gerada neste momento", t);
            return -1;
        }
    }

}
