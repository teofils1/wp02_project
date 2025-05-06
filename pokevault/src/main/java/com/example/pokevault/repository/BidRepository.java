package com.example.pokevault.repository;

import com.example.pokevault.model.Auction;
import com.example.pokevault.model.Bid;
import com.example.pokevault.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByAuction(Auction auction);
    List<Bid> findByBidder(User bidder);
    List<Bid> findByAuctionOrderByAmountDesc(Auction auction);
}
