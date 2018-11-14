/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreReflexao;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaCampo;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaDeEntidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ItfBeanSimples;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;
import org.coletivojava.fw.api.tratamentoErros.FabErro;
import org.hibernate.annotations.ManyToAny;

/**
 *
 * @author desenvolvedor
 */
public abstract class UtilSBPersistenciaValidacao {

    private static List<Class> getClassesComHierarquiaAteCotendoEntity(Class pClasse) {
        return UtilSBCoreReflexao.
                getClassesComHierarquiaAteCotendoEstaAnotacao(pClasse, Entity.class);

    }

    public static List<String> getCamposNaoIdentificadosHibernate(ItfBeanSimples pObjeto) {
        List<String> campos = new ArrayList<>();
        try {
            Class classeVinculada = pObjeto.getClass();
            EstruturaDeEntidade esEntidade = MapaObjetosProjetoAtual.getEstruturaObjeto(classeVinculada);

            for (EstruturaCampo estCampo : esEntidade.getCampos()) {
                if (estCampo.getFabricaTipoAtributo().equals(FabTipoAtributoObjeto.OBJETO_DE_UMA_LISTA)
                        || (estCampo.getFabricaTipoAtributo().equals(FabTipoAtributoObjeto.LISTA_OBJETOS_PUBLICOS)
                        || (estCampo.getFabricaTipoAtributo().equals(FabTipoAtributoObjeto.LISTA_OBJETOS_PARTICULARES)))) {
                    Field campo = classeVinculada.getDeclaredField(estCampo.getNomeDeclarado());

                    if (campo.getAnnotation(Transient.class) != null) {

                        continue;
                    }
                    if (campo.getAnnotation(ManyToAny.class) != null) {

                        continue;
                    }
                    if (campo.getAnnotation(ManyToMany.class) != null) {

                        continue;
                    }
                    if (campo.getAnnotation(ManyToOne.class) != null) {

                        continue;
                    }
                    if (campo.getAnnotation(OneToMany.class) != null) {

                        continue;
                    }
                    campos.add(estCampo.getNomeDeclarado());
                }
            }
            return campos;
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.PARA_TUDO, "Erro Tentando identificar campos não identificaveis", t);
            return campos;
        }
    }

}
