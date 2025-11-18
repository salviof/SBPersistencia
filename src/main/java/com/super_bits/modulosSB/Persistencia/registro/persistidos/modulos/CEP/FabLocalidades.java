/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP;

import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabrica;

/**
 *
 * @author desenvolvedor
 */
public enum FabLocalidades implements ComoFabrica {

    GRANDE_BH;

    @Override
    public Localidade getRegistro() {
        Localidade novalocalidade = new Localidade();
        switch (this) {
            case GRANDE_BH:
                novalocalidade.addCidade(FabCidades.BELO_HORIZONTE.getRegistro());
                novalocalidade.addCidade(FabCidades.Baldim.getRegistro());
                novalocalidade.addCidade(FabCidades.Betim.getRegistro());
                novalocalidade.addCidade(FabCidades.Brumadinho.getRegistro());
                novalocalidade.addCidade(FabCidades.CONTAGEM.getRegistro());
                novalocalidade.addCidade(FabCidades.Caeté.getRegistro());
                novalocalidade.addCidade(FabCidades.Capim_Branco.getRegistro());
                novalocalidade.addCidade(FabCidades.Confins.getRegistro());
                novalocalidade.addCidade(FabCidades.Esmeraldas.getRegistro());
                novalocalidade.addCidade(FabCidades.Florestal.getRegistro());
                novalocalidade.addCidade(FabCidades.Ibirité.getRegistro());
                novalocalidade.addCidade(FabCidades.Igarapé.getRegistro());
                novalocalidade.addCidade(FabCidades.Itaguara.getRegistro());
                novalocalidade.addCidade(FabCidades.Itatiaiuçu.getRegistro());
                novalocalidade.addCidade(FabCidades.Jaboticatubas.getRegistro());
                novalocalidade.addCidade(FabCidades.Lagoa_Santa.getRegistro());
                novalocalidade.addCidade(FabCidades.MArio_Campos.getRegistro());
                novalocalidade.addCidade(FabCidades.Mateus_Leme.getRegistro());
                novalocalidade.addCidade(FabCidades.Matozinhos.getRegistro());
                novalocalidade.addCidade(FabCidades.Nova_Lima.getRegistro());
                novalocalidade.addCidade(FabCidades.Nova_União.getRegistro());
                novalocalidade.addCidade(FabCidades.Pedro_Leopoldo.getRegistro());
                novalocalidade.addCidade(FabCidades.Raposos.getRegistro());
                novalocalidade.addCidade(FabCidades.Ribeirao_das_Neves.getRegistro());
                novalocalidade.addCidade(FabCidades.Rio_Acima.getRegistro());
                novalocalidade.addCidade(FabCidades.Sabara.getRegistro());
                novalocalidade.addCidade(FabCidades.Santa_Luzia.getRegistro());
                novalocalidade.addCidade(FabCidades.Sao_Joaquim_de_Bicas.getRegistro());
                novalocalidade.addCidade(FabCidades.Sarzedo.getRegistro());
                novalocalidade.addCidade(FabCidades.São_Jose_da_Lapa.getRegistro());
                novalocalidade.addCidade(FabCidades.Taquaracu_de_Minas.getRegistro());
                novalocalidade.addCidade(FabCidades.Vespasiano.getRegistro());

                break;
            default:
                throw new AssertionError(this.name());

        }
        return novalocalidade;

    }
}
