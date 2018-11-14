/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.dao.DriversConexao;

import com.super_bits.modulosSB.SBCore.modulos.fonteDados.FabTipoSelecaoRegistro;
import com.super_bits.modulosSB.Persistencia.dao.InfoPerisistirEntidade;
import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author desenvolvedor
 */
public interface ItfDriverBanco {

    public List<?> selecaoRegistros(EntityManager pEM,
            String pSQL, String pPQL, Integer maximo, Class tipoRegisto,
            UtilSBPersistencia.TIPO_SELECAO_REGISTROS pTipoSelecao, Object... parametros);

    public Object executaAlteracaoEmBancao(InfoPerisistirEntidade pInfoEntidadesPersistencia);

    public Object selecaoRegistro(EntityManager pEM, String pSQL, String pPQL, Class pClasseRegisto, FabTipoSelecaoRegistro pTipoSelecao, FabTipoAtributoObjeto pCampo, Object... parametros);

}
