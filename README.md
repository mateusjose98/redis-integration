

# Integração REDIS com Spring Boot
## Redis?

Redis significa REmote DIctionary Server. Diferente de um banco de dados
tradicional como MySQL ou Oracle, é categorizado como um banco de dados
não relacional, sendo muitas vezes referenciado pela sigla NOSQL (Not Only
SQL).

## Introdução ao CLI do Redis:
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

Buscando valores por padrão:
```
127.0.0.1:6379> keys resultado*
1) "resultado:18-10-2014:megasena"
2) "resultado:17-10-2014:megasena"
```

Inserindo múltiplos valores:

```
127.0.0.1:6379> MSET 61270072323 'mateus' 12332112321 'vanessa' 09809809812 'bernardo'
```

Buscas mais complexas:
- Buscando keys dos dias antes de 10, especificamente 1,2 ou 3 de qualquer mês e ano:
```
.0.1:6379> KEYS "resultado:0[123]-??-????"
1) "resultado:03-01-2000"
2) "resultado:02-01-2000"
3) "resultado:01-01-2000"

```

Veja que o padrão que fizemos é TYPE:IDENTIFIER;

Trabalhando com hashes
- Veja que o GET e SET não funciona para hashes, pois o Redis entende que o valor é um hash e não uma string.
```
127.0.0.1:6379> HSET resultado:20-09-2022:megasena 'numeros' '10,20,30,30,40'
(integer) 1
127.0.0.1:6379> HSET resultado:20-09-2022:megasena 'ganhadores' '240'
(integer) 1
127.0.0.1:6379> GET resultado:20-09-2022:megasena
(error) WRONGTYPE Operation against a key holding the wrong kind of value
127.0.0.1:6379> HGET resultado:20-09-2022:megasena
(error) ERR wrong number of arguments for 'hget' command
127.0.0.1:6379> HGET resultado:20-09-2022:megasena 'numeros'
"10,20,30,30,40"
127.0.0.1:6379> HGET resultado:20-09-2022:megasena 'ganhadores'
"240"
127.0.0.1:6379> 

```
Para remover em hash `HDEL`

Exemplo de armazenamento de sessão:
- Queremos armazenar o carrinho de compras de um usuário. Isso é útil para manter o carrinho inderentende de instâncias do servidor.
```
127.0.0.1:6379> HSET 'sessao:user:1231203120' 'nome' 'mateus'
(integer) 1
127.0.0.1:6379> HSET 'sessao:user:1231203120' 'total_produtos' '3'
(integer) 1
127.0.0.1:6379> HSET 'sessao:user:1231203120' 'valor' '450.40'
(integer) 1
127.0.0.1:6379> HGET 'sessao*'
(error) ERR wrong number of arguments for 'hget' command
127.0.0.1:6379> HGET 'sessao:user:1231203120' 'nome'
"mateus"
```
Para o nosso cenário, queremos que o carrinho expire após 1 minuto (para teste). Para isso, usamos o comando `EXPIRE`:
```
127.0.0.1:6379> EXPIRE 'sessao:user:1231203120' 60
```
Verificando quanto tempo falta para expirar:
```
127.0.0.1:6379> EXPIRE 'sessao:user:1231203120' 60
(integer) 1
127.0.0.1:6379> TTL 'sessao:user:1231203120'
(integer) 22
127.0.0.1:6379> TTL 'sessao:user:1231203120'
(integer) 20
127.0.0.1:6379> TTL 'sessao:user:1231203120'
(integer) 19
127.0.0.1:6379> TTL 'sessao:user:1231203120'
(integer) 19
127.0.0.1:6379> TTL 'sessao:user:1231203120'
(integer) -2
```

Quando temos um número armazenado, poderemos incrementar ou decrementar o valor:
```
127.0.0.1:6379> INCR pagina:/contato:25-05-2015
(integer) 1
127.0.0.1:6379> INCR pagina:/contato:25-05-2015
(integer) 2
127.0.0.1:6379> INCR pagina:/contato:25-05-2015
(integer) 3
127.0.0.1:6379> INCRBY pagina:/contato:25-05-2015 30
(integer) 33
```

Agora, queremos salvar quem acessou nosso site, que dias o andré acessou o sistema? que dias a maria acessou?
Usuário X acessou o Site: true ou false?

O redis já possui uma estrutura otimizada para salvar um conjunto de bits, o `bitset`. 
```
                         ID  t/f
SETBIT acesso:25-05-2015 1500 1
SETBIT acesso:25-05-2015 1000 1
SETBIT acesso:25-05-2015 1300 1

GETBIT acesso:25-05-2015 1300

// contando os acessos do dia 25
127.0.0.1:6379> BITCOUNT acesso:25-05-2015
(integer) 3

```

Conseguimos, assim otimizar a memória, pois o redis armazena os bits de forma otimizada. Além disso, conseguimos buscar pelo ID do nosso usuário.


Como saber os usuários que acessaram no dia 25 e 26? Note que se fosse OR, bastaria fazer um `BITOP OR`:
```

127.0.0.1:6379> BITOP AND acesso:25-e-26-05-2015 acesso:25-05-2015 acesso:26-05-2015
(integer) 188
127.0.0.1:6379> GETBIT acesso:25-e-26-05-2015 1500
(integer) 0
127.0.0.1:6379> GETBIT acesso:25-e-26-05-2015 1300
(integer) 1
```