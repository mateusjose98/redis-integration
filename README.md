

## Integração REDIS com Spring Boot

Suba o docker-compose.yml e entre no container.

```
/data # redis-cli ping
PONG
/data # redis-cli
127.0.0.1:6379> ECHO 'mensagem'
"mensagem"
127.0.0.1:6379> 
```

Armazenando/recuperando/atualizando valores no Redis (retorna o valor ou nil), tão fácil quanto GET,SET,DEL
:smiley: 
.
```
127.0.0.1:6379> SET 'contrato' 100000
OK
127.0.0.1:6379> GET contrato
"100000"
127.0.0.1:6379> GET contrato2
(nil)
127.0.0.1:6379> 
127.0.0.1:6379> SET contrato 10000011
OK
127.0.0.1:6379> 
127.0.0.1:6379> GET contrato
"10000011"
127.0.0.1:6379> DEL contrato
(integer) 1
127.0.0.1:6379> GET contrato
(nil)

```
Buscar todas as chaves do redis:
```
KEYS * // padrão
```
Boas práticas:
```
127.0.0.1:6379> SET resultado:17-10-2014:megasena '2,12,34,34,23,23'
OK
127.0.0.1:6379> keys *
1) "b"
2) "resultado:17-10-2014:megasena"
3) "a"
4) "ab"
   127.0.0.1:6379> SET resultado:18-10-2014:megasena '20,120,88,34,23,22'
   OK
```

Inserindo múltiplos valores:

```
127.0.0.1:6379> MSET 61270072323 'mateus' 12332112321 'vanessa' 09809809812 'bernardo'
```


