package com.postechfiap.msequipamentos.enums;

public enum TipoEquipamentoEnum {
    RESSONANCIA_MAGNETICA("Ressonância Magnética"),
    TOMOGRAFO_COMPUTADORIZADO("Tomógrafo Computadorizado"),
    ACELERADOR_LINEAR("Acelerador Linear - Radioterapia"),
    PET_CT("Tomografia por Emissão de Pósitrons"),
    HEMODINAMICA("Sistema de Angiografia/Hemodinâmica"),
    ARCO_CIRURGICO("Arco Cirúrgico Digital"),
    MAMOGRAFO_DIGITAL("Mamógrafo Digital"),
    EQUIPAMENTO_RAIO_X_DIGITAL("Raio-X Digital Fixo"),
    ULTRASSONOGRAFO_DOPPLER("Ultrassonógrafo com Doppler Colorido"),
    SISTEMA_DE_ROBOTICA_CIRURGICA("Sistema Cirúrgico Robótico");

    private final String descricao;

    TipoEquipamentoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}