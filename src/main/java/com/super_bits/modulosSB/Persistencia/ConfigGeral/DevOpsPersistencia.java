/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.super_bits.modulosSB.Persistencia.ConfigGeral;

import com.super_bits.modulosSB.Persistencia.dao.UtilSBPersistencia;
import com.super_bits.modulosSB.Persistencia.util.UtilSBPersistenciaFabricas;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UTilSBCoreInputs;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreResources;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilSBCoreShellBasico;
import com.super_bits.modulosSB.SBCore.modulos.ManipulaArquivo.UtilSBCoreArquivoTexto;
import com.super_bits.modulosSB.SBCore.modulos.ManipulaArquivo.UtilSBCoreArquivos;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.UtilSBCoreReflexaoCaminhoCampo;
import com.super_bits.modulosSB.SBCore.modulos.testes.UtilSBCoreTestes;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

/**
 *
 * @author salvioF
 */
public class DevOpsPersistencia {

    private final String nomeArquivoPersistencia;
    private final ItfConfigSBPersistencia configurador;
    private String hashBancoGerado;

    private final String arqResCompilaBanco = "compilaBanco.sh";
    private final String arqResCarregaBanco = "carregaBanco.sh";
    private final String arqScriptFinal = "scriptFinal.sh";
    private final String arqResApagaBanco = "apagaBanco.sh";
    private final String arqResSBProjeto = "SBProjeto.prop";

    private String getPrexinoNomeArquivo() {

        // possui a palavra requisito
        if (nomeArquivoPersistencia.contains("equisit")) {
            return "req_";
        }
        // sem prefixo caso seja o model do projeto
        if (nomeArquivoPersistencia.contains("Model") || nomeArquivoPersistencia.contains("model")) {
            return "";
        }
        int finalPrefixo = 5;
        if (nomeArquivoPersistencia.length() < 6) {
            finalPrefixo = nomeArquivoPersistencia.length() + 1;
        }

        return nomeArquivoPersistencia.substring(0, finalPrefixo) + "_";
    }

    private String getARQUIVO_COMPILA_BANCO() {
        return getPrexinoNomeArquivo() + arqResCompilaBanco;
    }

    private String getARQUIVO_CARREGA_BANCO() {
        return getPrexinoNomeArquivo() + arqResCarregaBanco;
    }

    private String getARQUIVO_APAGA_BANCO() {
        return getPrexinoNomeArquivo() + arqResApagaBanco;
    }

    private String getARQUIVO_CONFIGURACOES() {
        return getPrexinoNomeArquivo() + arqResSBProjeto;
    }

    public final String getArqScriptFinal() {
        return arqScriptFinal;
    }

    private String getARQUIVO() {
        return getPrexinoNomeArquivo() + "hashbanco.info";
    }

    private final String DESTINO_ARQUIVO_SCRIPT_FINAL;
    private final String DESTINO_ARQUIVO_CARREGA_BANCO;
    private final String DESTINO_ARQUIVO_COMPILA_BANCO;
    private final String DESTINO_ARQUIVO_APAGA_BANCO;
    private final String DESTINO_ARQUIVO_CONFIGURACOES;
    private final String DESTINO_ARQUIVO_HASH_BANCO;

    /**
     *
     * @deprecated Apenas para compatibilidade para análise e reflexão da
     * classe, favor usar outro constructor
     */
    @Deprecated

    public DevOpsPersistencia() {

        this.nomeArquivoPersistencia = null;
        this.configurador = null;
        this.DESTINO_ARQUIVO_SCRIPT_FINAL = null;
        this.DESTINO_ARQUIVO_CARREGA_BANCO = null;
        this.DESTINO_ARQUIVO_COMPILA_BANCO = null;
        this.DESTINO_ARQUIVO_APAGA_BANCO = null;
        this.DESTINO_ARQUIVO_CONFIGURACOES = null;
        this.DESTINO_ARQUIVO_HASH_BANCO = null;
    }

