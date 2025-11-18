/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.super_bits.modulosSB.Persistencia.dao;

import com.super_bits.modulosSB.Persistencia.dao.consultaDinamica.ConsultaDinamicaDeEntidade;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaCampo;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaDeEntidade;
import com.super_bits.modulosSB.SBCore.modulos.grafico.ItemGraficoTotalPorTipo;
import com.super_bits.modulosSB.SBCore.modulos.grafico.ItfDadoGraficoTotal;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.TIPO_PRIMITIVO;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import com.super_bits.modulosSB.SBCore.modulos.objetos.estrutura.ItfEstruturaCampoEntidade;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoTemStatus;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoStatus;
import com.super_bits.modulosSB.SBCore.modulos.objetos.validador.ErroValidacao;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author salvio
 */
public class UtilSBPersistenciaGraficos {

    public static List<ItfDadoGraficoTotal> getTotaisPorStatus(Class<? extends ComoTemStatus> pEntidade, Class<? extends ComoStatus> pEntidadeORMStatus) throws ErroValidacao {
        if (pEntidade == null || pEntidadeORMStatus == null) {
            throw new ErroValidacao("Campos Entidade e status são obrigatórios");
        }
        try {
            EstruturaDeEntidade estrutura = MapaObjetosProjetoAtual.getEstruturaObjeto(pEntidade);
            Optional<ItfEstruturaCampoEntidade> campoStatus = estrutura.getCampos().stream()
                    .filter(cp -> cp.getTipoPrimitivoDoValor().equals(TIPO_PRIMITIVO.ENTIDADE)
                    )
                    .filter(cpObjeto
                            -> MapaObjetosProjetoAtual.isNomeEntidadeRegistrada(pEntidade.getSimpleName())
                    && pEntidadeORMStatus.getSimpleName().equals(cpObjeto.getClasseCampoDeclaradoOuTipoLista())
                    ).findFirst();
            if (!campoStatus.isPresent()) {
                throw new ErroValidacao("Campo Status não encontrado em " + pEntidade.getSimpleName());
            }

            return getTotaisPorTipo(pEntidade, campoStatus.get().getNomeDeclarado());
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro gerando dados de relatório", t);
            return null;
        }
    }

    public static List<ItfDadoGraficoTotal> getTotaisPorTipo(Class pEntidade, String pCampoTipo) {
        EstruturaDeEntidade entidade = MapaObjetosProjetoAtual.getEstruturaObjeto(pEntidade);
        EstruturaCampo campo = entidade.getCampoByNomeDeclarado(pCampoTipo);
        String tipo = campo.getClasseCampoDeclaradoOuTipoLista();
        Class classeTipo = MapaObjetosProjetoAtual.getClasseDoObjetoByNome(tipo);
        EntityManager em = UtilSBPersistencia.getEMPadraoNovo();

        try {
            List<ComoEntidadeSimples> itensTipos = UtilSBPersistencia.getListaTodos(classeTipo, em);
            return getTotaisPorTipo(pEntidade, itensTipos);

        } finally {
            UtilSBPersistencia.fecharEM(em);
        }

    }

    public static List<ItfDadoGraficoTotal> getTotaisPorTipo(Class pEntidade, List<? extends ComoEntidadeSimples> pTipos) {
        int i = 0;
        EntityManager em = UtilSBPersistencia.getEMPadraoNovo();
        List<ItfDadoGraficoTotal> itens = new ArrayList<>();
        try {
            for (ComoEntidadeSimples item : pTipos) {
                ConsultaDinamicaDeEntidade consuta = new ConsultaDinamicaDeEntidade(pEntidade, em);
                consuta.addCondicaoManyToOneIgualA("status", item);
                Long quantidade = consuta.resultadoSomarQuantidade();
                itens.add(new ItemGraficoTotalPorTipo(item, quantidade));
                i++;
            }
            return itens;
        } finally {
            UtilSBPersistencia.fecharEM(em);
        }
    }

}
