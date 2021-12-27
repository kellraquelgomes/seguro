package com.itau.seguro.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "TB_CLIENTE")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID clienteId;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 20)
    private String documento;

    //MuitosClientesParaMuitosProdutos
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(    name = "TB_CLIENTES_PRODUTOS",
            joinColumns = @JoinColumn(name = "clienteId"),
            inverseJoinColumns = @JoinColumn(name = "produtoId"))
    private Set<Produto> produtos;

    //UmClienteParaMuitosAcionamentos
    @JsonProperty(access = JsonProperty.Access. WRITE_ONLY)
    @OneToMany(mappedBy = "cliente" , fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<ClienteAcionamentoProduto> acionamentos;


}
