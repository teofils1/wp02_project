package com.example.pokevault.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "auctions")
public class Auction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    @JoinColumn(name = "card_id")
    private Card card;
    
    @ManyToOne
    @JoinColumn(name = "seller_id")
    private User seller;
    
    @Column(nullable = false)
    private BigDecimal startingPrice;
    
    @Column(nullable = true)
    private BigDecimal currentBid;
    
    @ManyToOne
    @JoinColumn(name = "current_bidder_id")
    private User currentBidder;
    
    @Column(nullable = false)
    private LocalDateTime startTime;
    
    @Column(nullable = false)
    private LocalDateTime endTime;
    
    @Column(nullable = false)
    private boolean active;
    
    @OneToMany(mappedBy = "auction", cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();
    
    // Constructors
    public Auction() {
    }
    
    public Auction(Card card, User seller, BigDecimal startingPrice, 
                LocalDateTime startTime, LocalDateTime endTime) {
        this.card = card;
        this.seller = seller;
        this.startingPrice = startingPrice;
        this.currentBid = startingPrice;
        this.startTime = startTime;
        this.endTime = endTime;
        this.active = true;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Card getCard() {
        return card;
    }
    
    public void setCard(Card card) {
        this.card = card;
    }
    
    public User getSeller() {
        return seller;
    }
    
    public void setSeller(User seller) {
        this.seller = seller;
    }
    
    public BigDecimal getStartingPrice() {
        return startingPrice;
    }
    
    public void setStartingPrice(BigDecimal startingPrice) {
        this.startingPrice = startingPrice;
    }
    
    public BigDecimal getCurrentBid() {
        return currentBid;
    }
    
    public void setCurrentBid(BigDecimal currentBid) {
        this.currentBid = currentBid;
    }
    
    public User getCurrentBidder() {
        return currentBidder;
    }
    
    public void setCurrentBidder(User currentBidder) {
        this.currentBidder = currentBidder;
    }
    
    public LocalDateTime getStartTime() {
        return startTime;
    }
    
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
    
    public List<Bid> getBids() {
        return bids;
    }
    
    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }
    
    public void addBid(Bid bid) {
        this.bids.add(bid);
        this.currentBid = bid.getAmount();
        this.currentBidder = bid.getBidder();
    }
    
    public boolean isEnded() {
        return LocalDateTime.now().isAfter(endTime);
    }
}