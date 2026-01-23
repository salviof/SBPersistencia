/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package com.super_bits.modulosSB.Persistencia.dao;

/**
 *
 * @author salvio
 */
public enum TipoEntradaParametroUrl {

    TEXTO_E_NUMERO_POSITIVO,
    TEXTO_E_NUMERO_NEGATIVO,
    SOMENTE_TEXTO,
    SOMENTE_NUMERO;

    public static TipoEntradaParametroUrl getTipoEntrada(String s) {
        if (s == null || s.trim().isEmpty()) {
            return TipoEntradaParametroUrl.SOMENTE_TEXTO;
        }

        // somente número (positivo ou negativo)
        if (s.matches("-?\\d+")) {
            return TipoEntradaParametroUrl.SOMENTE_NUMERO;
        }

        int idx = s.indexOf('-');

        // não tem hífen → texto puro
        if (idx < 0) {
            return TipoEntradaParametroUrl.SOMENTE_TEXTO;
        }

        String sufixo = s.substring(idx + 1);

        // texto--123
        if (sufixo.matches("-\\d+")) {
            return TipoEntradaParametroUrl.TEXTO_E_NUMERO_NEGATIVO;
        }

        // texto-123
        if (sufixo.matches("\\d+")) {
            return TipoEntradaParametroUrl.TEXTO_E_NUMERO_POSITIVO;
        }

        return TipoEntradaParametroUrl.SOMENTE_TEXTO;

    }
}
