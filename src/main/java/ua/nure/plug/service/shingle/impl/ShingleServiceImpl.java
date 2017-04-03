package ua.nure.plug.service.shingle.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.Document;
import ua.nure.plug.model.Shingle;
import ua.nure.plug.service.HashService;
import ua.nure.plug.service.shingle.ShingleService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShingleServiceImpl implements ShingleService {

    private static final int SHINGLE_LENGTH = 3;

    @Autowired
    private HashService hashService;

    @Override
    public List<Shingle> createShingles(List<String> words) {
        List<Shingle> shingles = new ArrayList<>();
        for (int i = 0; i < words.size() - SHINGLE_LENGTH; i++) {
            String word = words.subList(i, i + SHINGLE_LENGTH).stream()
                    .collect(Collectors.joining());

            Shingle shingle = new Shingle();
            shingle.setShingle(word);
            shingle.setOffset(i);
            shingle.setHash(hashService.md5(word));
            shingles.add(shingle);
        }
        return shingles;
    }

    @Override
    public int similar(Document document1, Document document2) {
        List<Shingle> intersection = new ArrayList<>(document1.getShingles());
        List<Shingle> shingles2 = document2.getShingles();

        intersection.retainAll(shingles2);

        return (intersection.size() / document1.getWords().size()) * 100;
    }

}
