package com.itau.seguro.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteAcionamentoProdutoDto {

    @NotNull
    private ProdutoDto produto;

    private ClienteDto cliente;

    @NotNull
    private Date dataAcionamento;

}
