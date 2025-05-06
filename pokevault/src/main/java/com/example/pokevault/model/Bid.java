package com.example.pokevault.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "bids")
public class Bid {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;
    
    @ManyToOne
    @JoinColumn(name = "bidder_id")
    private User bidder;
    
    @Column(nullable = false)
    private BigDecimal amount;
    
    @Column(nullable = false)
    private LocalDateTime bidTime;
    
    // Constructors
    public Bid() {
    }
    
    public Bid(Auction auction, User bidder, BigDecimal amount) {
        this.auction = auction;
        this.bidder = bidder;
        this.amount = amount;
        this.bidTime = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Auction getAuction() {
        return auction;
    }
    
    public void setAuction(Auction auction) {
        this.auction = auction;
    }
    
    public User getBidder() {
        return bidder;
    }
    
    public void setBidder(User bidder) {
        this.bidder = bidder;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public LocalDateTime getBidTime() {
        return bidTime;
    }
    
    public void setBidTime(LocalDateTime bidTime) {
        this.bidTime = bidTime;
    }
}
