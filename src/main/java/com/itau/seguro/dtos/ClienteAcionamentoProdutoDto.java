package com.itau.seguro.dtos;

import lombok.Data;
import org.joda.time.DateTime;

import java.util.Date;

@Data
public class ClienteAcionamentoProdutoDto {

    private Integer clienteId;

    private Integer produtoId;

    private DateTime dataAcionamento;

}
