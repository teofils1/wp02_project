package com.example.pokevault.controller;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.pokevault.model.Auction;
import com.example.pokevault.service.AuctionService;

@Controller
public class HomeController {
    
    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private AuctionService auctionService;

    @GetMapping("/")
    public String index() {
        return "redirect:/home";
    }

    @GetMapping("/home")
    public String home(Model model) {
        try {
            // Check for expired auctions
            auctionService.checkAndEndExpiredAuctions();
            
            // Get recent active auctions for the dashboard
            List<Auction> activeAuctions = auctionService.getActiveAuctions();
            model.addAttribute("auctions", activeAuctions);
        } catch (Exception e) {
            logger.error("Error loading home page: ", e);
            model.addAttribute("auctions", Collections.emptyList());
            model.addAttribute("error", "An error occurred while loading auctions.");
        }
        
        return "home";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}
