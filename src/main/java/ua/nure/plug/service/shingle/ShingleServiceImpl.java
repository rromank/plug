package ua.nure.plug.service.shingle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.nure.plug.model.Range;
import ua.nure.plug.model.Shingle;
import ua.nure.plug.service.HashService;
import ua.nure.plug.service.text.Token;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShingleServiceImpl implements ShingleService {

    private static final int SHINGLE_LENGTH = 3;

    @Autowired
    private HashService hashService;

    @Override
    public List<Shingle> createShingles(List<Token> tokens) {
        List<Shingle> shingles = new ArrayList<>();
        for (int fromIndex = 0; fromIndex <= tokens.size() - SHINGLE_LENGTH; fromIndex++) {
            int toIndex = fromIndex + SHINGLE_LENGTH < tokens.size() ? fromIndex + SHINGLE_LENGTH : tokens.size();
            List<Token> shingleTokens = tokens.subList(fromIndex, toIndex);
            shingles.add(createShingle(shingleTokens));
        }
        return shingles;
    }

    private Shingle createShingle(List<Token> tokenSequence) {
        String sequence = tokenSequence.stream()
                .map(Token::getToken)
                .collect(Collectors.joining());

        return Shingle.builder()
                .range(Range.of(tokenSequence.get(0).getRange().getStart(),
                        tokenSequence.get(tokenSequence.size() - 1).getRange().getEnd()))
                .shingle(sequence)
                .hash(hashService.md5(sequence))
                .build();
    }

}
