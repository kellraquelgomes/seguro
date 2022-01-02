package com.itau.seguro.dtos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
public class ClienteAcionamentoProdutoDto {

    @NotNull
    private ProdutoDto produto;

    private ClienteDto cliente;

    @NotNull
    private Date dataAcionamento;

}
