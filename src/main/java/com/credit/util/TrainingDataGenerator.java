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
        profile.setActiveCreditCards(random.nextInt(3, 6)); // 3-5 cards
        profile.setTotalLoans(random.nextInt(1, 3)); // 1-2 loans
        profile.setCreditCardUsage(random.nextDouble(0.1, 0.3)); // 10-30% usage
        profile.setOnTimePayments(random.nextInt(11, 13)); // 11-12 on-time payments
        profile.setPredictedCategory(CreditCategory.EXCELLENT);
        return profile;
    }

    private static CreditProfile generateGoodProfile() {
        CreditProfile profile = new CreditProfile();
        profile.setAnnualIncome(random.nextDouble(80000, 150000)); // $80k-$150k
        profile.setMonthlyDebtPayments(random.nextDouble(2000, 4000)); // $2k-$4k monthly payments
        profile.setOldestAccountAge(random.nextInt(8, 15)); // 8-15 years
        profile.setActiveCreditCards(random.nextInt(2, 4)); // 2-3 cards
        profile.setTotalLoans(random.nextInt(1, 3)); // 1-2 loans
        profile.setCreditCardUsage(random.nextDouble(0.2, 0.4)); // 20-40% usage
        profile.setOnTimePayments(random.nextInt(10, 12)); // 10-11 on-time payments
        profile.setPredictedCategory(CreditCategory.GOOD);
        return profile;
    }

    private static CreditProfile generateFairProfile() {
        CreditProfile profile = new CreditProfile();
        profile.setAnnualIncome(random.nextDouble(50000, 80000)); // $50k-$80k
        profile.setMonthlyDebtPayments(random.nextDouble(3000, 5000)); // $3k-$5k monthly payments
        profile.setOldestAccountAge(random.nextInt(4, 8)); // 4-8 years
        profile.setActiveCreditCards(random.nextInt(1, 3)); // 1-2 cards
        profile.setTotalLoans(random.nextInt(2, 4)); // 2-3 loans
        profile.setCreditCardUsage(random.nextDouble(0.4, 0.6)); // 40-60% usage
        profile.setOnTimePayments(random.nextInt(8, 10)); // 8-9 on-time payments
        profile.setPredictedCategory(CreditCategory.FAIR);
        return profile;
    }

    private static CreditProfile generatePoorProfile() {
        CreditProfile profile = new CreditProfile();
        profile.setAnnualIncome(random.nextDouble(25000, 50000)); // $25k-$50k
        profile.setMonthlyDebtPayments(random.nextDouble(4000, 6000)); // $4k-$6k monthly payments
        profile.setOldestAccountAge(random.nextInt(1, 4)); // 1-4 years
        profile.setActiveCreditCards(random.nextInt(1, 2)); // 1 card
        profile.setTotalLoans(random.nextInt(3, 5)); // 3-4 loans
        profile.setCreditCardUsage(random.nextDouble(0.6, 0.9)); // 60-90% usage
        profile.setOnTimePayments(random.nextInt(6, 8)); // 6-7 on-time payments
        profile.setPredictedCategory(CreditCategory.POOR);
        return profile;
    }
} 