package br.com.tutoraia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TutoraIAAplicacao {

    public static void main(String[] args) {
        SpringApplication.run(TutoraIAAplicacao.class, args);
        System.out.println("API Running");
    }

}
