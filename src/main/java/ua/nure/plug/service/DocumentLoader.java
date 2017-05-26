package ua.nure.plug.service;

import java.util.Map;

public interface DocumentLoader {

    Map<String, String> loadFromDirectory(String directory);

}
