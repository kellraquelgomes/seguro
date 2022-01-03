package com.itau.seguro.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "TB_CLIENTE")
@EqualsAndHashCode(of = {"clienteId"})
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer clienteId;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 20)
    private String documento;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(    name = "TB_CLIENTES_PRODUTOS",
            joinColumns = @JoinColumn(name = "clienteId"),
            inverseJoinColumns = @JoinColumn(name = "produtoId"))
    private Set<Produto> produtos = new HashSet<>();

    @OneToMany(mappedBy = "cliente" , fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<ClienteAcionamentoProduto> acionamentos = new HashSet<>();


}
