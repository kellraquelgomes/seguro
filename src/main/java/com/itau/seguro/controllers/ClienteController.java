package com.itau.seguro.controllers;

import com.itau.seguro.dtos.ClienteDto;
import com.itau.seguro.services.ClienteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @PostMapping("/clientes_seguros")
    public ResponseEntity<ClienteDto> incluirClienteProduto(@RequestBody @Validated ClienteDto clienteDto){

        clienteService.incluirClienteProduto(clienteDto);

        return new ResponseEntity<ClienteDto>(HttpStatus.CREATED);
    }

}
