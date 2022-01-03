package com.itau.seguro.controllers;

import com.itau.seguro.dtos.ClienteDto;
import com.itau.seguro.services.ClienteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api")
@Api(value="API REST Cliente Produto")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @ApiOperation(value="Cadastrar os dados relacionados a um cliente e seus seguros contratados")
    @PostMapping("/clientes_seguros")
    public ResponseEntity<ClienteDto> incluirClienteProduto(@RequestBody @Validated ClienteDto clienteDto){

        clienteService.incluirClienteProduto(clienteDto);

        return new ResponseEntity<ClienteDto>(HttpStatus.CREATED);
    }

    @ApiOperation(value="Exibir dados de um cliente (seu portfolio de produtos contratados) e seus respectivos acionamentos")
    @GetMapping("/clientes_seguros/{documento}/informacoes")
    public ResponseEntity<ClienteDto> consultarClienteProdutosAcionamentos(@PathVariable(value = "documento") String documento) {

        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDocumento(documento);

        ClienteDto clienteRetorno = clienteService.consultarClienteProdutosAcionamentos(clienteDto);

       return new ResponseEntity<ClienteDto>(clienteRetorno, HttpStatus.OK);

    }

}
