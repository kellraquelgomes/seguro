package com.itau.seguro.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteDto {

    private Integer clienteId;

    @NotBlank
    @NotNull
    private String nome;

    @NotBlank
    @NotNull
    private String documento;

    @NotEmpty
    @NotNull
    private List< ProdutoDto > produtos;

}
