package com.itau.seguro.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "TB_PRODUTO")
public class Produto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer produtoId;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 10, precision = 2)
    private BigDecimal valor;

    @Column(nullable = false, length = 20)
    private Integer quantidadeAcionamento;

    //MuitosProdutosParaMuitosClientes
    @ManyToMany(mappedBy = "produtos",fetch = FetchType.LAZY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Set<Cliente> clientes = new HashSet<>();

    //MuitosProdutosParaOneParceiro
    @JsonProperty(access = JsonProperty.Access. WRITE_ONLY)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Parceiro parceiro;

    //UmProdutoParaMuitosAcionamentos
    @JsonProperty(access = JsonProperty.Access. WRITE_ONLY)
    @OneToMany(mappedBy = "produto" , fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<ClienteAcionamentoProduto> acionamentos = new HashSet<>();



}
