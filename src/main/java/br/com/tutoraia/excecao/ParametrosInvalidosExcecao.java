package br.com.tutoraia.excecao;

import lombok.Getter;

@Getter
public class ParametrosInvalidosExcecao extends RuntimeException{

    private final String name = "InvalidParamsException";
    public ParametrosInvalidosExcecao(String message) {
        super(message);
    }
}
