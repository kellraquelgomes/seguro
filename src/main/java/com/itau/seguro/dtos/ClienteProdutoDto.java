package com.itau.seguro.dtos;

import lombok.Data;

import java.util.List;

@Data
public class ClienteProdutoDto {

    private Integer clienteId;

    private String nome;

    private String documento;

    private List< ProdutoDto > produtos;

}
