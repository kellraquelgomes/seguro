package com.itau.seguro.controllers;

import com.itau.seguro.dtos.ClienteAcionamentoProdutoDto;
import com.itau.seguro.dtos.ClienteDto;
import com.itau.seguro.services.ClienteAcionamentoProdutoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value="/api")
@Api(value="API REST Acionar Seguros")
@CrossOrigin(origins = "*")
public class ClienteAcionamentoProdutoController {

    @Autowired
    ClienteAcionamentoProdutoService clienteAcionamentoProdutoService;

    @ApiOperation(value="Cadastrar o acionamento de um produto")
    @PostMapping("/v1/clientes_seguros/{documento}/acionamentos")
    public ResponseEntity< ClienteAcionamentoProdutoDto > incluirClienteAcionamentoProduto(@PathVariable(value = "documento") String documento,
                                                                                           @RequestBody @Validated ClienteAcionamentoProdutoDto clienteAcionamentoProdutoDto){
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setDocumento(documento);
        clienteAcionamentoProdutoDto.setCliente(clienteDto);

        clienteAcionamentoProdutoService.incluirClienteAcionamentoProduto(clienteAcionamentoProdutoDto);

        return new ResponseEntity<ClienteAcionamentoProdutoDto>(HttpStatus.CREATED);
    }
}
