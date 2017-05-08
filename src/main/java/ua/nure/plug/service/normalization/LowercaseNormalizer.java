package ua.nure.plug.service.normalization;

public class LowercaseNormalizer extends Normalizer {

    @Override
    String normalizeText(String text) {
        return text.toLowerCase();
    }

}
