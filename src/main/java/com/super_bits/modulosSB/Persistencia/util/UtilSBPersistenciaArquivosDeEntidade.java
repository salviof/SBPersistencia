/*
 *   Super-Bits.com CODE CNPJ 20.019.971/0001-90

 */
package com.super_bits.modulosSB.Persistencia.util;

import com.super_bits.editorImagem.util.UtilSBImagemEdicao;
import com.super_bits.modulosSB.Persistencia.ConfigGeral.SBPersistencia;
import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCStringFiltros;
import com.super_bits.modulosSB.SBCore.UtilGeral.UtilCRCStringValidador;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.campo.FabTipoAtributoObjeto;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimples;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.Interfaces.basico.ComoEntidadeSimplesSomenteLeitura;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * CLASSE QUE ORGANIZA EM SISTEMA DE ARQUIVOS, OS ARQUIVOS AUXILIARES REFERENTES
 * A REGISTROS EM BANCO DE DADOS
 *
 *
 * @author Sálvio Furbino <salviof@gmail.com>
 * @since 20/03/2013
 *
 */
public abstract class UtilSBPersistenciaArquivosDeEntidade {

    public static String caminhoLocalImagens = SBCore.getCentralVisualizacao().getCaminhoLocalPastaImagem() + SBPersistencia.getPastaImagensJPA();

    public enum prefixosIMG {
        peq_, med_, grande_
    }

    private static String getCaminhoLocalImagens(ComoEntidadeSimples item) {

        return caminhoLocalImagens + getCaminhoRelativoImagem(item);
    }

    private static String getCaminhoRelativoImagem(ComoEntidadeSimplesSomenteLeitura item) {
        return "/" + item.getClass().getSimpleName() + "/" + item.getId() + "/";
    }

    private static String getUrlIMGPadrao(FabTipoAtributoObjeto tipo) {

        String urlbase = SBPersistencia.getPastaImagensJPA();
        if (tipo.equals(FabTipoAtributoObjeto.IMG_PEQUENA)) {
            return urlbase + "/SBPequeno.jpg";
        }
        if (tipo.equals(FabTipoAtributoObjeto.IMG_MEDIA)) {
            return urlbase + "/SBMedio.png";
        }
        if (tipo.equals(FabTipoAtributoObjeto.IMG_GRANDE)) {
            return urlbase + "/SBGrande.png";
        }
        return urlbase + "/SBPequeno.jpg";

    }

    public static String getURLImagem(ComoEntidadeSimplesSomenteLeitura item, FabTipoAtributoObjeto tipo) {

        String pastaRelativaImagens = SBCore.getCentralVisualizacao().getRemotoPastaResource() + SBPersistencia.getPastaImagensJPA() + getCaminhoRelativoImagem(item);
        String urlpadrao = getUrlIMGPadrao(tipo);

        File pasta = new File(caminhoLocalImagens + getCaminhoRelativoImagem(item));

        File[] arquivos = pasta.listFiles();
        if (arquivos == null) {
            return urlpadrao;
        }
        for (File arq : arquivos) {

            if (tipo.equals(FabTipoAtributoObjeto.IMG_PEQUENA) & arq.getName().contains(prefixosIMG.peq_.toString())) {
                return pastaRelativaImagens + arq.getName();

            }
            if (tipo.equals(FabTipoAtributoObjeto.IMG_MEDIA) & arq.getName().contains(prefixosIMG.med_.toString())) {
                return pastaRelativaImagens + arq.getName();

            }

            if (tipo.equals(FabTipoAtributoObjeto.IMG_GRANDE) & arq.getName().contains(prefixosIMG.grande_.toString())) {
                return pastaRelativaImagens + arq.getName();

            }

        }

        return urlpadrao;
    }

    @SuppressWarnings("unused")
    public static List<String> getURLImagens(ComoEntidadeSimples item, String galeria) {

        String urlbase = caminhoLocalImagens;
        String urlpadrao = getUrlIMGPadrao(FabTipoAtributoObjeto.IMG_GRANDE);

        List<String> respPadrao = new ArrayList();
        respPadrao.add(urlpadrao);
        respPadrao.add(urlpadrao);
        respPadrao.add(urlpadrao);

        File pasta = new File(getCaminhoLocalImagens(item) + "/" + galeria);
        File[] arquivos = pasta.listFiles();
        if (arquivos == null) {
            return respPadrao;
        }
        List<String> resp = new ArrayList<>();
        int i = 0;
        for (File arq : arquivos) {
            resp.add(urlbase + "/" + item.getClass().getSimpleName() + "/" + item.getId() + "/galeria/" + arq.getName());
            i++;

        }

        if (arquivos.length == 0) {

            return respPadrao;
        } else {
            return resp;
        }
    }

    public static void SalvaIMAGEM(ComoEntidadeSimples entidade, InputStream foto) {
        SalvaIMAGEM(entidade, foto, null);
    }

    public static void SalvaIMAGEM(ComoEntidadeSimples entidade, InputStream foto, String categoria) {
        String tabela = entidade.getClass().getSimpleName();

        // Gerando Diretorios
        String caminho = caminhoLocalImagens;
        caminho = caminho + "/" + tabela + "/" + entidade.getId() + '/';
        if (categoria != null) {
            caminho = caminho + "/" + categoria + '/';
        }
        File arquivo = new File(caminho);
        arquivo.mkdirs();

        // Gerando Nome do Arquivo
        File[] arquivosnapasta = arquivo.listFiles();
        int qtd = arquivosnapasta.length;
        String extencao = "jpg";
        if (categoria == null) {
            //	198 x 43
            //	44 x 43

            String nomeArquivo = entidade.getNomeCurto();

            // criando arquivo Original
            criaArquivo(caminho, "ori_" + nomeArquivo + extencao, foto);

            try {
                // redimencionando imagem média
                criaArquivo(caminho, prefixosIMG.peq_ + nomeArquivo.replace(" ", "_") + ".jpg", UtilSBImagemEdicao.redimencionaImagem(ImageIO.read(foto), 43, 43));
                criaArquivo(caminho, prefixosIMG.med_ + nomeArquivo.replace(" ", "_") + ".jpg", UtilSBImagemEdicao.redimencionaImagem(ImageIO.read(foto), 150, 43));
                criaArquivo(caminho, prefixosIMG.grande_ + nomeArquivo.replace(" ", "_") + ".jpg", UtilSBImagemEdicao.redimencionaImagem(ImageIO.read(foto), 462, 133));

            } catch (IOException ex) {
                Logger.getLogger(UtilSBPersistenciaArquivosDeEntidade.class.getName()).log(Level.SEVERE, null, ex);
            }

            // se tiver Categoria Especificado
            // se tiver Categoria Especificado
            // se tiver Categoria Especificado
            // se tiver Categoria Especificado
        } else {

            String nomeArquivo
                    = entidade.getNomeCurto() + String.valueOf(qtd + 1) + "." + extencao;

            criaArquivo(caminho, nomeArquivo, foto);

        }

    }

    private static void criaArquivo(String dir, String nomeArquivo, InputStream in) {
        try {
            File diretorio = new File(dir);
            diretorio.mkdirs();

            // write the inputStream to a FileOutputStream
            OutputStream out;
            out = new FileOutputStream(new File(dir + UtilCRCStringFiltros.removeCaracteresEspeciais(nomeArquivo)));

            int read = 0;
            byte[] bytes = new byte[1024];

            while ((read = in.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }

            in.close();
            out.flush();
            out.close();

            System.out.println("New file created!");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
