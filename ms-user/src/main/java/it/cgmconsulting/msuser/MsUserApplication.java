package it.cgmconsulting.msuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
@EnableEurekaClient
public class MsUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsUserApplication.class, args);
    }

    // Occorre mettere il bean del passwordEncoder,
    // per istanziare la classe e poter criptare la PassWord
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // per i profili:
    // per qualunque metodo posso indicare un profilo..
    @Profile("sviluppo")
    @Bean   // indiva che viene inizializato subito all'avvio dell'applicazione
    public void provaProfilo(){
        System.out.println("------------------ Sto utilizzando il profilo di Sviluppo");
    }
}