    public DevOpsPersistencia(ItfConfigSBPersistencia pConfig) {
        nomeArquivoPersistencia = pConfig.bancoPrincipal();
        configurador = pConfig;

        DESTINO_ARQUIVO_CARREGA_BANCO = SBPersistencia.getPastaExecucaoScriptsSQL() + "/" + getARQUIVO_CARREGA_BANCO();
        DESTINO_ARQUIVO_COMPILA_BANCO = SBPersistencia.getPastaExecucaoScriptsSQL() + "/" + getARQUIVO_COMPILA_BANCO();
        DESTINO_ARQUIVO_APAGA_BANCO = SBPersistencia.getPastaExecucaoScriptsSQL() + "/" + getARQUIVO_APAGA_BANCO();
        DESTINO_ARQUIVO_CONFIGURACOES = SBPersistencia.getPastaExecucaoScriptsSQL() + "/" + getARQUIVO_CONFIGURACOES();
        DESTINO_ARQUIVO_HASH_BANCO = SBPersistencia.getPastaExecucaoScriptsSQL() + "/" + getARQUIVO();
        DESTINO_ARQUIVO_SCRIPT_FINAL = SBPersistencia.getPastaExecucaoScriptsSQL() + "/" + getArqScriptFinal();

        if (SBCore.isEmModoDesenvolvimento()) {
            criaScriptsBancoDeDAdos(configurador);
        }

    }

    private long gerarHashBanco() {
        long hashBancoAtual = 0;
        for (Class entidade : UtilSBPersistencia.getTodasEntidades()) {
            hashBancoAtual += UtilSBCoreResources.getHashCodeClasseDoPacote(entidade);
            if (UtilSBCoreReflexaoCaminhoCampo.isClasseBasicaSB(entidade)) {
                throw new UnsupportedOperationException("O Nome da entidade [" + entidade.getSimpleName() + "] é um nome reservado do sistema");
            }
        }
        for (Class fabrica : configurador.fabricasRegistrosIniciais()) {
            hashBancoAtual += UtilSBCoreResources.getHashCodeClasseDoPacote(fabrica);
        }
        hashBancoAtual += UtilSBCoreResources.getHashCodeClasseDoPacote(configurador.getClass());

        hashBancoAtual = Math.abs(hashBancoAtual);
        return hashBancoAtual;
    }

    private void limparCodigoHash() {
        UtilSBCoreArquivoTexto.escreverEmArquivoSubstituindoArqAnterior(DESTINO_ARQUIVO_HASH_BANCO, "0000");
    }

    private boolean houveAlteracaoHomologacaoBanco(ItfConfigSBPersistencia configurador) {

        if (!new File(DESTINO_ARQUIVO_HASH_BANCO).exists()) {
            limparCodigoHash();
        }

        String alteracaoAnterior = UTilSBCoreInputs.getStringByArquivoLocal(DESTINO_ARQUIVO_HASH_BANCO);
        Long altAnterior = NumberUtils.toLong(alteracaoAnterior);
        Long hashBancoAtual = NumberUtils.toLong(hashBancoGerado);
        long diferenca = hashBancoAtual - altAnterior;
        return diferenca != 0;

    }

    public void compilaBanco() {

        if (SBCore.getEstadoAPP() != SBCore.ESTADO_APP.DESENVOLVIMENTO) {
            throw new UnsupportedOperationException("A compilação do banco só pode ser realizada em modo desenvolvimento");
        }

        File script = new File(DESTINO_ARQUIVO_COMPILA_BANCO);

        if (!script.exists()) {
            throw new UnsupportedOperationException("O arquivo de script para compilar o banco não foi encontrado em " + script);
        }

        UtilSBCoreArquivoTexto.substituirEstaLinha(DESTINO_ARQUIVO_COMPILA_BANCO, "source ./" + getARQUIVO_CONFIGURACOES(), 2);
        UtilSBCoreArquivoTexto.escreverEmArquivoSubstituindoArqAnterior(DESTINO_ARQUIVO_HASH_BANCO, hashBancoGerado);

    }

    public String getHashBancoGerado() {
        return hashBancoGerado;
    }

