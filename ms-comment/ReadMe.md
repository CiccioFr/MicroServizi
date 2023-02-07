# Post

## restitusce lo stato di salute del MicroServizio
- collegato alla dipendenza nel pom.xml
- http://{host}:{gateway port}/{context-path}/actuator/health
- http://localhost:8090/comment/actuator/health

### Root
- http://localhost:8090/comment/swagger-ui/index.html

### Inserisce nuovo Comment
- Tipo `PUT`
- http://localhost:8090/comment/swagger-ui/index.html#/

### Mostra tutti i Comment del Post
- Tipo `GET`
- http://localhost:8090/comment/swagger-ui/index.html#/
