package com.itau.seguro.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String mensagem){
        super(mensagem);
    }
}
