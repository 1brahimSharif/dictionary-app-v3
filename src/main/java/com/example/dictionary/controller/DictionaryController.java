package com.example.dictionary.controller;

import com.example.dictionary.model.SavedWord;
import com.example.dictionary.repository.SavedWordRepository;
import com.example.dictionary.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow requests from all origins (especially useful for testing frontend)
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private SavedWordRepository savedWordRepository;

    // Fetch definition from Merriam-Webster
    @GetMapping("/definition")
    public ResponseEntity<String> getDefinition(@RequestParam String word) {
        String definition = dictionaryService.fetchDefinition(word);
        return ResponseEntity.ok(definition);
    }

    // Save selected word to MongoDB
    @PostMapping("/save")
    public ResponseEntity<String> saveWord(@RequestBody SavedWord word) {
        savedWordRepository.save(word);
        return ResponseEntity.ok("Word saved!");
    }

    // Get all saved words (used in saved.html)
    @GetMapping("/saved-words")
    public List<SavedWord> getSavedWords() {
        return savedWordRepository.findAll();
    }

    // Get a random saved word (Word of the Day)
    @GetMapping("/wotd")
    public ResponseEntity<?> getWordOfTheDay() {
        List<SavedWord> allWords = savedWordRepository.findAll();
        if (allWords.isEmpty()) {
            return ResponseEntity.ok().body("{\"word\":\"None\",\"meaning\":\"No words saved yet.\"}");
        }
        Random random = new Random();
        SavedWord randomWord = allWords.get(random.nextInt(allWords.size()));
        return ResponseEntity.ok(randomWord);
    }
}
