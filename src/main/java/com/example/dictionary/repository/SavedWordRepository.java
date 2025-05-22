package com.example.dictionary.repository;

import com.example.dictionary.model.SavedWord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SavedWordRepository extends MongoRepository<SavedWord, String> {
    // You can add custom query methods here if needed
}
