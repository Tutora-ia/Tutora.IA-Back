package br.com.tutoraia.controle;

import br.com.tutoraia.request.EmailRequest;
import br.com.tutoraia.request.RecuperacaoSenhaRequest;
import br.com.tutoraia.resposta.MensagemPadraoResposta;
import br.com.tutoraia.servico.UsuarioServico;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user")
@Slf4j
@AllArgsConstructor
public class UsuarioControle {

    private final UsuarioServico usuarioServico;

    @PostMapping("/recuperar-senha")
    public ResponseEntity<String> recoverPassword(@RequestBody EmailRequest emailRequest) {
        log.info("Recuperar senha para o email: {}", emailRequest.getEmail());
        return usuarioServico.recoverPassword(emailRequest.getEmail());
    }

    @PatchMapping("/resetar-senha/{token}")
    public ResponseEntity<MensagemPadraoResposta> resetPassword(@PathVariable String token, @RequestBody RecuperacaoSenhaRequest password) {
        log.info("Resetar senha do token: {}", token);
        return usuarioServico.resetPassword(token, password);
    }
}
