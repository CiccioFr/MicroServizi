# User

### Registrazione utente
- http://localhost:8090/user/swagger-ui/index.html#/user-controller/signup

### Login utente
- http://localhost:8090/user/swagger-ui/index.html#/user-controller/{id}/{authority}

## restitusce lo stato di salute del MicroServizio
- collegato alla dipendenza nel pom.xml
- http://{host}:{gateway port}/{context-path}/actuator/health
- http://localhost:8090/user/actuator/health