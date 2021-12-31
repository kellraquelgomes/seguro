package com.itau.seguro.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.joda.time.DateTime;

@Data
public class ClienteAcionamentoProdutoDto {

    private ProdutoDto produto;

    private ClienteDto cliente;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private DateTime dataAcionamento;

}
