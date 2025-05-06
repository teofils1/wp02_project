package com.example.pokevault.controller;

import com.example.pokevault.model.Card;
import com.example.pokevault.model.User;
import com.example.pokevault.service.CardService;
import com.example.pokevault.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cards")
public class CardController {

    @Autowired
    private CardService cardService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String getAllCards(Model model) {
        List<Card> cards = cardService.getAllCards();
        model.addAttribute("cards", cards);
        return "cards";
    }
    
    @GetMapping("/my-cards")
    public String getMyCards(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        List<Card> userCards = cardService.getCardsByOwner(user);
        model.addAttribute("cards", userCards);
        return "my-cards";
    }
    
    @GetMapping("/{id}")
    public String getCardDetails(@PathVariable Long id, Model model) {
        Optional<Card> optionalCard = cardService.getCardById(id);
        
        if (optionalCard.isPresent()) {
            model.addAttribute("card", optionalCard.get());
            return "card-details";
        } else {
            return "redirect:/cards";
        }
    }
    
    @GetMapping("/add")
    public String showAddCardForm(Model model) {
        model.addAttribute("card", new Card());
        return "add-card";
    }
    
    @PostMapping("/add")
    public String addCard(@ModelAttribute Card card, Authentication authentication) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        card.setOwner(user);
        cardService.saveCard(card);
        
        return "redirect:/cards/my-cards";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditCardForm(@PathVariable Long id, Model model, Authentication authentication) {
        Optional<Card> optionalCard = cardService.getCardById(id);
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            
            // Check if current user is the owner
            if (card.getOwner().getId().equals(user.getId())) {
                model.addAttribute("card", card);
                return "edit-card";
            }
        }
        
        return "redirect:/cards/my-cards";
    }
    
    @PostMapping("/edit/{id}")
    public String updateCard(@PathVariable Long id, @ModelAttribute Card card, Authentication authentication) {
        Optional<Card> optionalCard = cardService.getCardById(id);
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (optionalCard.isPresent()) {
            Card existingCard = optionalCard.get();
            
            // Check if current user is the owner
            if (existingCard.getOwner().getId().equals(user.getId())) {
                existingCard.setName(card.getName());
                existingCard.setType(card.getType());
                existingCard.setRarity(card.getRarity());
                existingCard.setDescription(card.getDescription());
                existingCard.setImageUrl(card.getImageUrl());
                
                cardService.saveCard(existingCard);
            }
        }
        
        return "redirect:/cards/my-cards";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteCard(@PathVariable Long id, Authentication authentication) {
        Optional<Card> optionalCard = cardService.getCardById(id);
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (optionalCard.isPresent()) {
            Card card = optionalCard.get();
            
            // Check if current user is the owner
            if (card.getOwner().getId().equals(user.getId())) {
                cardService.deleteCard(id);
            }
        }
        
        return "redirect:/cards/my-cards";
    }
    
    @GetMapping("/search")
    public String searchCards(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String type,
                             @RequestParam(required = false) String rarity,
                             Model model) {
        
        List<Card> cards;
        
        if (name != null && !name.isEmpty()) {
            cards = cardService.searchCardsByName(name);
        } else if (type != null && !type.isEmpty()) {
            cards = cardService.getCardsByType(type);
        } else if (rarity != null && !rarity.isEmpty()) {
            cards = cardService.getCardsByRarity(rarity);
        } else {
            cards = cardService.getAllCards();
        }
        
        model.addAttribute("cards", cards);
        return "cards";
    }
}
