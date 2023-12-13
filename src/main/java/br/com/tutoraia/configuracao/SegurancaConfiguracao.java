package br.com.tutoraia.configuracao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SegurancaConfiguracao extends WebSecurityConfigurerAdapter {

    @Autowired
    FiltroSeguranca filtroSeguranca;

    public void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf().disable()
                .authorizeRequests().antMatchers(HttpMethod.POST, "/v1/auth/cadastro").permitAll()
                .and().authorizeRequests().antMatchers("/v2/api-docs",
                        "/configuracao/ui",
                        "/recursos-swagger/**",
                        "/configuracao/seguranca",
                        "/swagger-ui.html",
                        "/webjars/**").permitAll()
                .and().authorizeRequests().antMatchers(HttpMethod.POST, "/v1/auth/login").permitAll()
                .and().authorizeRequests().antMatchers(HttpMethod.POST, "/v1/user/recuperar-senha").permitAll()
                .and().authorizeRequests().antMatchers(HttpMethod.PATCH, "/v1/user/resetar-senha/**").permitAll()
                .anyRequest().authenticated().and().addFilterBefore(filtroSeguranca, UsernamePasswordAuthenticationFilter.class).cors();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
