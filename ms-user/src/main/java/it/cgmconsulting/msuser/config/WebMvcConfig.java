package it.cgmconsulting.msuser.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")    // per qualunque sia il path richiamato
                .allowedOrigins("*")            // richieste da qualunque origine
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")  // metodi (verbi HTTP) permessi
                .maxAge(MAX_AGE_SECS);
    }
    
    
}
