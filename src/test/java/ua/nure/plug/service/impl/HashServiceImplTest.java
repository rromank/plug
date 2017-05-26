package ua.nure.plug.service.impl;

import org.junit.Test;
import ua.nure.plug.service.HashService;

public class HashServiceImplTest {


    @Test
    public void shouldReturnKarpRabinHash() {
        // given
        String string = "лише";
        String string2 = "лише s";
        HashService hashService = new HashServiceImpl();

        // when
        long hash = hashService.karpRabin(string);
        long hash2 = hashService.karpRabin(string2);

        // then
        System.out.println(hash);
        System.out.println(hash2);

    }

}