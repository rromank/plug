package ua.nure.plug.service.minhash;

import ua.nure.plug.model.Shingle;

import java.util.List;

public interface MinHashService {

    List<Integer> createMinHashSignature(List<Shingle> words);

}