    public final void criaScriptsBancoDeDAdos(ItfConfigSBPersistencia pConfigurador) {
        //UtilSBCore String caminhosScript = (SBCore.getCaminhoGrupoProjetoSource() + "/complaBanco.sh");

        //  File arquivoApaBanco = new File(DESTINO_ARQUIVO_APAGA_BANCO);
        //   if (!arquivoApaBanco.exists()) {
        Class classeDoResource = pConfigurador.getClass();
        String linha = "source ./" + getPrexinoNomeArquivo() + arqResSBProjeto;
        UtilSBCoreArquivos.copiarArquivoResourceJar(classeDoResource, arqResSBProjeto, DESTINO_ARQUIVO_CONFIGURACOES);

        UtilSBCoreArquivos.copiarArquivoResourceJar(classeDoResource, arqResApagaBanco, DESTINO_ARQUIVO_APAGA_BANCO);
        if (!UtilSBCoreArquivos.tornarExecutavel(DESTINO_ARQUIVO_APAGA_BANCO)) {
            throw new UnsupportedOperationException("Erro tornando arquivo apagar banco executavel");
        }
        UtilSBCoreArquivoTexto.substituirEstaLinha(DESTINO_ARQUIVO_APAGA_BANCO, linha, 2);
        UtilSBCoreArquivos.copiarArquivoResourceJar(classeDoResource, arqResCompilaBanco, DESTINO_ARQUIVO_COMPILA_BANCO);
        if (!UtilSBCoreArquivos.tornarExecutavel(DESTINO_ARQUIVO_COMPILA_BANCO)) {
            throw new UnsupportedOperationException("Erro tornando arquivo compilabanco executavel");
        }
        UtilSBCoreArquivoTexto.substituirEstaLinha(DESTINO_ARQUIVO_COMPILA_BANCO, linha, 2);
        UtilSBCoreArquivos.copiarArquivoResourceJar(classeDoResource, arqResCarregaBanco, DESTINO_ARQUIVO_CARREGA_BANCO);
        if (!UtilSBCoreArquivos.tornarExecutavel(DESTINO_ARQUIVO_CARREGA_BANCO)) {
            throw new UnsupportedOperationException("Erro tornando arquivo carrega executavel");
        }
        UtilSBCoreArquivoTexto.substituirEstaLinha(DESTINO_ARQUIVO_CARREGA_BANCO, linha, 2);

        //UtilSBCoreShellBasico.executeCommand(false, "chmod u+x " + SBPersistencia.getPastaExecucaoScriptsSQL()+"/*.sh");
        //System.out.println("Retorno CHNOD   "+ getPastaExecucaoScriptsSQL() + "/*.sh>>" + retorno);
    }

    public void execScriptFinal() {
        if (SBCore.getEstadoAPP() != SBCore.ESTADO_APP.DESENVOLVIMENTO) {
            throw new UnsupportedOperationException("o carregamento automatico do banco só pode ser realizado em modo desenvolvimento");
        }

        if (new File(DESTINO_ARQUIVO_SCRIPT_FINAL).exists()) {
            String retornoCarrregaBanco = UtilSBCoreShellBasico.executeCommand(DESTINO_ARQUIVO_SCRIPT_FINAL);
            System.out.println(retornoCarrregaBanco);
        } else {
            System.out.println("Este projeto não tem script Final para ser executado");
            System.out.println(DESTINO_ARQUIVO_CARREGA_BANCO + " -> não foi executado");
        }

    }

    public void carregaBanco() {
        if (SBCore.getEstadoAPP() != SBCore.ESTADO_APP.DESENVOLVIMENTO) {
            throw new UnsupportedOperationException("o carregamento automatico do banco só pode ser realizado em modo desenvolvimento");
        }
        //  IO.co tring teste;
        File script = new File(DESTINO_ARQUIVO_CARREGA_BANCO);
        if (!script.exists()) {
            throw new UnsupportedOperationException("O arquivo de script para carregar banco não foi encontrado em " + script);
        }
        UtilSBCoreArquivoTexto.substituirEstaLinha(DESTINO_ARQUIVO_CARREGA_BANCO, "source ./" + getARQUIVO_CONFIGURACOES(), 1);
        String retornoCarrregaBanco = UtilSBCoreShellBasico.executeCommand(DESTINO_ARQUIVO_CARREGA_BANCO);
        System.out.println("Retorno Carrega Banco" + retornoCarrregaBanco);
        execScriptFinal();
    }

