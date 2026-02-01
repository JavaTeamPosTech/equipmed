package com.postechfiap.mstransparencia.util;

public class LocalizacaoUtil {
    public static String extrairCidade(String localizacao) {
        if (localizacao == null || !localizacao.contains("-")) return "NÃO INFORMADO";
        return localizacao.split("-")[0].trim().toUpperCase();
    }
}