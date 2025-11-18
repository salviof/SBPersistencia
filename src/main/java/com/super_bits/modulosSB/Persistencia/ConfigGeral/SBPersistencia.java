/*
 *   Super-Bits.com CODE CNPJ 20.019.971/0001-90

 */
/**
 * Configurações gerais do modulo de Persistencia
 */
package com.super_bits.modulosSB.Persistencia.ConfigGeral;

import com.super_bits.modulosSB.Persistencia.dao.DriversConexao.DriverFWBancoJPANativo;
import com.super_bits.modulosSB.Persistencia.dao.DriversConexao.ItfDriverBanco;
import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.Persistencia.util.UtilSBPersistenciaFabricas;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UTilSBCoreInputs;

import com.super_bits.modulosSB.SBCore.modulos.fabrica.ComoFabrica;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.UtilSBCoreReflexaoCaminhoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.MapaObjetosProjetoAtual;
import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;

import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import org.apache.commons.io.IOUtils;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

public abstract class SBPersistencia {

    // VARIAVEIS DE SISTEMA
    /**
     * Informa o nome do PersistenceUnit padrão do sistema
     */
    private enum TipoBanco {

        MYSQL, ORACLE12
    }

    private static String nomeFactureManager = "UCPL";
    private static String formatoDataBanco = "yyyy-MM-dd";
    private static String formatoDataUsuario = "dd/MM/yy";
    private static String pastaImagensJPA = "/img";
    private static boolean configurado = false;
    private static final TipoBanco TIPO_BANCO = TipoBanco.MYSQL;
    private static DevOpsPersistencia devBanco;
    private static ItfDriverBanco driverFWBanco;
    private static final int MAXIMO_REGISTROS = 2000;

    private static Class<? extends ComoFabrica>[] fabricasRegistrosIniciais;

    public static void configuraJPA(ItfConfigSBPersistencia configurador) {
        configuraJPA(configurador, true, true);

    }

    /**
     *
     * Encontra o arquivo Java referente a esta classe na pasta modelRegras
     *
     * O método não Gera erro caso o arquivo não exista
     *
     * @param pClasseEntidade
     * @return
     */
    public static String getCaminhoArquivoModelJavaByClasse(Class pClasseEntidade) {
        try {
            if (!SBCore.isEmModoDesenvolvimento()) {
                throw new UnsupportedOperationException("O caminho do arquivo model só pode ser obtido em modo desenvolvimento");
            }

            return SBCore.getCaminhoGrupoProjetoSource() + "/modelRegras/src/main/java/" + pClasseEntidade.getCanonicalName().replaceAll("\\.", "/") + ".java";
        } catch (Throwable t) {
            return null;
        }
    }

    public static DevOpsPersistencia getDevOps() {
        return devBanco;
    }

    /**
     *
     * @param pClasse
     * @return Hash do arquivo javam ou 0 caso o arquivo não seja encontrado
     */
    public static long getHashCaminhoArquivoModelJava(Class pClasse) {
        try {
            String caminhoArquivp = getCaminhoArquivoModelJavaByClasse(pClasse);
            if (!new File(caminhoArquivp).exists()) {
                //  System.out.println("Nao encontrado [" + caminhoArquivp);
                return 0;
            }
            InputStream teste = UTilSBCoreInputs.getStreamByLocalFile(caminhoArquivp);

            return (Arrays.hashCode(IOUtils.toByteArray(teste)));
        } catch (Throwable ex) {
            return 0;
        }

    }

    private static void configurarCamposDeEntidade() {
        try {

            if (!MapaObjetosProjetoAtual.isObjetosConfigurados()) {
                EntityManager teste = UtilSBPersistencia.getNovoEM();
                Metamodel mm = teste.getEntityManagerFactory().getMetamodel();
                Set<EntityType<?>> entidades = mm.getEntities();
                List<Class> classesDeEntidades = new ArrayList<>();
                System.out.println("Configurando Campos de entidades");
                for (EntityType<?> entidade : entidades) {

                    classesDeEntidades.add(entidade.getJavaType());

                }
                UtilSBCoreReflexaoCaminhoCampo.configurarTodasAsClasses(classesDeEntidades);
                System.out.println("Campos de entidade configurados com sucesso" + classesDeEntidades);
            }

        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Ouve um erro configurando os campos de entidade ", t);
        }
    }

    public static void defineFacturyPadrao() {

    }

