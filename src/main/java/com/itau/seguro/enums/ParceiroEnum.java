package com.itau.seguro.enums;

public enum ParceiroEnum {
    COM_vOCE(1, "com Você"),
    MAIS_VOCE(2, "Mais Você");

    private Integer codigo;
    private String descricao;

    private ParceiroEnum(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    /**
     * @return the codigo
     */
    public Integer getCodigo() {
        return codigo;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

}
