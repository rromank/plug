package ua.nure.plug;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
//@EnableMongoRepositories(basePackages = "ua.nure.plug.repository.mongo")
@EnableElasticsearchRepositories(basePackages = "ua.nure.plug.repository.elastic")
public class AppConfig {

//    @Bean
//    public MongoClient mongo() {
//        return new MongoClient(new MongoClientURI("mongodb://plug:plug@ds145379.mlab.com:45379/plug"));
//    }
//
//    @Bean
//    public MongoTemplate mongoTemplate() {
//        return new MongoTemplate(mongo(), "plug");
//    }

}
