/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.registro.persistidos.modulos.CEP;

import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreStringsCammelCase;
import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabrica;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.coletivojava.fw.utilCoreBase.UtilSBCoreFabrica;

/**
 *
 * @author desenvolvedor
 */
public enum FabCidades implements ComoFabrica {

    BELO_HORIZONTE, CONTAGEM, Baldim,
    Betim, Brumadinho,
    Caeté,
    Capim_Branco,
    Confins,
    Esmeraldas,
    Florestal,
    Ibirité,
    Igarapé,
    Itaguara,
    Itatiaiuçu,
    Jaboticatubas,
    Juatuba,
    Lagoa_Santa,
    MArio_Campos,
    Mateus_Leme,
    Matozinhos,
    Nova_Lima,
    Nova_União,
    Pedro_Leopoldo,
    Raposos,
    Ribeirao_das_Neves,
    Rio_Acima,
    Rio_Manso,
    Sabara,
    Santa_Luzia,
    Sao_Joaquim_de_Bicas,
    São_Jose_da_Lapa,
    Sarzedo,
    Taquaracu_de_Minas,
    Vespasiano;

    @Override
    public Cidade getRegistro() {
        Cidade novaCidade = new Cidade();
        novaCidade.setUnidadeFederativa(FabUnidadesFederativas.MG.getRegistro());
        novaCidade.setNome(UtilSBCoreStringsCammelCase.getTextoByCammelPrimeiraLetraMaiuscula(this.toString()));
        novaCidade.setId(1l);
        switch (this) {

        }
        novaCidade.configIDPeloNome();
        return novaCidade;
    }

    public static List<Cidade> getCidadesPorEstado(FabUnidadesFederativas pUnidade) {
        List<Cidade> cidadesListadas = new ArrayList<>();
        List<Cidade> todasCidades = (List) UtilSBCoreFabrica.listaRegistros(FabCidades.class);
        for (Iterator<Cidade> iterator = todasCidades.iterator(); iterator.hasNext();) {
            Cidade next = iterator.next();
            if (next.getUnidadeFederativa().getSigla().toLowerCase().contains(pUnidade.getRegistro().getSigla().toLowerCase())) {
                cidadesListadas.add(next);
            }
        }
        return cidadesListadas;

    }

}
