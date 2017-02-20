package ua.nure.plug;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(mongoTemplateRef = "mongoTemplate", basePackages = "ua.nure.plug")
public class AppConfig {

    @Bean
    public MongoClient mongo() {
        return new MongoClient(new MongoClientURI("mongodb://plug:plug@ds145379.mlab.com:45379/plug"));
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), "plug");
    }

}
