package ua.nure.plug.service;

public interface HashService {

    String md5(String string);

    String sha1(String string);

    long karpRabin(String string);

}
