package com.itau.seguro.repositories;

import com.itau.seguro.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository< Cliente,Integer > {

    Optional< Cliente> findByDocumento(String documento);


}
