package com.super_bits.modulosSB.Persistencia.InjecaoPorReflexao;

import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.InfoCampo;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.anotacoes.util.ErrorMessages;
import com.super_bits.modulosSB.SBCore.modulos.objetos.InfoCampos.excecao.ErroDeInjecao;
import java.lang.reflect.Field;
import java.util.Date;

class Validador {

    //=====================================================================================
    // PUBLIC METHODS
    //=====================================================================================
    public static void validateFields(Object objectInstance) throws ErroDeInjecao {
        Class<?> objectClass = objectInstance.getClass();

        Field[] fields = objectClass.getDeclaredFields();

        for (Field field : fields) {
            InfoCampo annotationField = field.getAnnotation(InfoCampo.class);

            if (annotationField != null) {
                if (annotationField.obrigatorio()) {
                    if (!isValidRequeridField(field)) {
                        throw new ErroDeInjecao(String.format(ErrorMessages.FIELD_WITH_INVALID_TYPE,
                                annotationField.label(), field.getType()));
                    }
                } else if (!isValidNotRequeridField(field)) {
                    throw new ErroDeInjecao(String.format(ErrorMessages.FIELD_WITH_INVALID_TYPE,
                            annotationField.label(), field.getType()));
                }
            }
        }
    }

    //=====================================================================================
    // PRIVATE METHODS
    //=====================================================================================
    private static boolean isValidRequeridField(Field field) {
        return !((field.getType() != int.class) && (field.getType() != long.class) && (field.getType() != boolean.class)
                && (field.getType() != char.class) && (field.getType() != double.class) && (field.getType() != float.class)
                && (field.getType() != Date.class));
    }

    private static boolean isValidNotRequeridField(Field field) {
        return !((field.getType() != int.class) && (field.getType() != Integer.class)
                && (field.getType() != long.class) && (field.getType() != Long.class)
                && (field.getType() != boolean.class) && (field.getType() != Boolean.class)
                && (field.getType() != char.class) && (field.getType() != Character.class)
                && (field.getType() != double.class) && (field.getType() != Double.class)
                && (field.getType() != float.class) && (field.getType() != Float.class)
                && (field.getType() != Date.class));
    }

}
