/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP;

import com.super_bits.modulosSB.SBCore.modulos.fabrica.ItfFabrica;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author desenvolvedor
 */
public enum FabUnidadesFederativas implements ItfFabrica {
    AC, AL, AM, AP, BA, CE, DF, ES, GO, MA, MG, MS, MT, PA, PB, PE, PI, PR, RJ, RN, RO, RR, RS, SC, SE, SP, TO;

    @Override
    public UnidadeFederativa getRegistro() {
        UnidadeFederativa uf = new UnidadeFederativa();
        uf.setUF(this.toString());
        uf.setNome(this.toString());
        switch (this) {
            case AC:

                break;
            case AL:
                break;
            case AM:
                break;
            case AP:
                break;
            case BA:
                break;
            case CE:
                break;
            case DF:
                break;
            case ES:
                break;
            case GO:
                break;
            case MA:
                break;
            case MG:
                uf.setId(1);
                uf.setNome("Minas Gerais");
                uf.setUF("MG");
                break;
            case MS:
                break;
            case MT:
                break;
            case PA:
                break;
            case PB:
                break;
            case PE:
                break;
            case PI:
                break;
            case PR:
                break;
            case RJ:
                break;
            case RN:
                break;
            case RO:
                break;
            case RR:
                break;
            case RS:
                break;
            case SC:
                break;
            case SE:
                break;
            case SP:
                break;
            case TO:
                break;
            default:
                throw new AssertionError(this.name());

        }
        return uf;
    }

    public static List<UnidadeFederativa> getTodos() {
        List<UnidadeFederativa> listaTodos = new ArrayList<>();
        for (ItfFabrica estFab : FabUnidadesFederativas.values()) {
            listaTodos.add((UnidadeFederativa) estFab.getRegistro());
        }
        return listaTodos;
    }

}