    public void carregaBancoSeBancoVazio() {
        File script = new File(DESTINO_ARQUIVO_CARREGA_BANCO);
        //if (!testeConexao()) {

        //}
        if (!script.exists()) {
            throw new UnsupportedOperationException("O arquivo de script para carregar banco não foi encontrado em " + script);
        }
        UtilSBCoreArquivoTexto.substituirEstaLinha(DESTINO_ARQUIVO_CARREGA_BANCO, "source ./" + getARQUIVO_CONFIGURACOES(), 1);
        String retornoCarrregaBanco = UtilSBCoreShellBasico.executeCommand(DESTINO_ARQUIVO_CARREGA_BANCO);
        System.out.println("Retorno Carrega Banco" + retornoCarrregaBanco);
        execScriptFinal();
    }

    public void limparBanco() {

        if (SBCore.getEstadoAPP() != SBCore.ESTADO_APP.DESENVOLVIMENTO) {
            throw new UnsupportedOperationException("A limpeza do banco só pode ser realizada em modo desenvolvimento");
        }
        String caminhosScript = DESTINO_ARQUIVO_APAGA_BANCO;
        File script = new File(caminhosScript);
        if (!script.exists()) {
            throw new UnsupportedOperationException("O arquivo de script para apagar banco não foi encontrado em " + script);
        }
        String retornoApagaBanco = "";
        try {
            UtilSBCoreArquivoTexto.substituirEstaLinha(caminhosScript, "source ./" + getARQUIVO_CONFIGURACOES(), 2);
            retornoApagaBanco = UtilSBCoreShellBasico.executeCommand(caminhosScript);
        } catch (Throwable t) {
            if (!t.getMessage().contains("doesn't exist")) {
                throw new UnsupportedOperationException("Ocorreu um erro inesperado" + t.getMessage(), t);
            } else {
                retornoApagaBanco = "dropped (não existia o banco)";
            }
        }
        System.out.println("Retorno Apaga banco=" + retornoApagaBanco);
        if (!retornoApagaBanco.contains("dropped")) {
            throw new UnsupportedOperationException("A palavra dropped não apareceu no retorno do comando apagaBanco.sh que integra as boas práticas de Devops do frameWork" + retornoApagaBanco);
        }

        //criarRegistrosIniciais();
    }

