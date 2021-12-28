package com.itau.seguro.exceptions;

public class BusinessException extends RuntimeException {

    public BusinessException(String mensagem){
        super(mensagem);
    }


}
