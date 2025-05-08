package com.credit.service;

import com.credit.model.CreditCard;
import com.credit.model.CreditCategory;
import com.credit.model.CreditProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreditCardRecommendationService {
    private List<CreditCard> creditCards;

    public CreditCardRecommendationService() {
        loadCreditCards();
    }

    private void loadCreditCards() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("credit-cards.json");
            this.creditCards = mapper.readValue(resource.getInputStream(), 
                new TypeReference<List<CreditCard>>() {});
        } catch (IOException e) {
            this.creditCards = new ArrayList<>();
            initializeDefaultCreditCards();
        }
    }

    private void initializeDefaultCreditCards() {
        // This method will be replaced by the JSON file
        CreditCard card = new CreditCard();
        card.setId("default-card");
        card.setName("Default Credit Card");
        card.setBrand("Default Bank");
        card.setCategory(CreditCategory.GOOD);
        card.setAnnualFee(95.0);
        card.setInterestRate(15.99);
        card.setRewardsRate(1.5);
        card.setCreditLimit(5000.0);
        card.setFeatures(List.of("Basic Rewards", "Online Banking"));
        card.setEligibilityCriteria(List.of("Good Credit Score", "Stable Income"));
        creditCards.add(card);
    }

    public List<CreditCard> getRecommendedCards(CreditProfile profile, CreditCategory predictedCategory) {
        return creditCards.stream()
            .filter(card -> card.getCategory() == predictedCategory)
            .collect(Collectors.toList());
    }

    public List<CreditCard> getAllCards() {
        return new ArrayList<>(creditCards);
    }
} 