    public void loadC3p0(Map<String, Object> pPropriedades) {
        // desabilitando hbm2dllauto por segurança
        //pPropriedades.put("javax.persistence.sharedCache.mode", "NONE");

        //pPropriedades.put("hibernate.cache.use_query_cache", "false");
        pPropriedades.put("hibernate.event.merge.entity_copy_observer", "allow");
        //TEntativa de diminuir utilização de memória baseado em https://stackoverflow.com/questions/24359088/high-memory-usage-when-using-hibernate
        //pPropriedades.put("hibernate.query.plan_cache_max_soft_references", 2048);
        //pPropriedades.put("hibernate.query.plan_cache_max_strong_references", 128);
        // Número de conexõs que o pool tentará adiquirur durante a inicialização. Deve
        // ser um número entre  minPoolSize e maxPoolSize.
        pPropriedades.put("hibernate.c3p0.initialPoolSize", 2);
        //Número mínimo de conexões que o pool irá manter
        pPropriedades.put("hibernate.c3p0.min_size", 2);
        //Número máximo de conexões que o pool irá manter.c3p0.maxPoolSize
        pPropriedades.put("hibernate.c3p0.max_size", "500");//30
        //Segundos que uma Conexão será mantida no pool sem ser usada antes de ser
        //descartada.Zero significa que a conexão nunca expira
        pPropriedades.put("hibernate.c3p0.maxIdleTime", 5);

        /**
         * O tamanho do cache do C3P0 para PreparedStatements. Se o valor de
         * ambos, maxStatements e maxStatementsPerConnection, é zero, o cache
         * será desabilitado. Se maxStatements é zero mas
         * maxStatementsPerConnection é um valor diferente de zero, o cache será
         * habilitado, mas sem um limite global, apenas com um limite por
         * conexão. maxStatements controla o número total de Statements dos
         * quais é feito cache, para todas as conexões. Se setado, deve ser um
         * valor relativamente alto, já que cada Conexão do pool terá um
         * determinado número de statements colocado em cache. Como um exemplo,
         * considere quantos PreparedStatements distintos são frequentemente
         * usados na sua aplicação e multiplique esse /número por maxPoolSize
         * para chegar num valor apropriado. Apesar do parâmetro maxStatements
         * ser o padrão para o JDBC controlar o cache de statements, usuários
         * podem achar mais intuitivo o uso do parâmetro
         * maxStatementsPerConnection.
         */
        pPropriedades.put("hibernate.c3p0.max_statements", 0);//100
        /**
         * # O número de PreparedStatements que o c3p0 irá colocar em cache,
         * para cada conexão # do pool. Se ambos maxStatements e
         * maxStatementsPerConnection são zero, o cache # de consultas ficará
         * inativo. Se maxStatementsPerConnection é zero, mas maxStatements # é
         * um valor não nulo, o cache de consultas será habilitado, e um limite
         * global # imposto, mas por outro lado, não existirá nenhum limite
         * individual por conexão. # Se setado, maxStatementsPerConnection
         * deveria ser um valor, aproximado, do número # de PreparedStatements,
         * distintos, que são frequentemente usados na sua aplicação # mais dois
         * ou três, para que as consultas menos comuns não tirem as mais comuns
         * # do cache. Apesar de maxStatements ser o parâmetro padrão em JDBC
         * para controlar # o cache de consultas, o usuário pode achar mais
         * intuitivo usar o parâmetro # maxStatementsPerConnection.
         */
        pPropriedades.put("hibernate.c3p0.maxStatementsPerConnection", 0);//10

        /**
         * # Determina quantas conexões por vez o c3p0 tenta adquirir quando o
         * pool não tem # conexões inativas para serem usadas.
         */
        pPropriedades.put("hibernate.c3p0.acquire_increment", 1);//10
        /**
         * Se idleConnectionTestPeriod é um número maior que zero, c3p0 irá
         * testar todas # as conexões inativas, que estão no pool e não fizeram
         * o check-out, de X em X # segundos, onde X é o valor de
         * idleConnectionTestPeriod.
         */
        pPropriedades.put("hibernate.c3p0.idle_test_period", 0);//60
        /**
         * O número de milisegundos que um cliente chamando getConnection() irá
         * esperar # por uma Conexão, via check-in ou uma nova conexão adquirida
         * quando o pool estiver # esgotado. Zero siginifica esperar
         * indefinidademento. Setar qualquer valor positivo # causará um
         * time-out com uma SQLException depois de passada a quantidade
         * especificada # de milisegundos.
         */
        pPropriedades.put("hibernate.c3p0.checkoutTimeout", 5000);
        /**
         * Tempo em milisegundos que o c3p0 irá esperar entre tentivas de
         * aquisição.
         */
        pPropriedades.put("hibernate.c3p0.acquireRetryDelay", 1000);
        /**
         * Define quantas vezes o c3p0 tentará adquirir uma nova Conexão da base
         * de dados # antes de desistir. Se esse valor é menor ou igual a zero,
         * c3p0 tentará adquirir # uma nova conexão indefinidamente.
         */
        pPropriedades.put("hibernate·c3p0.acquireRetryAttempts", 5);

        /**
         * # Se true, um pooled DataSource declarará a si mesmo quebrado e
         * ficará permanentemente # fechado caso não se consiga uma Conexão do
         * banco depois de tentar acquireRetryAttempts # vezes. Se falso, o
         * fracasso para obter uma Conexão jogará uma exceção, porém # o
         * DataSource permanecerá valido, e tentará adquirir novamente, seguindo
         * uma nova # chamada para getConnection().
         */
        pPropriedades.put("hibernate.c3p0.breakAfterAcquireFailure", false);
        /**
         * Número de segundos que conexões acima do limite minPoolSize deverão
         * permanecer # inativas no pool antes de serem fechadas. Destinado para
         * aplicações que desejam reduzir agressivamente o número de conexões
         * abertas, diminuindo o pool novamente #para minPoolSize, se, seguindo
         * um pico, o nível de load diminui e Conexões não são mais requeridas.
         * Se maxIdleTime está definido, maxIdleTimeExcessConnections deverá ser
         * um valor menor para que o parâmetro tenho efeito. Zero significa que
         * não existirá nenhuma imposição, Conexões em excesso não serão mais
         * fechadas.
         */
        pPropriedades.put("hibernate.c3p0.maxIdleTimeExcessConnections", 5);//5
        /**
         * c3p0 é muito assíncrono. Operações JDBC lentas geralmente são
         * executadas por # helper threads que não detém travas de fechamento.
         * Separar essas operações atravéz # de múltiplas threads pode melhorar
         * significativamente a performace, permitindo # que várias operações
         * sejam executadas ao mesmo tempo.
         *
         */
        pPropriedades.put("hibernate.c3p0.numHelperThreads", 10);//3
        /**
         * # Se true, e se unreturnedConnectionTimeout está definido com um
         * valor positivo, então o pool capturará a stack trace (via uma
         * exceção) de todos os checkouts de Conexões, e o stack trace será
         * impresso quando o checkout de Conexões der timeout. Este paramêtro é
         * destinado para debug de aplicações com leak de Conexões, isto é,
         * aplicações que ocasionalmente falham na liberação/fechamento de
         * Conexões, ocasionando o crescimento do pool, e eventualmente na sua
         * exaustão (quando o pool atinge maxPoolSize com todas as suas conexões
         * em uso e perdidas). # Este paramêtro deveria ser setado apenas para
         * debugar a aplicação, já que capturar o stack trace deixa mais o lento
         * o precesso de check-out de Conexões.
         *
         */
        pPropriedades.put("hibernate.c3p0.debugUnreturnedConnectionStackTraces", false);
        /**
         * # Segundos. Se setado, quando uma aplicação realiza o check-out e
         * falha na realização # do check-in [i.e. close()] de um Conexão,
         * dentro de período de tempo especificado, # o pool irá, sem
         * cerimonias, destruir a conexão [i.e. destroy()]. Isto permite # que
         * aplicações com ocasionais leaks de conexão sobrevivam, ao invéz de
         * exaurir # o pool. E Isto é uma pena. Zero significa sem timeout,
         * aplicações deveriam fechar # suas próprias Conexões. Obviamente, se
         * um valor positivo é definido, este valor # deve ser maior que o maior
         * valor que uma conexão deveria permanecer em uso. Caso # contrário, o
         * pool irá ocasionalmente matar conexões ativas, o que é ruim. Isto #
         * basicamente é uma péssima idéia, porém é uma funcionalidade pedida
         * com frequência. # Consertem suas aplicações para que não vazem
         * Conexões!!! Use esta funcionalidade # temporariamente em combinação
         * com debugUnreturnedConnectionStackTraces para # descobrir onde as
         * conexões esão vazando! (Determina também o tempo maximo entre o
         * inicio e fim de uma tranzação)
         */
        pPropriedades.put("hibernate.c3p0.unreturnedConnectionTimeout", 600);

        //    propriedades.put("hibernate.c3p0.timeout", 10);
        pPropriedades.put("hibernate.connection.provider_class", "org.hibernate.connection.C3P0ConnectionProvider");

    }

