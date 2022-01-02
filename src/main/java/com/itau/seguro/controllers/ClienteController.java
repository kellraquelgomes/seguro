package com.itau.seguro.controllers;

import com.itau.seguro.dtos.ClienteDto;
import com.itau.seguro.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


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

    @GetMapping("/clientes_seguros/{documento}/informacoes")
    public ResponseEntity<ClienteDto> consultarClienteProdutosAcionamentos(@PathVariable(value = "documento") String documento) {

        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDocumento(documento);

        ClienteDto clienteRetorno = clienteService.consultarClienteProdutosAcionamentos(clienteDto);

        return new ResponseEntity<ClienteDto>(clienteRetorno, HttpStatus.OK);
    }

}
