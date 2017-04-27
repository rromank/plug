package ua.nure.plug.service.shingle;

import ua.nure.plug.model.Shingle;
import ua.nure.plug.service.text.Token;

import java.util.List;

public interface ShingleService {

    List<Shingle> createShingles(List<Token> words);

}
