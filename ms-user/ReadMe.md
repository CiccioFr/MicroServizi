# User

## restitusce lo stato di salute del MicroServizio
- collegato alla dipendenza nel pom.xml
- http://{host}:{gateway port}/{context-path}/actuator/health
- http://localhost:8090/user/actuator/health

### Registrazione di un nuovo utente - `PUT`
- http://localhost:8090/user/swagger-ui/index.html#/user-controller/signup

### Login utente - `GET`
- http://localhost:8090/user/swagger-ui/index.html#/user-controller/  
Richiede:
  - `id` 

### Ricerca di Users per Ruolo (passato come parametro) - `GET`
- http://localhost:8090/user/swagger-ui/index.html#/user-controller/  
Richiede:
  - `id`  
  