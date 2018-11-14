package com.super_bits.modulosSB.Persistencia.ConfigGeral;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public abstract class CSBNQ {

    public enum Qr {
        // as QRs devem ser criadas com a segunte sintax
        // TABELAbyParametro1_Parametro2_Parametro3

        Cidade, CidadeByID, CidadeByLocalidade, OperadoraByClinica,
        OperadoraByLocalidade, Operadora, OperadoraById, OperadoraByCodigoAns,
        OperadoraByLocalidade$MaisCaro, OperadoraByMenosEconomicos$Localidade,
        OperadoraByMaisReclamacoes$Localidade,
        OperadoraByMenosReclamações$Localidade,
        ReclamacoesANSByOperadora$Data,
        Localidade, LocalidadeByid, Clinica,
        ClinicaByLocalidade,
        ClinicaByLocalidade$Especialidade,
        ClinicaByLocalidade$Operadora$Especialidade,
        ClinicaByLocalidade$Cidade$Operadora$Especialidade,
        Tipoclinica, TipoclinicaById, PlanoByClinica, Plano, PlanoByID,
        PlanoByApartamento$TransporteAereo$Cooparticipativo$CoberturaGeografica,
        TipoPlano, TipoPlanoByID, PlanoLKCoberturaGeografica,
        PlanoLKCoberturaGeograficaById, PlanoLKCoparticipativo, PlanoLKCoparticipativoById, PlanoLKInternacao,
        PlanoLKInternacaoById, PlanoLKObstetricia, PlanoLKObstetriciaById, PlanoLKOdontologia, PlanoLKOdontologiaById,
        PlanoLKTransporteAereo, PlanoLKTransporteAereoById,
        ReclamacoesANSByOperadora, SqlGenericoLike
    }

    public static void palavarasReservadas() {
        Map<String, String> reserv = new HashMap<String, String>();
        reserv.put("", "[TABELA]");
        reserv.put("", "[NOMECURTO]");

    }

    private static String makeCondicional(String[] camposSBNQ) {
        if (1 == 1) {
            return "";
        }
        String resposta = " where ";

        for (String cp : camposSBNQ) {
            // se a primeira  letra for maiuscula
            //	Integer tabelasEnvolvidas=cp.split("_").length;
            if (cp.substring(0, 0).equals(
                    cp.substring(0, 0).toUpperCase())) {
                resposta = resposta + " and " + cp.toLowerCase() + ".id in (:p" + cp + ") ";
            } else {
                resposta = resposta + " and " + cp + " in (:p" + cp + ") ";
            }

        }
        // não tem vários campo (contem apenas um)

        return resposta;
    }

    private static String MakeSQLbyNome(Qr Cquery) {

        String tabela = Cquery.toString().split("By")[0];
        String sql;
        sql = " from " + tabela;
        if (1 == 1) {
            return sql;
        }
        // se tiver algum condicional (by x)
        String restante = Cquery.toString().split("By")[1];
        // se tiver condiconal Seta as condições
        if (Cquery.toString().split("By").length > 1) {
            sql = sql + makeCondicional(restante.split("\\$"));
        }
        return sql;

    }

    public static String getSQL(Qr pNQ) {

        switch (pNQ) {

            case OperadoraByClinica:
                // sql = "from Operadora";
                return MakeSQLbyNome(pNQ);
            case OperadoraByLocalidade:
                // sql = " from Operadora";
                return MakeSQLbyNome(pNQ);
            case OperadoraByCodigoAns:
                // sql = " from Operadora where codigoANS=:pCodigoAns";
                return MakeSQLbyNome(pNQ);
            case Operadora:
                // sql = " from Operadora";
                return MakeSQLbyNome(pNQ);
            case OperadoraById:
                // sql = " from Operadora where id= :pOperadora ";
                return MakeSQLbyNome(pNQ);
            case ReclamacoesANSByOperadora$Data:
                // sql =
                // " from ReclamacoesANS where  operadora.id=:pOperadora and data=:pData";
                return MakeSQLbyNome(pNQ);
            case Localidade:
                // sql = " from Localidade";
                return MakeSQLbyNome(pNQ);
            case LocalidadeByid:
                // sql = " from Localidade where id= :pid ";
                return MakeSQLbyNome(pNQ);
            case Clinica:
                // sql = " from Prestador";
                return MakeSQLbyNome(pNQ);
            case ClinicaByLocalidade:
                // sql = ""
                // +
                // "   from Prestador where cidade.localidade.id in (:pLocalidade) ";
                return MakeSQLbyNome(pNQ);
            case CidadeByLocalidade:
                // sql = "" +
                // "   from Cidade where localidade.id in (:pLocalidade)";
                return MakeSQLbyNome(pNQ);
            case ClinicaByLocalidade$Cidade$Operadora$Especialidade:
                // sql = "select  cli from  Prestador cli "
                // + "left join cli.clinicaOperadoras as op "
                // + " left join cli.clinicaEspecialidades esp"
                // + "  where cli.cidade.localidade.id in :pLocalidade and "
                // + " cli.cidade.id in :pCidade "
                // + " and op.operadora.id in :pOperadora "
                // + " and cli.tipoclinica.id in :pTipoclinica "
                // + " group by cli";
                return MakeSQLbyNome(pNQ);
            case TipoclinicaById:
                // sql = " from TipoPrestador where id=:pid ";
                return MakeSQLbyNome(pNQ);
            //case ReclamacoesansByOperadora:
            // sql =
            // " from ReclamacoesANS where operadora.id =:pOperadora order by data";
            //	return MakeSQLbyNome(pNQ);
            default:
                // sql = "from [TABELA]";
                return MakeSQLbyNome(pNQ);

        }

    }

    public static List<String> getParametros(Qr query) {
        return null;
    }

}
