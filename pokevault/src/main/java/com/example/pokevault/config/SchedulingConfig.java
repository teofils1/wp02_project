package com.example.pokevault.config;

import com.example.pokevault.service.AuctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class SchedulingConfig {

    @Autowired
    private AuctionService auctionService;
    
    // Run every hour to check for expired auctions
    @Scheduled(fixedRate = 3600000)
    public void checkExpiredAuctions() {
        auctionService.checkAndEndExpiredAuctions();
    }
}
