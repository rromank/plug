package ua.nure.plug.service.normalization;

public abstract class Normalizer {

    private Normalizer next;

    public Normalizer setNext(Normalizer normalizer) {
        next = normalizer;
        return normalizer;
    }

    public String normalize(String text) {
        text = normalizeText(text);
        if (next != null) {
            text = next.normalize(text);
        }
        return text;
    }

    abstract String normalizeText(String text);

}
