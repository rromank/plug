package ua.nure.plug.service.shingle.impl;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.Document;
import ua.nure.plug.model.Shingle;
import ua.nure.plug.service.HashService;
import ua.nure.plug.service.shingle.ShingleService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class ShingleServiceImpl implements ShingleService {

    private static final int SHINGLE_LENGTH = 3;

    @Autowired
    private HashService hashService;

    @Override
    public List<Shingle> createShingles(List<String> words) {
        AtomicInteger offset = new AtomicInteger(0);
        return Lists.partition(words, SHINGLE_LENGTH).stream()
                .map(strings -> strings.stream().collect(Collectors.joining()))
                .map(sequence -> Shingle.builder()
                        .offset(offset.getAndIncrement())
                        .shingle(sequence)
                        .hash(hashService.md5(sequence))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public int similar(Document document1, Document document2) {
        List<Shingle> intersection = new ArrayList<>(document1.getShingles());
        List<Shingle> shingles2 = document2.getShingles();

        intersection.retainAll(shingles2);

        return (intersection.size() / document1.getWords().size()) * 100;
    }

}
