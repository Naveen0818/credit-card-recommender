package com.credit.service;

import com.credit.model.CreditCard;
import com.credit.model.CreditProfile;
import com.credit.model.CreditCategory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Service
public class CreditCardRecommendationService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final CreditPredictionService predictionService;
    private List<CreditCard> creditCards;
    
    @Autowired
    public CreditCardRecommendationService(CreditPredictionService predictionService) {
        this.predictionService = predictionService;
    }
    
    @PostConstruct
    public void initialize() {
        try {
            // Load credit card data
            ClassPathResource resource = new ClassPathResource("credit-cards.json");
            creditCards = objectMapper.readValue(resource.getInputStream(), 
                new TypeReference<List<CreditCard>>() {});
            
            log.info("Loaded {} credit cards", creditCards.size());
            
        } catch (IOException e) {
            log.error("Error loading credit card data", e);
            throw new RuntimeException("Failed to initialize credit card recommendation service", e);
        }
    }
    
    public List<CreditCard> getRecommendations(CreditProfile profile) {
        CreditCategory category = predictionService.predictCreditCategory(profile);
        return getRecommendedCards(profile, category);
    }
    
    private List<CreditCard> getRecommendedCards(CreditProfile profile, CreditCategory category) {
        // Filter cards by category and eligibility criteria
        return creditCards.stream()
            .filter(card -> card.getCategory() == category)
            .filter(card -> isEligible(profile, card))
            .sorted(Comparator.comparingDouble(CreditCard::getRewardsRate).reversed())
            .limit(5)
            .toList();
    }
    
    private boolean isEligible(CreditProfile profile, CreditCard card) {
        // Check if profile meets minimum requirements
        return profile.getIncome() >= card.getMinIncome() &&
               profile.getCreditHistoryLength() >= card.getMinCreditHistory() &&
               profile.getPaymentHistory() >= card.getMinPaymentHistory();
    }
    
    public List<CreditCard> getAllCards() {
        return new ArrayList<>(creditCards);
    }
} 