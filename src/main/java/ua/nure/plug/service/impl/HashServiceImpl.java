package ua.nure.plug.service.impl;


import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import ua.nure.plug.service.HashService;

import java.util.Arrays;

@Service
public class HashServiceImpl implements HashService {

    @Override
    public String md5(String word) {
        return Arrays.toString(DigestUtils.md5(word));
    }

}
