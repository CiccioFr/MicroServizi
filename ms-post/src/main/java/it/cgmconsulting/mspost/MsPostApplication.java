package it.cgmconsulting.mspost;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@EnableEurekaClient
@SpringBootApplication
public class MsPostApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsPostApplication.class, args);
    }

    // solo nel caso del RestTemplate aggiungere @LoadBalanced
    // @LoadBalanced fa in modo che tutte le richieste vengono reindirizzate dal microservizio che se neoccupa
    @LoadBalanced // capisce che il giro che fa e la redirect, sa che passa dal gateway e verrà redirect
	//
    @Bean   // il bean del PasswordEncoder (cosa che nella monolitica, dovrebbe aver messo nelle classi di Spring Security)
    //  vedere https://reflectoring.io/spring-resttemplate/
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    // per i profili:
    // per qualunque metodo posso indicare un profilo..
    @Profile("sviluppo")
    @Bean   // indiva che viene inizializato subito all'avvio dell'applicazione
    public void provaProfilo(){
        System.out.println("------------------ Sto utilizzando il profilo di Sviluppo");
    }
}
