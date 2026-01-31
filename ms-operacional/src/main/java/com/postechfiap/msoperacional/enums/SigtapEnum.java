package com.postechfiap.msoperacional.enums;

import lombok.Getter;
import java.util.Arrays;

@Getter
public enum SigtapEnum {

    // --- RESSONÂNCIA MAGNÉTICA (RM) ---
    RM_CRANIO("02.07.01.006-4", "RM de Crânio", "RESSONANCIA_MAGNETICA"),
    RM_COLUNA("02.07.03.001-4", "RM de Coluna Cervical/Pescoço", "RESSONANCIA_MAGNETICA"),
    RM_CORACAO("02.07.01.004-8", "RM de Coração", "RESSONANCIA_MAGNETICA"),

    // --- TOMOGRAFIA COMPUTADORIZADA (TC) ---
    TC_CRANIO("02.06.01.007-9", "TC de Crânio", "TOMOGRAFO_COMPUTADORIZADO"),
    TC_TORAX("02.06.03.001-0", "TC de Tórax", "TOMOGRAFO_COMPUTADORIZADO"),
    TC_ABDOME_TOTAL("02.06.02.001-5", "TC de Abdome Total", "TOMOGRAFO_COMPUTADORIZADO"),

    // --- RADIOTERAPIA (Acelerador Linear) ---
    RT_MEGAVOLTAGEM("03.04.01.026-6", "Radioterapia de Megavoltagem", "ACELERADOR_LINEAR"),
    RT_ESTEREOTAXICA("03.04.01.025-8", "Radioterapia Estereotáxica", "ACELERADOR_LINEAR"),

    // --- PET-CT ---
    PET_CT_ONCOLOGICO("02.08.09.001-0", "PET-CT Oncológico", "PET_CT"),
    PET_CT_CEREBRAL("02.08.09.002-8", "PET-CT Cerebral", "PET_CT"),

    // --- HEMODINÂMICA E ANGIOGRAFIA ---
    HD_CATETERISMO("02.11.02.003-9", "Cateterismo Cardíaco", "HEMODINAMICA"),
    HD_ANGIOPLASTIA("04.06.03.001-9", "Angioplastia Coronariana", "HEMODINAMICA"),

    // --- ARCO CIRÚRGICO ---
    AC_CIRURGIA_ORTOPEDICA("04.08.05.011-4", "Tratamento Cirúrgico de Fratura", "ARCO_CIRURGICO"),
    AC_MARCAPASSO("04.06.01.062-0", "Implante de Marcapasso Temporário", "ARCO_CIRURGICO"),

    // --- MAMOGRAFIA DIGITAL ---
    MM_BILATERAL("02.04.03.018-8", "Mamografia Digital Bilateral", "MAMOGRAFO_DIGITAL"),
    MM_MONOLATERAL("02.04.03.017-0", "Mamografia Digital Monolateral", "MAMOGRAFO_DIGITAL"),

    // --- RAIO-X DIGITAL ---
    RX_TORAX("02.04.03.015-3", "Radiografia de Tórax (PA e Perfil)", "EQUIPAMENTO_RAIO_X_DIGITAL"),
    RX_COLUNA_TOTAL("02.04.02.015-8", "Radiografia de Coluna Total (Panorâmica)", "EQUIPAMENTO_RAIO_X_DIGITAL"),

    // --- ULTRASSONOGRAFIA DOPPLER ---
    US_DOPPLER_COLORIDO("02.05.01.004-0", "Ultrassonografia Doppler de Fluxo Obstétrico", "ULTRASSONOGRAFO_DOPPLER"),
    US_CAROTIDAS("02.05.01.001-6", "Ultrassonografia Doppler de Carótidas e Vertebrais", "ULTRASSONOGRAFO_DOPPLER"),

    // --- ROBÓTICA CIRÚRGICA ---
    RB_PROSTATECTOMIA("04.09.03.004-2", "Prostatectomia Radical Assistida por Robô", "SISTEMA_DE_ROBOTICA_CIRURGICA"),
    RB_CISTECTOMIA("04.09.02.001-2", "Cistectomia Radical Assistida por Robô", "SISTEMA_DE_ROBOTICA_CIRURGICA");

    private final String codigo;
    private final String descricao;
    private final String tipoEquipamentoPermitido;

    SigtapEnum(String codigo, String descricao, String tipoEquipamentoPermitido) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.tipoEquipamentoPermitido = tipoEquipamentoPermitido;
    }

    public static SigtapEnum deCodigo(String codigo) {
        return Arrays.stream(SigtapEnum.values())
                .filter(s -> s.getCodigo().equals(codigo))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Código SIGTAP [" + codigo + "] não mapeado ou inválido."));
    }
}