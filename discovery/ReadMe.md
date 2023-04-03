# Discovery

## Eureka Service Registry
Gestisce un registro dei servizi disponibili in modo che i moduli possano registrarvisi e trovarsi a vicenda.
### http://localhost:8761
- @EnableEurekaServer  
  anteposto alla classe del main abilita Eureka  
- Importare nel [POM](./pom.xml)
- Impostare nel file yaml  
  `server.port: 8761`

## Elenco Post Pubblici
http://localhost:8090/post/swagger-ui/index.html#/post-controller/getPosts

---