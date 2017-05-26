package ua.nure.plug.service.impl;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import ua.nure.plug.service.HashService;

@Service
public class HashServiceImpl implements HashService {

    @Override
    public String md5(String string) {
        return DigestUtils.md5Hex(string);
    }

    @Override
    public String sha1(String string) {
        return DigestUtils.sha1Hex(string);
    }

    @Override
    public long karpRabin(String string) {
        int prime = 31;
        long result = 0;
        for (int i = 0; i < string.length(); i++) {
            result += Math.pow(prime, string.length() - 1 - i) * (string.charAt(i));
        }
        return result;
    }

}
