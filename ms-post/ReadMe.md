# Post

## Actuator
restitusce lo stato di salute del MicroServizio
- http://{host}:{gateway port}/{context-path}/actuator/health
- http://localhost:8090/post/actuator/health

### Root
- http://localhost:8090/post/swagger-ui/index.html

### Inserisce nuovo Post
- Tipo `PUT`
- http://localhost:8090/post/swagger-ui/index.html#/post-controller/save

### Ricerca Post Pubblici (Publushed = True)
- Tipo `GET`
- http://localhost:8090/post/swagger-ui/index.html#/post-controller/getPosts

### Pubblicazione di un Post - Cambio dello stato "published" a true da parte di un ROLE_ADMIN
- Tipo `PATCH`
- http://localhost:8090/post/swagger-ui/index.html#/post-controller/publishPost

### Modifica Post Esistente
- Tipo `PATCH`
- http://localhost:8090/post/swagger-ui/index.html#/post-controller/update  
Richiede:
  - `id` del Post da modificare 

### verifica se post esiste e se pubblicato
- Tipo `GET`
- http://localhost:8090/post/swagger-ui/index.html#/post-controller/getPostDetail  
  Richiede:
  - `id` del Post 
