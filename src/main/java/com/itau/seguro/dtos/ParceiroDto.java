package com.itau.seguro.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParceiroDto {

    private Integer codigo;

    private String nome;
}
