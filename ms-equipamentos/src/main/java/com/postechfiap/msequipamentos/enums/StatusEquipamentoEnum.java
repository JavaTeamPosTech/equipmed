package com.postechfiap.msequipamentos.enums;

public enum StatusEquipamentoEnum {
    ATIVO("Equipamento em operação"),
    MANUTENCAO_PREVENTIVA("Manutenção agendada/preventiva"),
    MANUTENCAO_CORRETIVA("Equipamento com defeito/parado"),
    AGUARDANDO_INSTALACAO("Recebido, mas não instalado"),
    FORA_DE_USO("Equipamento desativado ou obsoleto"),
    EM_TRANSFERENCIA("Em trânsito para outra unidade");

    private final String descricao;

    StatusEquipamentoEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}