package com.example.pokevault.repository;

import com.example.pokevault.model.Auction;
import com.example.pokevault.model.Card;
import com.example.pokevault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
    List<Auction> findByActive(boolean active);
    List<Auction> findBySeller(User seller);
    List<Auction> findByCurrentBidder(User bidder);

    @Query("SELECT a FROM Auction a WHERE a.active = true AND a.card.name LIKE %?1%")
    List<Auction> findActiveAuctionsByCardName(String cardName);
    
    @Query("SELECT a FROM Auction a WHERE a.active = true AND a.endTime > ?1 ORDER BY a.endTime ASC")
    List<Auction> findActiveAuctionsOrderByEndTime(LocalDateTime currentTime);
    
    @Query("SELECT a FROM Auction a WHERE a.active = true AND (a.card.name LIKE %?1%)")
    List<Auction> findByTitleOrCardNameContaining(String keyword);
    
    @Query("SELECT a FROM Auction a WHERE a.active = true AND a.card.rarity = ?1")
    List<Auction> findByCardRarity(String rarity);
}