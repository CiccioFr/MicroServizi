# Post

## restitusce lo stato di salute del MicroServizio
- collegato alla dipendenza nel pom.xml
- http://{host}:{gateway port}/{context-path}/actuator/health
- http://localhost:8090/post/actuator/health

### Inserisce nuovo Post
- http://localhost:8090/post/swagger-ui/index.html#/post-controller/save

### Ricerca Post Pubblici (Publushed = True)
- http://localhost:8090/post/swagger-ui/index.html#/post-controller/getPosts

### Cambio dello stato "Pubblicato" a true del Post da parte di un ROLE_ADMIN
- http://localhost:8090/post/swagger-ui/index.html#/post-controller/publishPost

### Modifica Post Esistente
- http://localhost:8090/post/swagger-ui/index.html#/post-controller/update  
Richiede:
  - `id` del Post da modificare 
