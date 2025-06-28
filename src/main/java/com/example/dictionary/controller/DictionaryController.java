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
@CrossOrigin(origins = "*")
public class DictionaryController {

    @Autowired
    private DictionaryService dictionaryService;

    @Autowired
    private SavedWordRepository savedWordRepository;

    @GetMapping("/definition")
    public ResponseEntity<String> getDefinition(@RequestParam String word) {
        String definition = dictionaryService.fetchDefinition(word);
        return ResponseEntity.ok(definition);
    }

    @PostMapping("/save")
    public ResponseEntity<String> saveWord(@RequestBody SavedWord word) {
        savedWordRepository.save(word);
        return ResponseEntity.ok("Word saved!");
    }

    @GetMapping("/saved-words")
    public List<SavedWord> getSavedWords() {
        return savedWordRepository.findAll();
    }

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

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteWord(@PathVariable String id) {
        savedWordRepository.deleteById(id);
        return ResponseEntity.ok("Word deleted successfully.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateMeaning(@PathVariable String id, @RequestBody SavedWord updatedWord) {
        Optional<SavedWord> optionalWord = savedWordRepository.findById(id);
        if (optionalWord.isPresent()) {
            SavedWord existingWord = optionalWord.get();
            existingWord.setMeaning(updatedWord.getMeaning());
            savedWordRepository.save(existingWord);
            return ResponseEntity.ok("Word updated successfully.");
        } else {
            return ResponseEntity.status(404).body("Word not found.");
        }
    }
}
