package com.mateusjose98.redisintegration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;

@SpringBootTest
class RedisIntegrationApplicationTests {

    @Test
    void helloJedis() {
        Jedis jedis = new Jedis("localhost", 6379);

        jedis.set("name", "Mateus");

        String resultado = jedis.echo("Hello Jedis!");

        System.out.println(resultado);
        System.out.println(jedis.get("name"));

        jedis.del("name");

        System.out.println(jedis.get("name"));
        jedis.close();
    }

    @Test
    void megaSena() {
        Jedis jedis = new Jedis("localhost", 6379);

        String chave = "resultado:megasena";
        String numerosDoUltimoSorteio = "2, 13, 24, 41, 42, 44";

        var resultado = jedis.set(chave, numerosDoUltimoSorteio);
        System.out.println(resultado);
        jedis.close();
    }

    @Test
    void buscaMegasena() {
        String dataDoSorteio1 = "04-09-2013";
        String numerosDoSorteio1 = "10, 11, 18, 42, 55, 56";
        String chave1 = String.format("resultado:%s:megasena",
                dataDoSorteio1);
        String dataDoSorteio2 = "07-09-2013";
        String numerosDoSorteio2 = "2, 21, 30, 35, 45, 50";
        String chave2 = String.format("resultado:%s:megasena",
                dataDoSorteio2);
        String dataDoSorteio3 = "21-09-2013";
        String numerosDoSorteio3 = "2, 13, 24, 41, 42, 44";
        String chave3 = String.format("resultado:%s:megasena",
                dataDoSorteio3);
        String dataDoSorteio4 = "02-10-2014";
        String numerosDoSorteio4 = "7, 15, 20, 23, 30, 41";
        String chave4 = String.format("resultado:%s:megasena",
                dataDoSorteio4);
        Jedis jedis = new Jedis("localhost", 6379);
        String resultado = jedis.mset(
                chave1, numerosDoSorteio1,
                chave2, numerosDoSorteio2,
                chave3, numerosDoSorteio3,
                chave4, numerosDoSorteio4
        );
        System.out.println(resultado);

        System.out.println("Resultado do sorteio de " + dataDoSorteio1 + ": " + jedis.get(chave1));
        System.out.println("Resultado do sorteio de " + dataDoSorteio2 + ": " + jedis.get(chave2));

        String mes = "09";
        String ano = "2013";

        String chave = "resultado:*-%02d-%04d:megasena";
        String k = String.format(chave, Integer.parseInt(mes), Integer.parseInt(ano));
        System.out.println("Chave: " + k);
        System.out.println("Resultado do sorteio de " + mes + "/" + ano + ": " + jedis.keys(k));

        jedis.close();

    }

}
