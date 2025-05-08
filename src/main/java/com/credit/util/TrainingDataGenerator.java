package com.credit.util;

import com.credit.model.CreditCategory;
import com.credit.model.CreditProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TrainingDataGenerator {
    private static final Random random = new Random();
    private static final int PROFILES_PER_CATEGORY = 1000;

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
        profile.setAnnualIncome(random.nextDouble(150000, 300000)); // $150k-$300k
        profile.setMonthlyDebtPayments(random.nextDouble(1000, 3000)); // $1k-$3k monthly payments
        profile.setOldestAccountAge(random.nextInt(15, 30)); // 15-30 years
        profile.setFicoScore(random.nextInt(750, 850)); // 750-850 FICO score
        profile.setMissedPayments(random.nextInt(0, 2)); // 0-1 missed payments
        profile.setCategory(CreditCategory.EXCELLENT);
        return profile;
    }

    private static CreditProfile generateGoodProfile() {
        CreditProfile profile = new CreditProfile();
        profile.setAnnualIncome(random.nextDouble(80000, 150000)); // $80k-$150k
        profile.setMonthlyDebtPayments(random.nextDouble(2000, 4000)); // $2k-$4k monthly payments
        profile.setOldestAccountAge(random.nextInt(8, 15)); // 8-15 years
        profile.setFicoScore(random.nextInt(700, 750)); // 700-750 FICO score
        profile.setMissedPayments(random.nextInt(1, 3)); // 1-2 missed payments
        profile.setCategory(CreditCategory.GOOD);
        return profile;
    }

    private static CreditProfile generateFairProfile() {
        CreditProfile profile = new CreditProfile();
        profile.setAnnualIncome(random.nextDouble(50000, 80000)); // $50k-$80k
        profile.setMonthlyDebtPayments(random.nextDouble(3000, 5000)); // $3k-$5k monthly payments
        profile.setOldestAccountAge(random.nextInt(4, 8)); // 4-8 years
        profile.setFicoScore(random.nextInt(650, 700)); // 650-700 FICO score
        profile.setMissedPayments(random.nextInt(2, 4)); // 2-3 missed payments
        profile.setCategory(CreditCategory.FAIR);
        return profile;
    }

    private static CreditProfile generatePoorProfile() {
        CreditProfile profile = new CreditProfile();
        profile.setAnnualIncome(random.nextDouble(25000, 50000)); // $25k-$50k
        profile.setMonthlyDebtPayments(random.nextDouble(4000, 6000)); // $4k-$6k monthly payments
        profile.setOldestAccountAge(random.nextInt(1, 4)); // 1-4 years
        profile.setFicoScore(random.nextInt(300, 650)); // 300-650 FICO score
        profile.setMissedPayments(random.nextInt(3, 12)); // 3-11 missed payments
        profile.setCategory(CreditCategory.POOR);
        return profile;
    }
} 