package com.credit.util;

import com.credit.model.CreditProfile;
import com.credit.model.CreditCategory;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrainingDataGenerator {
    private static final Random random = new Random();
    private static final int PROFILES_PER_CATEGORY = 1000;
    
    public static void main(String[] args) {
        try {
            List<CreditProfile> profiles = generateTrainingData();
            
            // Save to JSON file
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(new File("src/main/resources/training-data.json"), profiles);
            
            System.out.println("Generated " + profiles.size() + " credit profiles");
            System.out.println("Saved to: src/main/resources/training-data.json");
            
        } catch (Exception e) {
            System.err.println("Error generating training data: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static List<CreditProfile> generateTrainingData() {
        List<CreditProfile> profiles = new ArrayList<>();
        
        // Generate profiles for each category
        for (int i = 0; i < PROFILES_PER_CATEGORY; i++) {
            profiles.add(generateExcellentProfile());
            profiles.add(generateGoodProfile());
            profiles.add(generateFairProfile());
            profiles.add(generatePoorProfile());
        }
        
        return profiles;
    }
    
    private static CreditProfile generateExcellentProfile() {
        CreditProfile profile = new CreditProfile();
        profile.setIncome(random.nextDouble(100000, 200000));
        profile.setDebtToIncomeRatio(random.nextDouble(0.1, 0.3));
        profile.setCreditHistoryLength(random.nextInt(10, 20));
        profile.setNumberOfCreditCards(random.nextInt(2, 5));
        profile.setNumberOfLoans(random.nextInt(1, 3));
        profile.setCreditUtilization(random.nextDouble(0.1, 0.3));
        profile.setPaymentHistory(random.nextDouble(95, 100));
        profile.setCategory(CreditCategory.EXCELLENT);
        return profile;
    }
    
    private static CreditProfile generateGoodProfile() {
        CreditProfile profile = new CreditProfile();
        profile.setIncome(random.nextDouble(60000, 100000));
        profile.setDebtToIncomeRatio(random.nextDouble(0.3, 0.4));
        profile.setCreditHistoryLength(random.nextInt(5, 10));
        profile.setNumberOfCreditCards(random.nextInt(2, 4));
        profile.setNumberOfLoans(random.nextInt(1, 3));
        profile.setCreditUtilization(random.nextDouble(0.3, 0.5));
        profile.setPaymentHistory(random.nextDouble(85, 95));
        profile.setCategory(CreditCategory.GOOD);
        return profile;
    }
    
    private static CreditProfile generateFairProfile() {
        CreditProfile profile = new CreditProfile();
        profile.setIncome(random.nextDouble(30000, 60000));
        profile.setDebtToIncomeRatio(random.nextDouble(0.4, 0.6));
        profile.setCreditHistoryLength(random.nextInt(2, 5));
        profile.setNumberOfCreditCards(random.nextInt(1, 3));
        profile.setNumberOfLoans(random.nextInt(1, 4));
        profile.setCreditUtilization(random.nextDouble(0.5, 0.7));
        profile.setPaymentHistory(random.nextDouble(70, 85));
        profile.setCategory(CreditCategory.FAIR);
        return profile;
    }
    
    private static CreditProfile generatePoorProfile() {
        CreditProfile profile = new CreditProfile();
        profile.setIncome(random.nextDouble(20000, 30000));
        profile.setDebtToIncomeRatio(random.nextDouble(0.6, 0.8));
        profile.setCreditHistoryLength(random.nextInt(0, 2));
        profile.setNumberOfCreditCards(random.nextInt(0, 2));
        profile.setNumberOfLoans(random.nextInt(2, 5));
        profile.setCreditUtilization(random.nextDouble(0.7, 0.9));
        profile.setPaymentHistory(random.nextDouble(60, 70));
        profile.setCategory(CreditCategory.POOR);
        return profile;
    }
} 