package com.example.pokevault.controller;

import com.example.pokevault.model.Auction;
import com.example.pokevault.model.Card;
import com.example.pokevault.model.User;
import com.example.pokevault.service.AuctionService;
import com.example.pokevault.service.CardService;
import com.example.pokevault.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/auctions")
public class AuctionController {

    private static final Logger logger = LoggerFactory.getLogger(AuctionController.class);

    @Autowired
    private AuctionService auctionService;
    
    @Autowired
    private CardService cardService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public String getAllAuctions(Model model) {
        // Check for expired auctions
        auctionService.checkAndEndExpiredAuctions();
        
        List<Auction> activeAuctions = auctionService.getActiveAuctions();
        model.addAttribute("auctions", activeAuctions);
        return "auctions";
    }
    
    @GetMapping("/{id}")
    public String getAuctionDetails(@PathVariable Long id, Model model, Authentication authentication) {
        Optional<Auction> optionalAuction = auctionService.getAuctionById(id);
        
        if (optionalAuction.isPresent()) {
            Auction auction = optionalAuction.get();
            model.addAttribute("auction", auction);
            
            // Add current user to model for bid validation
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("currentUser", user);
            
            // Calculate minimum bid amount (current bid + 1)
            BigDecimal minBid = auction.getCurrentBid().add(BigDecimal.ONE);
            model.addAttribute("minBid", minBid);
            
            return "auction-details";
        } else {
            return "redirect:/auctions";
        }
    }
    
    @GetMapping("/my-auctions")
    public String getMyAuctions(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        List<Auction> userAuctions = auctionService.getAuctionsBySeller(user);
        model.addAttribute("auctions", userAuctions);
        return "my-auctions";
    }
    
    @GetMapping("/my-bids")
    public String getMyBids(Authentication authentication, Model model) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            
            List<Auction> userBids = auctionService.getAuctionsByBidder(user);
            model.addAttribute("auctions", userBids);
            model.addAttribute("currentUser", user); // Always add the current user to the model
            
            return "my-bids";
        } catch (Exception e) {
            logger.error("Error retrieving user bids: ", e);
            model.addAttribute("error", "An error occurred while retrieving your bids.");
            return "error";
        }
    }
    
    @GetMapping("/create")
    public String showCreateAuctionForm(Model model, Authentication authentication) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            
            List<Card> userCards = cardService.getCardsByOwner(user);
            model.addAttribute("cards", userCards);
            model.addAttribute("auction", new Auction());
            
            return "create-auction";
        } catch (Exception e) {
            logger.error("Error showing auction creation form: ", e);
            return "redirect:/cards/my-cards?error=true";
        }
    }
    
    @PostMapping("/create")
    public String createAuction(
            @RequestParam(name = "cardId", required = true) String cardIdStr,
            @RequestParam BigDecimal startingPrice,
            @RequestParam int durationInDays,
            Authentication authentication,
            Model model) {
        
        try {
            Long cardId = Long.parseLong(cardIdStr);
            logger.info("Creating auction with cardId: {}, startingPrice: {}, duration: {}", 
                    cardId, startingPrice, durationInDays);
            
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            Optional<Card> optionalCard = cardService.getCardById(cardId);
            
            if (optionalCard.isPresent()) {
                Card card = optionalCard.get();
                
                // Verify that the user owns the card
                if (card.getOwner().getId().equals(user.getId())) {
                    auctionService.createAuction(card, user, startingPrice, durationInDays);
                    return "redirect:/auctions/my-auctions";
                }
            }
            
            // If something went wrong, return to the create form with an error
            return "redirect:/auctions/create?error=true";
        } catch (NumberFormatException e) {
            logger.error("Invalid card ID format: ", e);
            return "redirect:/auctions/create?error=invalidId";
        } catch (Exception e) {
            logger.error("Error creating auction: ", e);
            return "redirect:/auctions/create?error=true";
        }
    }
    
    @PostMapping("/{id}/bid")
    public String placeBid(@PathVariable Long id,
                         @RequestParam BigDecimal bidAmount,
                         Authentication authentication) {
        
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        boolean bidPlaced = auctionService.placeBid(id, user, bidAmount);
        
        if (bidPlaced) {
            return "redirect:/auctions/" + id + "?success";
        } else {
            return "redirect:/auctions/" + id + "?error";
        }
    }
    
    @PostMapping("/{id}/end")
    public String endAuction(@PathVariable Long id, Authentication authentication) {
        Optional<Auction> optionalAuction = auctionService.getAuctionById(id);
        String username = authentication.getName();
        User user = userService.findByUsername(username);
        
        if (optionalAuction.isPresent()) {
            Auction auction = optionalAuction.get();
            
            // Check if current user is the seller
            if (auction.getSeller().getId().equals(user.getId())) {
                auctionService.endAuction(id);
            }
        }
        
        return "redirect:/auctions/my-auctions";
    }
    
    @GetMapping("/search")
    public String searchAuctions(@RequestParam String keyword, Model model) {
        List<Auction> searchResults = auctionService.searchAuctionsByCardName(keyword);
        model.addAttribute("auctions", searchResults);
        return "auctions";
    }
}