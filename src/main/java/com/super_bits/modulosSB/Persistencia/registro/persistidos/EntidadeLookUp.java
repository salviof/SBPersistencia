package com.super_bits.modulosSB.Persistencia.registro.persistidos;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoLookUp;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

public class EntidadeLookUp extends EntidadeSimples {

    public EntidadeLookUp() {
        super();
        InfoLookUp info = this.getClass().getAnnotation(InfoLookUp.class);

        if (info == null) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Info lookup não anotado para" + this.getClass().getName(), null);

        }

    }

}
