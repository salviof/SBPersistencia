package com.super_bits.modulosSB.Persistencia.InjecaoPorReflexao;

import com.super_bits.modulosSB.SBCore.ConfigGeral.SBCore;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.ValorAceito;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.util.ErrorMessages;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.excecao.ErroDeFormatoDoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.excecao.ErroDeInjecao;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.excecao.ErroDeMapaDeCampos;
import com.super_bits.modulosSB.SBCore.modulos.objetos.registro.CampoMapValores;
import java.lang.reflect.Field;
import java.util.Date;
import org.coletivojava.fw.api.tratamentoErros.FabErro;

public class InjetorDeValores {

    /**
     * Injeta valores padrão apenas em campos publicos
     *
     * @param objectInstance
     */
    public static void injectaValoresPublicos(Object objectInstance, CampoMapValores mapFields) throws ErroDeInjecao {
        Validador.validateFields(objectInstance);

        Class<?> objClass = objectInstance.getClass();

        Field[] fields = objClass.getFields();

        for (Field field : fields) {
            InfoCampo annotationField = field.getAnnotation(InfoCampo.class);

            if (annotationField != null) {
                injetaValorPadrao(field, annotationField, objectInstance, mapFields);
            }
        }
    }

    /**
     ** Injeta valores padrão em campos de todos os tipos (public, protected,
     * private, final).
     *
     * @param pInstancia Instancia onde o valor será injetado
     * @param pValoresPadrao Campos e seus respectivos valores padrão
     * @throws Excessão de injeção
     * com.super_bits.modulosSB.Persistencia.InjecaoPorReflexao.excecao.InjectionException
     */
    public static void injectaTodosValores(Object pInstancia, CampoMapValores pValoresPadrao) throws ErroDeInjecao {
        Validador.validateFields(pInstancia);

        Class<?> objClass = pInstancia.getClass();

        Field[] fields = objClass.getDeclaredFields();

        for (Field field : fields) {
            InfoCampo annotationField = field.getAnnotation(InfoCampo.class);

            if (annotationField != null) {
                field.setAccessible(true);

                injetaValorPadrao(field, annotationField, pInstancia, pValoresPadrao);
            }
        }
    }

    private static void injetaValorPadrao(Field pCampo, InfoCampo anotacaoInfoCampo, Object pInstancia, CampoMapValores pValoresPadrao) throws ErroDeInjecao {
        Object value = null;

        try {
            if (pCampo.get(pInstancia) != null) {

                try {
                    validaCampo(anotacaoInfoCampo, pValoresPadrao);

                    if ((pCampo.getType() == int.class) || (pCampo.getType() == Integer.class)) {            // INTEGER
                        value = pValoresPadrao.getIntValue(anotacaoInfoCampo.tipo());
                    } else if ((pCampo.getType() == long.class) || (pCampo.getType() == Long.class)) {       // LONG
                        value = pValoresPadrao.getLongValue(anotacaoInfoCampo.tipo());
                    } else if ((pCampo.getType() == boolean.class) || (pCampo.getType() == Boolean.class)) { // BOOLEAN
                        value = pValoresPadrao.getBooleanValue(anotacaoInfoCampo.tipo());
                    } else if ((pCampo.getType() == char.class) || (pCampo.getType() == Character.class)) {  // CHARACTER
                        value = pValoresPadrao.getCharacterValue(anotacaoInfoCampo.tipo());
                    } else if ((pCampo.getType() == double.class) || (pCampo.getType() == Double.class)) {   // DOUBLE
                        value = pValoresPadrao.getDoubleValue(anotacaoInfoCampo.tipo());
                    } else if ((pCampo.getType() == float.class) || (pCampo.getType() == Float.class)) {     // FLOAT
                        value = pValoresPadrao.getFloatValue(anotacaoInfoCampo.tipo());
                    } else if (pCampo.getType() == Date.class) {                                            // DATE
                        value = pValoresPadrao.getDateValue(anotacaoInfoCampo.tipo());
                    }
                } catch (ErroDeMapaDeCampos e) {
                    if (anotacaoInfoCampo.obrigatorio()) {
                        throw new ErroDeInjecao(e.getMessage(), e);
                    }
                } catch (ErroDeFormatoDoCampo e) {
                    throw new ErroDeInjecao(e.getMessage(), e);
                }

                try {
                    pCampo.set(pInstancia, value);
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new ErroDeInjecao(e.getMessage(), e);
                }

            }
        } catch (IllegalArgumentException e) {

            throw new ErroDeInjecao(e.getMessage(), e);
        } catch (IllegalAccessException e1) {
            SBCore.RelatarErro(FabErro.SOLICITAR_REPARO, "Erro Injetando valor na instancia", e1);

        }

    }

    private static void validaCampo(InfoCampo fieldAnnotation, CampoMapValores mapFields) throws ErroDeMapaDeCampos, ErroDeInjecao {
        if (fieldAnnotation.valoresAceitos().length <= 0) {
            return;
        }

        String value = mapFields.getValorPadrao(fieldAnnotation.tipo()).toString();

        for (ValorAceito wPatternValue : fieldAnnotation.valoresAceitos()) {
            if (wPatternValue.valor().equalsIgnoreCase(value)) {
                return;
            }
        }

        throw new ErroDeInjecao(String.format(ErrorMessages.FIELD_WITH_INVALID_VALUE, fieldAnnotation.tipo(), value));
    }

}