    /**
     *
     * @param configurador
     * @param pCriarTodosCampos Indica se o caminho para todos os campos deve
     * ser préconfigurado
     * @param pRecriarBanco
     */
    public static void configuraJPA(ItfConfigSBPersistencia configurador, boolean pCriarTodosCampos, boolean pRecriarBanco) {

        driverFWBanco = new DriverFWBancoJPANativo();
        nomeFactureManager = configurador.bancoPrincipal();
        formatoDataBanco = configurador.formatoDataBanco();
        formatoDataUsuario = configurador.formatoDataUsuario();
        pastaImagensJPA = configurador.pastaImagensJPA();
        fabricasRegistrosIniciais = configurador.fabricasRegistrosIniciais();
        configurado = true;

        devBanco = new DevOpsPersistencia(configurador);

        devBanco.iniciarBanco(pRecriarBanco);

        if (pCriarTodosCampos) {
            configurarCamposDeEntidade();
        }

    }

    public static boolean isConfigurado() {
        return configurado;
    }

    public static ItfDriverBanco getDriverFWBanco() {
        return driverFWBanco;
    }

    public static void criarRegistrosIniciais() {
        validaConfigurado();

        if (fabricasRegistrosIniciais != null) {
            for (Class classe : fabricasRegistrosIniciais) {
                EntityManager em = UtilSBPersistencia.getEMPadraoNovo();
                try {
                    UtilSBPersistenciaFabricas.persistirRegistrosDaFabrica(classe, em, UtilSBPersistenciaFabricas.TipoOrdemGravacao.ORDERNAR_POR_ID);
                } finally {
                    UtilSBPersistencia.fecharEM(em);
                }
            }
        }
    }

    /**
     * Valida se foi realizada a configuração do APP
     */
    private static void validaConfigurado() {
        if (configurado) {
            return;
        }
        try {
            System.out.println("CONFIG DO SBPERSISTENCIA NAO DEFINIDO !!");
            throw new UnsupportedOperationException("Erro o config da persistencia não foi defido");
        } catch (Throwable t) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, t.getMessage(), t);

        }

        if (SBCore.getEstadoAPP() == SBCore.ESTADO_APP.DESENVOLVIMENTO) {
            System.exit(0);
        } else {
            System.out.println("A configuração da persistencia não foi definida");
        }
        //    configCoreNaoDefinido.Alerta(ErroSB.TIPO_ERRO.PARA_TUDO, "CONFIG DO CORE NAO DEFINIDO");
    }

    public static String getNomeBancoPadrao() {
        validaConfigurado();
        return nomeFactureManager;
    }

    public static String getFormatoDataBanco() {
        validaConfigurado();
        return nomeFactureManager;
    }

    public static String getFormatoDataUsuario() {
        validaConfigurado();
        return nomeFactureManager;
    }

    public static String getPastaImagensJPA() {
        validaConfigurado();
        return pastaImagensJPA;
    }

    public static Date getDataFormatoBanco(String databanco) {
        validaConfigurado();
        SimpleDateFormat sd = new SimpleDateFormat(getFormatoDataBanco());
        try {
            Date data = sd.parse(databanco);
            return data;
        } catch (ParseException e) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro obtendo data no formato do banco" + databanco, e);
        }

        return null;
    }

    /**
     * Realiza o backup do banco principal, analiza o tipo de banco pela
     * variavel tipoBanco e executa as rotinas nescessárias
     *
     * A estrutura de pasta do arquivo é:
     * SBCore.getDiretorioBackup()/ano/mes/semana[1,2,3,4]/diadaSemana/banco.zip
     *
     * O arquivo de backup será criado com um nome temporário, após a
     * confirmação do sucesso seu nome é renomeado configurado
     *
     * As imagens da pastaImagensJPA também são copiadas em arquivo separado na
     * mesma pasta ImagensJPA.zip
     *
     *
     *
     * @return True se realizado com sucesso, False se não realizado com sucesso
     */
    public static synchronized boolean realizarBackupBancoPrincial() {
        EntityManager emBackup = UtilSBPersistencia.getNovoEM();
        throw new UnsupportedOperationException("Ainda não implementado");//TODO Edu
    }

    public static String getPastaExecucaoScriptsSQL() {
        if (SBCore.getEstadoAPP().equals(SBCore.ESTADO_APP.DESENVOLVIMENTO)
                || SBCore.getEstadoAPP().equals(SBCore.ESTADO_APP.HOMOLOGACAO)) {
            return SBCore.getCaminhoGrupoProjetoSource();
        } else {
            return "/home/git/publicados/" + SBCore.getGrupoProjeto() + "/";
        }

    }

    public static void limparBanco() {
        devBanco.limparBanco();
        devBanco.carregaBanco();
    }

    public static int getMAXIMO_REGISTROS() {
        return MAXIMO_REGISTROS;
    }

}
