# BackEnd di un blog
L'applicazione (IN FASE DI SVILUPPO) consiste nella realizzazione di un Blog.  
Strutturata secondo l'architettura a microservizi, l'applicazione è composta da più componenti di piccole dimensioni, detti servizi o MicroServizi, indipendenti e che comunicano tra loro tramite API.  

### Breve Spiegazione di Microservizi
Tali componenti eseguono ciascuno un proprio processo applicativo come un servizio, ovvero ogni servizio esegue una sola funzione.  
Questa organizzazione permette l'esecuzione indipendente di ciascun servizio; altresì ne permette il controllo, l'aggiornamento, la distribuzione e il ridimensionato del singolo per rispondere alla richiesta di funzioni specifiche di un’applicazione;.
Inoltre permette la realizzazione degli stessi con tecnologie differenti e la collocazione su server distinti.

## Librerie
1. Auth0 JWT (Json Web Token)
1. Lombok - Libreria di tipo `APT` (`Annotation Processing Tool`)
1. Resilience4j - Libreria di tolleranza ai guasti
    - CircuitBreaker
1. Actuator - stato di salute del MicroServ
1. Logging
1. Zipkin & Sleuth
1. Eureka
1. Swagger UI - Pg web Test API
1. LoadBalancer
