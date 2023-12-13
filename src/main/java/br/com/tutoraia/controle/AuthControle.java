package br.com.tutoraia.controle;

import br.com.tutoraia.request.LoginRequest;
import br.com.tutoraia.request.UsuarioRequest;
import br.com.tutoraia.resposta.TokenResposta;
import br.com.tutoraia.servico.UsuarioServico;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@AllArgsConstructor
@Slf4j
public class AuthControle {

    private final UsuarioServico usuarioServico;

    @PostMapping("/cadastro")
    public ResponseEntity<Void> signUp(@RequestBody UsuarioRequest usuarioRequest) {
        return usuarioServico.signUp(usuarioRequest);
    }

    @PostMapping("/logar")
    public ResponseEntity<TokenResposta> login(@RequestBody LoginRequest loginRequest) {
        return usuarioServico.login(loginRequest);
    }

    @GetMapping
    public String teste(){
        return "teste";
    }
}
