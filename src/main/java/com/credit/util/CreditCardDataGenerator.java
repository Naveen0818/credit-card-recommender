package com.credit.util;

import com.credit.model.CreditCard;
import com.credit.model.CreditCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreditCardDataGenerator {
    private static final String[] BANK_BRANDS = {
        "Chase", "American Express", "Citibank", "Bank of America", "Capital One",
        "Discover", "Wells Fargo", "Barclays", "HSBC", "TD Bank"
    };
    
    private static final String[] CARD_TYPES = {
        "Travel", "Cash Back", "Business", "Student", "Secured",
        "Balance Transfer", "Low Interest", "Premium", "Shopping", "Gas"
    };
    
    private static final Random random = new Random();
    
    public static void main(String[] args) {
        try {
            CreditCardDataGenerator generator = new CreditCardDataGenerator();
            List<CreditCard> cards = generator.generateCreditCards();
            
            // Save to JSON file
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("src/main/resources/credit-cards.json"), cards);
            
            System.out.println("Generated " + cards.size() + " credit cards");
            System.out.println("Saved to: src/main/resources/credit-cards.json");
            
        } catch (Exception e) {
            System.err.println("Error generating credit cards: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public List<CreditCard> generateCreditCards() {
        List<CreditCard> cards = new ArrayList<>();
        int cardsPerCategory = 50; // 50 cards per category
        
        for (String brand : BANK_BRANDS) {
            for (CreditCategory category : CreditCategory.values()) {
                for (int i = 0; i < cardsPerCategory; i++) {
                    CreditCard card = generateCreditCard(brand, category, i);
                    cards.add(card);
                }
            }
        }
        
        return cards;
    }
    
    private CreditCard generateCreditCard(String brand, CreditCategory category, int index) {
        CreditCard card = new CreditCard();
        card.setId(brand.toLowerCase() + "-" + category.name().toLowerCase() + "-" + index);
        card.setName(brand + " " + category + " Card");
        card.setBrand(brand);
        card.setCategory(category);
        
        // Set card attributes based on category
        switch (category) {
            case EXCELLENT:
                card.setAnnualFee(random.nextDouble(100, 600));
                card.setInterestRate(random.nextDouble(12.99, 15.99));
                card.setRewardsRate(random.nextDouble(2.0, 5.0));
                card.setCreditLimit(random.nextDouble(10000, 25000));
                card.setMinIncome(80000);
                card.setMinCreditHistory(5);
                card.setMinPaymentHistory(90);
                break;
            case GOOD:
                card.setAnnualFee(random.nextDouble(50, 200));
                card.setInterestRate(random.nextDouble(15.99, 19.99));
                card.setRewardsRate(random.nextDouble(1.5, 3.0));
                card.setCreditLimit(random.nextDouble(5000, 15000));
                card.setMinIncome(50000);
                card.setMinCreditHistory(3);
                card.setMinPaymentHistory(80);
                break;
            case FAIR:
                card.setAnnualFee(random.nextDouble(25, 100));
                card.setInterestRate(random.nextDouble(19.99, 24.99));
                card.setRewardsRate(random.nextDouble(1.0, 2.0));
                card.setCreditLimit(random.nextDouble(2000, 8000));
                card.setMinIncome(30000);
                card.setMinCreditHistory(2);
                card.setMinPaymentHistory(70);
                break;
            case POOR:
                card.setAnnualFee(random.nextDouble(0, 50));
                card.setInterestRate(random.nextDouble(24.99, 29.99));
                card.setRewardsRate(random.nextDouble(0.5, 1.5));
                card.setCreditLimit(random.nextDouble(500, 2500));
                card.setMinIncome(20000);
                card.setMinCreditHistory(1);
                card.setMinPaymentHistory(60);
                break;
        }
        
        // Set features based on category
        card.setFeatures(generateFeatures(category));
        
        // Set eligibility criteria
        card.setEligibilityCriteria(generateEligibilityCriteria(category));
        
        return card;
    }
    
    private List<String> generateFeatures(CreditCategory category) {
        List<String> features = new ArrayList<>();
        
        switch (category) {
            case EXCELLENT:
                features.add("Premium Travel Insurance");
                features.add("Airport Lounge Access");
                features.add("Concierge Service");
                features.add("Extended Warranty");
                features.add("Purchase Protection");
                features.add("No Foreign Transaction Fees");
                features.add("Elite Status Benefits");
                break;
            case GOOD:
                features.add("Travel Insurance");
                features.add("Extended Warranty");
                features.add("Purchase Protection");
                features.add("No Foreign Transaction Fees");
                features.add("Travel Assistance");
                break;
            case FAIR:
                features.add("Basic Travel Insurance");
                features.add("Extended Warranty");
                features.add("Purchase Protection");
                features.add("Travel Assistance");
                break;
            case POOR:
                features.add("Basic Purchase Protection");
                features.add("Travel Assistance");
                features.add("Online Account Management");
                break;
        }
        
        return features;
    }
    
    private List<String> generateEligibilityCriteria(CreditCategory category) {
        List<String> criteria = new ArrayList<>();
        
        switch (category) {
            case EXCELLENT:
                criteria.add("Excellent credit score (720+)");
                criteria.add("High income ($80,000+)");
                criteria.add("Long credit history (5+ years)");
                criteria.add("Low debt-to-income ratio");
                criteria.add("No recent late payments");
                break;
            case GOOD:
                criteria.add("Good credit score (670-719)");
                criteria.add("Stable income ($50,000+)");
                criteria.add("Established credit history (3+ years)");
                criteria.add("Moderate debt-to-income ratio");
                criteria.add("Few recent late payments");
                break;
            case FAIR:
                criteria.add("Fair credit score (580-669)");
                criteria.add("Regular income ($30,000+)");
                criteria.add("Some credit history (2+ years)");
                criteria.add("Higher debt-to-income ratio");
                criteria.add("Some recent late payments");
                break;
            case POOR:
                criteria.add("Poor credit score (below 580)");
                criteria.add("Basic income ($20,000+)");
                criteria.add("Limited credit history (1+ year)");
                criteria.add("High debt-to-income ratio");
                criteria.add("Multiple recent late payments");
                break;
        }
        
        return criteria;
    }
} 