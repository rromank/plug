package ua.nure.plug.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import ua.nure.plug.model.Word;

public interface WordRepository extends MongoRepository<Word, ObjectId> {

    @Query(value = "{'forms': ?0}")
    Word findNoun(String wordForm);

}
