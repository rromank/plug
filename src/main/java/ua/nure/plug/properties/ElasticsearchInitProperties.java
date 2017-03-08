package ua.nure.plug.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "elastic.init")
public class ElasticsearchInitProperties {

    private Boolean clearDictionaries;
    private String dictionaryZipRu;
    private String dictionaryZipUa;

}
