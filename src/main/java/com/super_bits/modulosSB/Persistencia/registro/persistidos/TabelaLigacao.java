package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringValidador;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
import java.lang.reflect.Field;
import java.util.List;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

@SuppressWarnings("serial")
public abstract class TabelaLigacao extends EntidadeORMGenerica implements ItfTabelaLigacao {

    private ComoEntidadeSimples getcampo(int pNumero) {
        String nomeTabela = this.getClass().getSimpleName();
        List<String> tabelasStr = UtilSBCoreStringValidador.splitMaiuscula(nomeTabela);
        try {
            Field field = this.getClass().getDeclaredField(tabelasStr.get(pNumero - 1).toLowerCase());
            field.setAccessible(true);
            return (ComoEntidadeSimples) field.get(this);

        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro getCampo pelo numero em TabelaLigacao", e);

        }
        return null;

    }

    public ComoEntidadeSimples getCampo1() {
        return getcampo(1);
    }

    public ComoEntidadeSimples getCampo2() {
        return getcampo(2);

    }

    public ComoEntidadeSimples getCampoDiferente(String pParamentro) {

        List<String> tabelasStr = UtilSBCoreStringValidador.splitMaiuscula(this.getClass().getSimpleName());

        if (tabelasStr.get(0).equals(pParamentro)) {
            return getcampo(2);
        } else {
            return getcampo(1);
        }

    }

}
