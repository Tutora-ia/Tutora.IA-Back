package br.com.tutoraia.request;

import lombok.Data;

@Data
public class RecuperacaoSenhaRequest {

    private String password;
    private String confirmPassword;
}
