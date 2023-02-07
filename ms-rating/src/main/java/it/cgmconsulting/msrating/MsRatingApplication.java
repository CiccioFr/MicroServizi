package it.cgmconsulting.msrating;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
// con Spring ver. 3.0.2 @EnableEurekaClient è sostituito da
@EnableDiscoveryClient
public class MsRatingApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsRatingApplication.class, args);
    }

    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
