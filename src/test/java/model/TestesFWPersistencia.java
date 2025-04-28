/*
 *   Super-Bits.com CODE CNPJ 20.019.971/0001-90

 */
package model;

import config.ConfiguradorCoreSBSBpersistencia;
import config.ConfigPersistenciaExemplo;
import com.super_bits.modulosSB.Persistencia.ConfigGeral.SBPersistencia;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.Persistencia.registro.persistidos.testes.RegistroTesteSimples;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.fonteDados.FabTipoSelecaoRegistro;
import com.super_bits.modulosSB.SBCore.modulos.geradorCodigo.model.EstruturaCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campoInstanciado.ItfCampoInstanciado;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import java.util.List;
import org.junit.Test;
import testesFW.TesteJunitSBPersistencia;

/**
 *
 *
 *
 * @author Sálvio Furbino <salviof@gmail.com>
 * @since 20/07/2014
 *
 */
public class TestesFWPersistencia extends TesteJunitSBPersistencia {

    @Test
    public void teste() {
        try {

            EstruturaCampo estrutura = MapaObjetosProjetoAtual.getEstruturaObjeto("RegistroTesteSimples").getCampoByNomeDeclarado("objetoTeste");
            String classe = estrutura.getClasseCampoDeclaradoOuTipoLista();
            EstruturaCampo teste = MapaObjetosProjetoAtual.getEstruturaCampoPorCaminhoCompleto("RegistroTesteSimples.objetoTeste.nome");
            System.out.println(estrutura);

            List<RegistroTesteSimples> listagem = UtilSBPersistencia.getListaTodos(RegistroTesteSimples.class, getEMTeste());
            ItfCampoInstanciado campoInstanciado = listagem.get(0).getCampoInstanciadoByNomeOuAnotacao("id");
            SBCore.getServicoRepositorio().selecaoRegistros(null, null,
                    "from " + campoInstanciado.getNomeClasseOrigemAtributo() + " where " + campoInstanciado.getNome() + " = ?", 1, MapaObjetosProjetoAtual.getClasseDoObjetoByNome(campoInstanciado.getNomeClasseOrigemAtributo()), FabTipoSelecaoRegistro.NOMECURTO, 1);
            for (RegistroTesteSimples item : listagem) {
                System.out.println(item);
            }
        } catch (Throwable t) {
            lancarErroJUnit(t);
        }
    }

    @Override
    protected void configAmbienteDesevolvimento() {
        SBCore.configurar(new ConfiguradorCoreSBSBpersistencia(), SBCore.ESTADO_APP.DESENVOLVIMENTO);
        SBPersistencia.configuraJPA(new ConfigPersistenciaExemplo());
    }

}
