package com.credit.util;

import com.credit.model.CreditCard;
import com.credit.model.CreditCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CreditCardDataGenerator {
    private static final String[] BRANDS = {
        "Chase", "American Express", "Capital One", "Citi", "Bank of America",
        "Discover", "Wells Fargo", "Barclays", "HSBC", "U.S. Bank"
    };

    private static final String[] CARD_TYPES = {
        "Travel Rewards", "Cash Back", "Business", "Student", "Secured",
        "Balance Transfer", "Low Interest", "Premium", "Shopping", "Gas"
    };

    public static void main(String[] args) {
        try {
            List<CreditCard> cards = generateCreditCards();
            saveToJson(cards);
            System.out.println("Credit card data generated successfully!");
            System.out.println("Total cards: " + cards.size());
            System.out.println("File saved to: " + new File("src/main/resources/credit-cards.json").getAbsolutePath());
        } catch (Exception e) {
            System.err.println("Error generating credit card data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<CreditCard> generateCreditCards() {
        List<CreditCard> cards = new ArrayList<>();
        
        // Generate cards for each brand and category
        for (String brand : BRANDS) {
            for (CreditCategory category : CreditCategory.values()) {
                cards.addAll(generateCardsForBrandAndCategory(brand, category));
            }
        }
        
        return cards;
    }

    private static List<CreditCard> generateCardsForBrandAndCategory(String brand, CreditCategory category) {
        List<CreditCard> cards = new ArrayList<>();
        int cardsPerCategory = 5; // Number of cards per brand and category

        for (int i = 0; i < cardsPerCategory; i++) {
            CreditCard card = new CreditCard();
            card.setId(brand.toLowerCase().replace(" ", "-") + "-" + 
                      category.name().toLowerCase() + "-" + (i + 1));
            card.setName(generateCardName(brand, category, i));
            card.setBrand(brand);
            card.setCategory(category);
            card.setAnnualFee(generateAnnualFee(category));
            card.setInterestRate(generateInterestRate(category));
            card.setRewardsRate(generateRewardsRate(category));
            card.setCreditLimit(generateCreditLimit(category));
            card.setFeatures(generateFeatures(category));
            card.setEligibilityCriteria(generateEligibilityCriteria(category));
            cards.add(card);
        }

        return cards;
    }

    private static String generateCardName(String brand, CreditCategory category, int index) {
        String type = CARD_TYPES[index % CARD_TYPES.length];
        return brand + " " + type + " Card";
    }

    private static double generateAnnualFee(CreditCategory category) {
        return switch (category) {
            case EXCELLENT -> Math.random() * 500 + 100; // $100-$600
            case GOOD -> Math.random() * 200 + 50;      // $50-$250
            case FAIR -> Math.random() * 100 + 25;      // $25-$125
            case POOR -> Math.random() * 50 + 0;        // $0-$50
        };
    }

    private static double generateInterestRate(CreditCategory category) {
        return switch (category) {
            case EXCELLENT -> Math.random() * 5 + 12;   // 12-17%
            case GOOD -> Math.random() * 8 + 15;        // 15-23%
            case FAIR -> Math.random() * 10 + 20;       // 20-30%
            case POOR -> Math.random() * 15 + 25;       // 25-40%
        };
    }

    private static double generateRewardsRate(CreditCategory category) {
        return switch (category) {
            case EXCELLENT -> Math.random() * 3 + 2;    // 2-5%
            case GOOD -> Math.random() * 2 + 1;         // 1-3%
            case FAIR -> Math.random() * 1 + 0.5;       // 0.5-1.5%
            case POOR -> Math.random() * 0.5 + 0;       // 0-0.5%
        };
    }

    private static double generateCreditLimit(CreditCategory category) {
        return switch (category) {
            case EXCELLENT -> Math.random() * 15000 + 10000;  // $10k-$25k
            case GOOD -> Math.random() * 8000 + 5000;         // $5k-$13k
            case FAIR -> Math.random() * 5000 + 2000;         // $2k-$7k
            case POOR -> Math.random() * 2000 + 500;          // $500-$2.5k
        };
    }

    private static List<String> generateFeatures(CreditCategory category) {
        List<String> features = new ArrayList<>();
        
        // Common features
        features.add("Online Account Management");
        features.add("Mobile App Access");
        
        // Category-specific features
        switch (category) {
            case EXCELLENT -> {
                features.add("Travel Insurance");
                features.add("Airport Lounge Access");
                features.add("Concierge Service");
                features.add("Extended Warranty");
                features.add("Purchase Protection");
            }
            case GOOD -> {
                features.add("Travel Insurance");
                features.add("Extended Warranty");
                features.add("Price Protection");
            }
            case FAIR -> {
                features.add("Basic Purchase Protection");
                features.add("Fraud Protection");
            }
            case POOR -> {
                features.add("Basic Fraud Protection");
                features.add("Credit Building Tools");
            }
        }
        
        return features;
    }

    private static List<String> generateEligibilityCriteria(CreditCategory category) {
        List<String> criteria = new ArrayList<>();
        
        switch (category) {
            case EXCELLENT -> {
                criteria.add("Credit Score: 750+");
                criteria.add("Annual Income: $100,000+");
                criteria.add("Low Debt-to-Income Ratio");
                criteria.add("Clean Credit History");
            }
            case GOOD -> {
                criteria.add("Credit Score: 700-749");
                criteria.add("Annual Income: $50,000+");
                criteria.add("Moderate Debt-to-Income Ratio");
            }
            case FAIR -> {
                criteria.add("Credit Score: 650-699");
                criteria.add("Annual Income: $30,000+");
                criteria.add("Stable Employment");
            }
            case POOR -> {
                criteria.add("Credit Score: 580-649");
                criteria.add("Proof of Income");
                criteria.add("No Recent Bankruptcies");
            }
        }
        
        return criteria;
    }

    private static void saveToJson(List<CreditCard> cards) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter()
              .writeValue(new File("src/main/resources/credit-cards.json"), cards);
    }
} 