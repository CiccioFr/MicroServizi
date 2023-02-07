# User

## Actuator
restitusce lo stato di salute del MicroServizio
- http://{host}:{gateway port}/{context-path}/actuator/health
- http://localhost:8090/user/actuator/health

### Root
- http://localhost:8090/user/swagger-ui/index.html

### Registrazione di un nuovo utente
- Tipo `PUT`
- http://localhost:8090/user/swagger-ui/index.html#/user-controller/signup

### Login utente
- Tipo `GET`
- http://localhost:8090/user/swagger-ui/index.html#/user-controller/  
Richiede:
  - `id` 

### Ricerca di Users per Ruolo (passato come parametro)
- Tipo `GET`
- http://localhost:8090/user/swagger-ui/index.html#/user-controller/  
Richiede:
  - `id`  
  