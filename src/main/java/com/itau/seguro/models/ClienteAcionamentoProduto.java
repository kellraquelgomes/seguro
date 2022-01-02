package com.itau.seguro.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "TB_CLIENTE_ACIONAMENTO_PRODUTO")
@EqualsAndHashCode(of = {"id"})
public class ClienteAcionamentoProduto {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    @Column(nullable = false)
    private Date dataAcionamento;

    //MuitosAcionamentosParaUmProduto
    @JsonProperty(access = JsonProperty.Access. WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Produto produto;

    //MuitosAcionamentosParaUmCliente
    @JsonProperty(access = JsonProperty.Access. WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cliente cliente;








}
