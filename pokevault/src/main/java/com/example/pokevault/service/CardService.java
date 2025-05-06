package com.example.pokevault.service;

import com.example.pokevault.model.Card;
import com.example.pokevault.model.User;
import com.example.pokevault.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;
    
    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }
    
    public Optional<Card> getCardById(Long id) {
        return cardRepository.findById(id);
    }
    
    public List<Card> getCardsByOwner(User owner) {
        return cardRepository.findByOwner(owner);
    }
    
    public List<Card> searchCardsByName(String name) {
        return cardRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Card> getCardsByType(String type) {
        return cardRepository.findByType(type);
    }
    
    public List<Card> getCardsByRarity(String rarity) {
        return cardRepository.findByRarity(rarity);
    }
    
    public Card saveCard(Card card) {
        return cardRepository.save(card);
    }
    
    public void deleteCard(Long id) {
        cardRepository.deleteById(id);
    }
}
