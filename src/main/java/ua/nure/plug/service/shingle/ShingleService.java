package ua.nure.plug.service.shingle;

import ua.nure.plug.model.Shingle;

import java.util.List;

public interface ShingleService {

    List<Shingle> createShingles(List<String> words);

}
