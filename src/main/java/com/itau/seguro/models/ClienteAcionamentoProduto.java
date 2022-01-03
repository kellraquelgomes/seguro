package com.itau.seguro.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "TB_CLIENTE_ACIONAMENTO_PRODUTO")
@EqualsAndHashCode(of = {"id"})
public class ClienteAcionamentoProduto  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private Date dataAcionamento;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Produto produto;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Cliente cliente;








}
