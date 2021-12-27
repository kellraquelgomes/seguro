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
@Table(name = "TB_PARCEIRO")
public class Parceiro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID parceiroId;

    @Column(nullable = false, length = 150)
    private String nome;

    //UmParceiroParaMuitosProdutos
    @JsonProperty(access = JsonProperty.Access. WRITE_ONLY)
    @OneToMany(mappedBy = "parceiro" , fetch = FetchType.LAZY)
    @Fetch(FetchMode.SUBSELECT)
    private Set<Produto> produtos;




}
