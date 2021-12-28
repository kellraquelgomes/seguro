package com.itau.seguro.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ClienteDto {

    private Integer clienteId;

    private String nome;

    private String documento;

    private List< ProdutoDto > produtos;

}
