package com.itau.seguro.services;

import com.itau.seguro.dtos.ClienteDto;

public interface ClienteService {

    void saveClienteProduto(ClienteDto clienteDto);

    ClienteDto findClienteProdutosAcionamentos(ClienteDto clienteDto);



}
