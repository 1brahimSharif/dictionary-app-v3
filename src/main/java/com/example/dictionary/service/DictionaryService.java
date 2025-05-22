package com.example.dictionary.service;

import com.example.dictionary.model.SavedWord;
import com.example.dictionary.repository.SavedWordRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class DictionaryService {

    @Value("${merriam.api.key}")
    private String apiKey;

    private final String BASE_URL = "https://www.dictionaryapi.com/api/v3/references/collegiate/json/";

    private final RestTemplate restTemplate;
    private final SavedWordRepository savedWordRepository;

    public DictionaryService(RestTemplate restTemplate, SavedWordRepository savedWordRepository) {
        this.restTemplate = restTemplate;
        this.savedWordRepository = savedWordRepository;
    }

    public String fetchDefinition(String word) {
        try {
            String url = BASE_URL + word + "?key=" + apiKey;
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }

    public void saveWord(String word, String meaning) {
        SavedWord savedWord = new SavedWord(word, meaning);
        savedWordRepository.save(savedWord);
    }

    public List<SavedWord> getAllSavedWords() {
        return savedWordRepository.findAll();
    }

    public SavedWord getRandomSavedWord() {
        List<SavedWord> allWords = savedWordRepository.findAll();
        if (allWords.isEmpty()) return null;
        Random random = new Random();
        return allWords.get(random.nextInt(allWords.size()));
    }
}
