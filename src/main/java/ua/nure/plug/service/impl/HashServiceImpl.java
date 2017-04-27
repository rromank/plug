package ua.nure.plug.service.impl;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import ua.nure.plug.service.HashService;

@Service
public class HashServiceImpl implements HashService {

    @Override
    public String md5(String word) {
        return DigestUtils.md5Hex(word);
    }

}
