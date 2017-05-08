package ua.nure.plug.service.normalization;

import org.springframework.stereotype.Service;

@Service
public class LowercaseNormalizer extends Normalizer {

    @Override
    String normalizeText(String text) {
        return text.toLowerCase();
    }

}
