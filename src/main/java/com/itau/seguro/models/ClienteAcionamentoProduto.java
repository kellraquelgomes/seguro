package com.itau.seguro.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_CLIENTE_ACIONAMENTO_PRODUTO")
public class ClienteAcionamentoProduto {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'")
    @Column(nullable = false)
    private LocalDateTime dataAcionamento;

    //MuitosAcionamentosParaUmProduto
    @JsonProperty(access = JsonProperty.Access. WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Produto produto;

    //MuitosAcionamentosParaUmCliente
    @JsonProperty(access = JsonProperty.Access. WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cliente cliente;








}
