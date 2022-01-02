package com.itau.seguro.services;

import com.itau.seguro.dtos.ClienteDto;

public interface ClienteService {

    void incluirClienteProduto(ClienteDto clienteDto);

    ClienteDto consultarClienteProdutosAcionamentos(ClienteDto clienteDto);



}
