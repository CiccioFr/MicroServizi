# Discovery

## Eureka Service Registry
Gestisce un registro dei servizi disponibili in modo che i moduli possano registrarvisi e trovarsi a vicenda.
- @EnableEurekaServer  
  anteposto alla classe del main abilita Eureka  
- Importare nel [POM](./pom.xml)
- Impostare nel file yaml
  `server.port: 8761`