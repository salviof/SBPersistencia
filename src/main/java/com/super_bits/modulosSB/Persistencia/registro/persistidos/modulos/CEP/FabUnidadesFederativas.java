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

    public static UnidadeFederativa getUF(String pUF) {
        for (FabUnidadesFederativas f : FabUnidadesFederativas.values()) {
            if (f.toString().toUpperCase().equals(pUF.toUpperCase())) {
                return f.getRegistro();
            }
        }
        return null;
    }

    @Override
    public UnidadeFederativa getRegistro() {
        UnidadeFederativa uf = new UnidadeFederativa();
        uf.setSigla(this.toString());
        uf.setUF(this.toString());
        uf.setNome(this.toString());
        switch (this) {
            case AC:
                uf.setNome("Acre");
                break;
            case AL:
                uf.setNome("Alagoas");
                break;
            case AM:
                uf.setNome("Amazonas");
                break;
            case AP:
                uf.setNome("Amapá");
                break;
            case BA:
                uf.setNome("Bahia");
                break;
            case CE:
                uf.setNome("Ceará");
                break;
            case DF:
                uf.setNome("Distrito Federal");
                break;
            case ES:
                uf.setNome("Espirito Santo");
                break;
            case GO:
                uf.setNome("Goias");
                break;
            case MA:
                uf.setNome("Maranhão");
                break;
            case MG:
                uf.setId(1l);
                uf.setNome("Minas Gerais");
                uf.setUF("MG");
                break;
            case MS:
                uf.setNome("Mato Grosso do Sul");
                break;
            case MT:
                uf.setNome("Mato Grosso");
                break;
            case PA:
                uf.setNome("Pará");
                break;
            case PB:
                uf.setNome("Paraiba");
                break;
            case PE:
                uf.setNome("Pernambuco");
                break;
            case PI:
                uf.setNome("Piauí");
                break;
            case PR:
                uf.setNome("Paraná");
                break;
            case RJ:
                uf.setNome("Rio de Janeiro");
                break;
            case RN:
                uf.setNome("Rio Grande do Norte");
                break;
            case RO:
                uf.setNome("Rondônia");
                break;
            case RR:
                uf.setNome("Roraima");
                break;
            case RS:
                uf.setNome("Rio Grande do Sul");
                break;
            case SC:
                uf.setNome("Santa Catarina");
                break;
            case SE:
                uf.setNome("Sergipe");
                break;
            case SP:
                uf.setNome("São Paulo");
                break;
            case TO:
                uf.setNome("Tocantins");
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
