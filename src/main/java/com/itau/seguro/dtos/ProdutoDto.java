package com.itau.seguro.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Data
public class ProdutoDto {

    private Integer produtoId;

    @NotBlank
    @NotNull
    private String nome;

    @NotBlank
    @NotNull
    private BigDecimal valor;

    private Integer quantidadeAcionamento;

    @NotBlank
    @NotNull
    private ParceiroDto parceiro;

    @NotEmpty
    @NotBlank
    @NotNull
    private List<ClienteAcionamentoProdutoDto> acionamentos;


}
