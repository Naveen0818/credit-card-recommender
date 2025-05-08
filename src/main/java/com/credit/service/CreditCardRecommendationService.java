package com.credit.service;

import com.credit.model.CreditCard;
import com.credit.model.CreditCategory;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class CreditCardRecommendationService {
    private final Map<CreditCategory, List<CreditCard>> creditCardsByCategory;

    public CreditCardRecommendationService() {
        this.creditCardsByCategory = new EnumMap<>(CreditCategory.class);
        initializeCreditCards();
    }

    private void initializeCreditCards() {
        // Excellent category cards
        List<CreditCard> excellentCards = new ArrayList<>();
        excellentCards.add(createCard(
            "Platinum Elite",
            "Premium travel and lifestyle benefits",
            550.0,
            15.99,
            750,
            CreditCategory.EXCELLENT,
            "Airport lounge access, travel insurance, concierge service"
        ));
        excellentCards.add(createCard(
            "Signature Rewards",
            "High cashback and premium rewards",
            395.0,
            16.99,
            740,
            CreditCategory.EXCELLENT,
            "3% cashback on all purchases, extended warranty"
        ));
        creditCardsByCategory.put(CreditCategory.EXCELLENT, excellentCards);

        // Good category cards
        List<CreditCard> goodCards = new ArrayList<>();
        goodCards.add(createCard(
            "Gold Rewards",
            "Balanced rewards and benefits",
            95.0,
            18.99,
            700,
            CreditCategory.GOOD,
            "2% cashback on groceries, 1% on all purchases"
        ));
        goodCards.add(createCard(
            "Travel Plus",
            "Travel-focused rewards card",
            120.0,
            19.99,
            690,
            CreditCategory.GOOD,
            "Travel points, basic travel insurance"
        ));
        creditCardsByCategory.put(CreditCategory.GOOD, goodCards);

        // Fair category cards
        List<CreditCard> fairCards = new ArrayList<>();
        fairCards.add(createCard(
            "Essential Card",
            "Basic credit building card",
            35.0,
            22.99,
            650,
            CreditCategory.FAIR,
            "1% cashback on all purchases"
        ));
        fairCards.add(createCard(
            "Secure Plus",
            "Secured credit card with rewards",
            0.0,
            23.99,
            640,
            CreditCategory.FAIR,
            "Credit building, basic rewards"
        ));
        creditCardsByCategory.put(CreditCategory.FAIR, fairCards);

        // Poor category cards
        List<CreditCard> poorCards = new ArrayList<>();
        poorCards.add(createCard(
            "Fresh Start",
            "Credit rebuilding card",
            0.0,
            25.99,
            580,
            CreditCategory.POOR,
            "Credit monitoring, basic protection"
        ));
        poorCards.add(createCard(
            "Secure Basic",
            "Secured credit card",
            0.0,
            26.99,
            550,
            CreditCategory.POOR,
            "Credit building, no rewards"
        ));
        creditCardsByCategory.put(CreditCategory.POOR, poorCards);
    }

    private CreditCard createCard(String name, String description, double annualFee,
                                double interestRate, int creditScoreRequirement,
                                CreditCategory minimumCategory, String benefits) {
        CreditCard card = new CreditCard();
        card.setName(name);
        card.setDescription(description);
        card.setAnnualFee(annualFee);
        card.setInterestRate(interestRate);
        card.setCreditScoreRequirement(creditScoreRequirement);
        card.setMinimumCategory(minimumCategory);
        card.setBenefits(benefits);
        return card;
    }

    public List<CreditCard> getRecommendations(CreditCategory category) {
        return creditCardsByCategory.getOrDefault(category, Collections.emptyList());
    }
} 