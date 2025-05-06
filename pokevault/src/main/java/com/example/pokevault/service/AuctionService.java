package com.example.pokevault.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.pokevault.model.Auction;
import com.example.pokevault.model.Bid;
import com.example.pokevault.model.Card;
import com.example.pokevault.model.User;
import com.example.pokevault.repository.AuctionRepository;
import com.example.pokevault.repository.BidRepository;
import com.example.pokevault.repository.CardRepository;

@Service
public class AuctionService {

    @Autowired
    private AuctionRepository auctionRepository;
    
    @Autowired
    private BidRepository bidRepository;
    
    @Autowired
    private CardRepository cardRepository;
    
    @Autowired
    private CardService cardService;
    
    public List<Auction> getAllAuctions() {
        return auctionRepository.findAll();
    }
    
    public List<Auction> getActiveAuctions() {
        return auctionRepository.findByActive(true);
    }
    
    public List<Auction> getActiveAuctionsOrderByEndTime() {
        return auctionRepository.findActiveAuctionsOrderByEndTime(LocalDateTime.now());
    }
    
    public List<Auction> searchAuctions(String keyword) {
        return auctionRepository.findByTitleOrCardNameContaining(keyword);
    }
    
    public List<Auction> getAuctionsByRarity(String rarity) {
        return auctionRepository.findByCardRarity(rarity);
    }
    
    public List<Auction> getAuctionsBySeller(User seller) {
        return auctionRepository.findBySeller(seller);
    }
    
    public List<Auction> getAuctionsByBidder(User bidder) {
        return auctionRepository.findByCurrentBidder(bidder);
    }
    
    public List<Auction> searchAuctionsByCardName(String cardName) {
        return auctionRepository.findActiveAuctionsByCardName(cardName);
    }
    
    public Optional<Auction> getAuctionById(Long id) {
        return auctionRepository.findById(id);
    }
    
    @Transactional
    public Auction createAuction(Card card, User seller, BigDecimal startingPrice, 
                              int durationInDays) {
        
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusDays(durationInDays);
        
        Auction auction = new Auction(card, seller, startingPrice, startTime, endTime);
        return auctionRepository.save(auction);
    }
    
    @Transactional
    public boolean placeBid(Long auctionId, User bidder, BigDecimal amount) {
        Optional<Auction> optionalAuction = auctionRepository.findById(auctionId);
        
        if (optionalAuction.isPresent()) {
            Auction auction = optionalAuction.get();
            
            // Check if auction is active and not ended
            if (!auction.isActive() || auction.isEnded()) {
                return false;
            }
            
            // Check if bid amount is higher than current bid
            if (amount.compareTo(auction.getCurrentBid()) <= 0) {
                return false;
            }
            
            // Check if bidder is not the seller
            if (auction.getSeller().getId().equals(bidder.getId())) {
                return false;
            }
            
            // Create and save the bid
            Bid bid = new Bid(auction, bidder, amount);
            bidRepository.save(bid);
            
            // Update auction with new bid
            auction.addBid(bid);
            auctionRepository.save(auction);
            
            return true;
        }
        
        return false;
    }
    
    @Transactional
    public void endAuction(Long auctionId) {
        Optional<Auction> optionalAuction = auctionRepository.findById(auctionId);
        
        if (optionalAuction.isPresent()) {
            Auction auction = optionalAuction.get();
            
            // Transfer ownership of card if there was a winning bid
            if (auction.getCurrentBidder() != null) {
                Card card = auction.getCard();
                card.setOwner(auction.getCurrentBidder());
                cardService.saveCard(card);
            }
            
            // Mark auction as inactive
            auction.setActive(false);
            auctionRepository.save(auction);
        }
    }
    
    public void checkAndEndExpiredAuctions() {
        List<Auction> activeAuctions = auctionRepository.findByActive(true);
        
        for (Auction auction : activeAuctions) {
            if (auction.isEnded()) {
                endAuction(auction.getId());
            }
        }
    }
}