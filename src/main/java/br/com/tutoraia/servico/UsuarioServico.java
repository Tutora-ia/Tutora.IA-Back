package br.com.tutoraia.servico;

import br.com.tutoraia.request.LoginRequest;
import br.com.tutoraia.request.RecuperacaoSenhaRequest;
import br.com.tutoraia.request.UsuarioRequest;
import br.com.tutoraia.resposta.MensagemPadraoResposta;
import br.com.tutoraia.resposta.TokenResposta;
import br.com.tutoraia.excecao.ParametrosInvalidosExcecao;
import br.com.tutoraia.modelo.TokenResetSenha;
import br.com.tutoraia.modelo.User;
import br.com.tutoraia.repository.TokenResetSenhaRepository;
import br.com.tutoraia.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class UsuarioServico {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final AuthenticationManager manager;
    private final TokenServico tokenServico;
    private TokenResetSenhaRepository tokenResetSenhaRepository;
    private final EmailServico emailServico;

    public ResponseEntity<Void> signUp(UsuarioRequest usuarioRequest) {
        log.info("Save user: {}", usuarioRequest);
        if (usuarioRepository.existsByEmail(usuarioRequest.getEmail())) {
            log.error("Email already exists");
            throw new ParametrosInvalidosExcecao("Email already exists");
        }
        User user = new User();
        BeanUtils.copyProperties(usuarioRequest, user);
        user.setPassword(encoder.encode(usuarioRequest.getPassword()));
        usuarioRepository.save(user);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<TokenResposta> login(LoginRequest loginRequest) {
        log.info("Login user: {}", loginRequest);
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication auth = manager.authenticate(userToken);
        String token = tokenServico.generateToken((User) auth.getPrincipal());
        return ResponseEntity.status(HttpStatus.OK).body(new TokenResposta(token));
    }

    public ResponseEntity<String> recoverPassword(String email) {
        log.info("Recover password for email: {}", email);
        Optional<User> user = usuarioRepository.byEmail(email);
        if (user.isPresent()) {
            log.info("User found: {}", user.get());
            TokenResetSenha tokenResetSenha = TokenResetSenha.builder()
                    .token(UUID.randomUUID().toString())
                    .user(user.get())
                    .expiryDate(LocalDateTime.now())
                    .build();
            tokenResetSenhaRepository.save(tokenResetSenha);
            emailServico.sendEmail(user.get(), "tutora-ia.com/recuperar-senha/" + tokenResetSenha.getToken());
            return ResponseEntity.ok().build();
        }
        log.error("User not found for email: {}", email);
        return ResponseEntity.badRequest().build();
    }

    public ResponseEntity<MensagemPadraoResposta> resetPassword(String token, RecuperacaoSenhaRequest password) {
        log.info("Reset password for token: {}", token);
        Optional<TokenResetSenha> passwordResetToken = tokenResetSenhaRepository.findByToken(token);
        if (passwordResetToken.isPresent() && !passwordResetToken.get().getUsed()) {
            log.info("Token found: {}", passwordResetToken.get());
            LocalDateTime tokenCreationTime = passwordResetToken.get().getExpiryDate();
            LocalDateTime expirationTime = tokenCreationTime.plusHours(24);
            if (expirationTime.isBefore(LocalDateTime.now())) {
                log.error("Token expired");
                return ResponseEntity.badRequest().body(new MensagemPadraoResposta("Token expired"));
            }
            if (!password.getPassword().equals(password.getConfirmPassword())) {
                log.error("Password and confirm password not match");
                return ResponseEntity.badRequest().body(new MensagemPadraoResposta("Password and confirm password not match"));
            }
            User user = passwordResetToken.get().getUser();
            user.setPassword(encoder.encode(password.getPassword()));
            usuarioRepository.save(user);
            passwordResetToken.get().setUsed(true);
            return ResponseEntity.ok().body(new MensagemPadraoResposta("Password updated successfully"));
        }
        log.error("Token not found");
        return ResponseEntity.badRequest().body(new MensagemPadraoResposta("Recover password token not found"));
    }
}