    public void carregarConfiguracaoBasicaPadraoMysql(Map<String, Object> pPropriedades) {
        pPropriedades.put("javax.persistence.sharedCache.mode", "ENABLE_SELECTIVE");
        /// pPropriedades.put("org.hibernate.cacheable", "false");
        //pPropriedades.put("hibernate.cache.use_query_cache", "false");
        //pPropriedades.put("hibernate.cache.use_second_level_cache", "false");
        pPropriedades.put("org.hibernate.cacheable", true);

        pPropriedades.put("hibernate.cache.use_query_cache", true);
        /// Cuidado com este cara, ele faz entityanager.refresh deixar de atualizar a entidade pelo banco
        pPropriedades.put("hibernate.cache.use_second_level_cache", true);
        pPropriedades.put("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
        pPropriedades.put("hibernate.event.merge.entity_copy_observer", "allow");
    }

    public void carregarDadosConexaoPadrao(Map<String, Object> pPropriedades) {
        if (SBCore.isEmModoProducao()) {

            String senhaBanco = "senhaEspacoDockerProtegido#123";
            String hostBanco = "banco_rel_intranet";

            if (System.getenv("JAVA_EE_BANCO_SENHA") != null) {
                senhaBanco = System.getenv("JAVA_EE_BANCO_SENHA");
            }
            if (System.getenv("JAVA_EE_BANCO_HOST") != null) {
                hostBanco = System.getenv("JAVA_EE_BANCO_HOST");
            }

            String nomeBanco = SBPersistencia.getNomeBancoPadrao();
            if (System.getenv("JAVA_EE_BANCO_NOME") != null) {
                nomeBanco = System.getenv("JAVA_EE_BANCO_NOME");
            }
            //String nomeSlugLegado = SBPersistencia.getNomeBancoPadrao() + "." + SBCore.getGrupoProjeto() + "." + SBCore.DOMINIO_FICTICIO_INTRANET_DOCKER + "/" + SBPersistencia.getNomeBancoPadrao();
            pPropriedades.put("javax.persistence.jdbc.url", "jdbc:mysql://" + hostBanco + "/" + nomeBanco + "?createDatabaseIfNotExist=true&useSSL=false");
            pPropriedades.put("javax.persistence.jdbc.password", senhaBanco);
        } else {
            pPropriedades.put("javax.persistence.jdbc.url", "jdbc:mysql://localhost/" + SBPersistencia.getNomeBancoPadrao() + "?createDatabaseIfNotExist=true&useSSL=false");
            pPropriedades.put("javax.persistence.jdbc.password", "senhaDev#123");
        }
    }

    public void carregarAutoLoadPadrao(Map<String, Object> pPropriedades) {
        if (SBCore.isEmModoDesenvolvimento()) {
            // desabilitando criação de banco de dados no início caso o banco seja o mesmo
            pPropriedades.put("hibernate.hbm2ddl.auto", null);
            // Mostrar SQL
            pPropriedades.put("hibernate.show_sql", true);
            //Mostrar SQL formatado
            pPropriedades.put("hibernate.format_sql", true);
            //Mostrar comentários explicativos
            pPropriedades.put("hibernate.use_sql_comments", true);

        } else {

            // desabilitando hbm2dllauto por segurança
            pPropriedades.put("hibernate.hbm2ddl.auto", null);
            // Mostrar SQL
            pPropriedades.put("hibernate.show_sql", false);
            //Mostrar SQL formatado
            pPropriedades.put("hibernate.format_sql", false);
            //Mostrar comentários explicativos
            pPropriedades.put("hibernate.use_sql_comments", false);
            // Cofiguracoes de C3p0
        }
    }

    public void iniciarBanco(boolean pRecriarBanco) {

        Map<String, Object> propriedades = new HashMap<>();
        carregarConfiguracaoBasicaPadraoMysql(propriedades);
        carregarDadosConexaoPadrao(propriedades);

        //Habilita funcionamento do lasy com persistencia fechada, foge do padrão da JCP,
        //TODO: analizar qual deve ser o padrão, e se deve fazer parte do config de persistencia
        ///propriedades.put("hibernate.enable_lazy_load_no_trans", true);
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Collection<Logger> logColection = ctx.getLoggers();

        List<Logger> loggers = new ArrayList<>();
        logColection.stream().forEach(loggers::add);
//        loggers.add(LogManager.getRootLogger());

        carregarAutoLoadPadrao(propriedades);
        if (SBCore.isEmModoDesenvolvimento()) {
            for (Logger loggerAtual : loggers) {
                if (loggerAtual.getName().contains("hibernate")) {
                    loggerAtual.setLevel(Level.INFO);
                }

            }
        } else {
            for (Logger loggerAtual : loggers) {
                if (loggerAtual.getName().contains("hibernate")) {
                    loggerAtual.setLevel(Level.ERROR);
                }

            }

        }

        if (SBCore.isEmModoDesenvolvimento() && pRecriarBanco) {

            EntityManagerFactory emFacturePadrao = null;

            try {
                loadC3p0(propriedades);
                emFacturePadrao = Persistence.createEntityManagerFactory(nomeArquivoPersistencia, propriedades);
            } catch (Throwable t) {
                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Obtendo Configurações de Persistencia", t);
            }
            if (emFacturePadrao == null) {
                throw new UnsupportedOperationException("Impossível criar a fabrica de EntityManager");
            }

            UtilSBPersistencia.defineFabricaEntityManager(emFacturePadrao, propriedades);
            hashBancoGerado = String.valueOf(gerarHashBanco());

            if (houveAlteracaoHomologacaoBanco(configurador)) {
                try {
                    UtilSBPersistencia.getEmfabricaPadrao().close();
                } catch (Throwable t) {
                    System.out.println("Erro tentnaod fechar entitymanager factury para criação de novo banco");
                }
                limparBanco();
                propriedades.put("hibernate.hbm2ddl.auto", "create");
                EntityManager primeiraConexao = null;
                try {
                    emFacturePadrao = Persistence.createEntityManagerFactory(nomeArquivoPersistencia, propriedades);

                    UtilSBPersistencia.defineFabricaEntityManager(emFacturePadrao, propriedades);

                    primeiraConexao = UtilSBPersistencia.getNovoEM();
                    if (SBCore.isEmModoDesenvolvimento()) {
                        UtilSBCoreTestes.emContextoTEste = UtilSBPersistencia.getEntyManagerPadraoNovo();
                    }
                    if (configurador.fabricasRegistrosIniciais() != null) {
                        for (Class classe : configurador.fabricasRegistrosIniciais()) {
                            try {
                                UtilSBPersistenciaFabricas.persistirRegistrosDaFabrica(classe, primeiraConexao, UtilSBPersistenciaFabricas.TipoOrdemGravacao.ORDERNAR_POR_ORDEM_DE_DECLARCAO);
                            } catch (Throwable t) {
                                SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, arqResSBProjeto, t);
                                throw new UnsupportedOperationException("Erro Cadastrando Fabrica de dados inicial: (Fabrica:" + classe + "");
                            }
                        }
                    }

                    configurador.criarBancoInicial();
                    if (!SBCore.isIgnorarPermissoes()) {
                        SBCore.getCentralPermissao().atualizarInformacoesDePermissoesDoSistema();
                    }
                    configurador.criarRegraDeNegocioInicial();
                    compilaBanco();
                    primeiraConexao.close();
                } catch (Throwable t) {
                    UtilSBCoreArquivoTexto.escreverEmArquivoSubstituindoArqAnterior(DESTINO_ARQUIVO_HASH_BANCO, String.valueOf(0000));
                    SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro ao construir banco de dados", t);
                    throw new UnsupportedOperationException("Impossível carregar o banco pela primeira vez, cheque as configurações do entityManager" + t.getMessage());
                }

                //senão houve alterção no banco
            } else {
                try {
                    limparBanco();
                } catch (Throwable t) {
                    limparCodigoHash();
                    limparBanco();
                }
                carregaBanco();
                UtilSBPersistencia.renovarFabrica();
            }

            // SENÃO (ESTÁDO DIFERENTE DE EM DESENVOLVIMENTO): (A FUNÇÃO DE LIMPAR E SUBIR O BANCO CABE AO SCRIPT DE IMPLANTAÇÃO , E NAÕ DURANTE EXECUÇÃO DO CODIGO)
            // TODO remover essa caixa alta..
        } else {
            loadC3p0(propriedades);
            /// Creditos para esta configuração impecável e cuidadosamente comentada
            //  para: http://www.guj.com.br/users/rollei/summary
            EntityManagerFactory emFacturePadrao = Persistence.createEntityManagerFactory(nomeArquivoPersistencia, propriedades);
            UtilSBPersistencia.defineFabricaEntityManager(emFacturePadrao, propriedades);
        }

    }

}
