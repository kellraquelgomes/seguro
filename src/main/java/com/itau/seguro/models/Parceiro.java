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
@Table(name = "TB_PARCEIRO")
@EqualsAndHashCode(of = {"parceiroId"})
public class Parceiro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer parceiroId;

    @Column(nullable = false, length = 150)
    private String nome;

    //UmParceiroParaMuitosProdutos
    @JsonProperty(access = JsonProperty.Access. WRITE_ONLY)
    @OneToMany(mappedBy = "parceiro" , fetch = FetchType.LAZY)
   // @Fetch(FetchMode.SUBSELECT)
    private Set<Produto> produtos = new HashSet<>();




}
