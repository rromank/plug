package ua.nure.plug.service.shingle;

import ua.nure.plug.model.Document;
import ua.nure.plug.model.Shingle;

import java.util.List;

public interface ShingleService {

    int similar(Document document1, Document document2);

    List<Shingle> createShingles(List<String> words);

}
