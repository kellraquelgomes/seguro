package com.itau.seguro.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

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

    @OneToMany(mappedBy = "parceiro" , fetch = FetchType.LAZY)
    private Set<Produto> produtos = new HashSet<>();




}
