package br.com.tutoraia.resposta;

import lombok.Data;

@Data
public class TokenResposta {
    private String token;

    public TokenResposta(String token) {
        this.token = token;
    }
}
