package com.example.pokevault.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pokevault.model.Card;
import com.example.pokevault.model.User;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByOwner(User owner);
    List<Card> findByNameContainingIgnoreCase(String name);
    List<Card> findByType(String type);
    List<Card> findByRarity(String rarity);
}
