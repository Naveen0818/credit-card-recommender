package com.credit.service;

import com.credit.model.CreditCategory;
import com.credit.model.CreditProfile;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.util.*;

@Service
public class CreditPredictionService {
    private static final Logger logger = LoggerFactory.getLogger(CreditPredictionService.class);
    private List<CreditProfile> trainingData;
    private Map<CreditCategory, SimpleRegression> regressionModels;
    private static final double[] FEATURE_WEIGHTS = {
        0.20,  // annualIncome
        0.20,  // monthlyDebtPayments
        0.15,  // oldestAccountAge
        0.15,  // activeCreditCards
        0.10,  // totalLoans
        0.10,  // creditCardUsage
        0.10   // onTimePayments
    };

    public CreditPredictionService() {
        logger.info("Initializing CreditPredictionService...");
        this.trainingData = new ArrayList<>();
        this.regressionModels = new EnumMap<>(CreditCategory.class);
        
        logger.info("Loading training data from JSON file...");
        loadTrainingData();
        logger.info("Successfully loaded {} training profiles", trainingData.size());
        
        logger.info("Starting model training...");
        trainModels();
        logger.info("Model training completed successfully");
    }

    private void loadTrainingData() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ClassPathResource resource = new ClassPathResource("training-data.json");
            this.trainingData = mapper.readValue(resource.getInputStream(), 
                new TypeReference<List<CreditProfile>>() {});
            
            // Log category distribution
            Map<CreditCategory, Long> categoryCounts = trainingData.stream()
                .collect(HashMap::new,
                    (map, profile) -> map.merge(profile.getPredictedCategory(), 1L, Long::sum),
                    HashMap::putAll);
            
            logger.info("Training data distribution: {}", categoryCounts);
            
        } catch (IOException e) {
            logger.error("Failed to load training data from JSON file", e);
            throw new RuntimeException("Failed to load training data from JSON file", e);
        }
    }

    private void trainModels() {
        for (CreditCategory category : CreditCategory.values()) {
            logger.debug("Training model for category: {}", category);
            SimpleRegression regression = new SimpleRegression();
            
            // Train model for each category using all features combined
            for (CreditProfile profile : trainingData) {
                double[] features = extractFeatures(profile);
                double combinedFeature = 0.0;
                for (int i = 0; i < features.length; i++) {
                    combinedFeature += features[i] * FEATURE_WEIGHTS[i];
                }
                double target = profile.getPredictedCategory() == category ? 1.0 : 0.0;
                regression.addData(combinedFeature, target);
            }
            
            regressionModels.put(category, regression);
            logger.debug("Completed training for category: {}", category);
        }
    }

    private double[] extractFeatures(CreditProfile profile) {
        // Calculate derived metrics from user-friendly fields
        double debtToIncomeRatio = profile.getMonthlyDebtPayments() * 12 / profile.getAnnualIncome();
        double normalizedIncome = Math.min(profile.getAnnualIncome() / 200000.0, 1.0); // Cap at $200k
        double normalizedCreditHistory = Math.min(profile.getOldestAccountAge() / 20.0, 1.0); // Cap at 20 years
        double normalizedCreditCards = Math.min(profile.getActiveCreditCards() / 5.0, 1.0); // Cap at 5 cards
        double normalizedLoans = Math.min(profile.getTotalLoans() / 5.0, 1.0); // Cap at 5 loans
        double normalizedPaymentHistory = profile.getOnTimePayments() / 12.0; // Convert to 0-1 scale
        
        return new double[]{
            normalizedIncome,
            debtToIncomeRatio,
            normalizedCreditHistory,
            normalizedCreditCards,
            normalizedLoans,
            profile.getCreditCardUsage(),
            normalizedPaymentHistory
        };
    }

    public CreditCategory predictCategory(CreditProfile profile) {
        Map<CreditCategory, Double> predictions = new EnumMap<>(CreditCategory.class);
        double[] features = extractFeatures(profile);
        
        // Calculate combined feature value
        double combinedFeature = 0.0;
        for (int i = 0; i < features.length; i++) {
            combinedFeature += features[i] * FEATURE_WEIGHTS[i];
        }
        
        // Get predictions for each category
        for (Map.Entry<CreditCategory, SimpleRegression> entry : regressionModels.entrySet()) {
            double prediction = entry.getValue().predict(combinedFeature);
            predictions.put(entry.getKey(), prediction);
        }
        
        // Apply thresholds based on feature values
        double onTimePaymentRate = profile.getOnTimePayments() / 12.0;
        double debtToIncomeRatio = profile.getMonthlyDebtPayments() * 12 / profile.getAnnualIncome();
        
        if (onTimePaymentRate < 0.7) { // Less than 70% on-time payments
            return CreditCategory.POOR;
        } else if (onTimePaymentRate >= 0.9 && // 90% or more on-time payments
                  debtToIncomeRatio <= 0.3 && // Low debt-to-income ratio
                  profile.getCreditCardUsage() <= 0.3) { // Low credit card usage
            return CreditCategory.EXCELLENT;
        } else if (onTimePaymentRate >= 0.8 && // 80% or more on-time payments
                  debtToIncomeRatio <= 0.4 && // Moderate debt-to-income ratio
                  profile.getCreditCardUsage() <= 0.4) { // Moderate credit card usage
            return CreditCategory.GOOD;
        } else if (onTimePaymentRate >= 0.7 && // 70% or more on-time payments
                  debtToIncomeRatio <= 0.6 && // Higher debt-to-income ratio
                  profile.getCreditCardUsage() <= 0.6) { // Higher credit card usage
            return CreditCategory.FAIR;
        }
        
        // If no threshold matches, use the model predictions
        return predictions.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(CreditCategory.POOR);
    }

    public Map<String, Object> trainModel(List<CreditProfile> newTrainingData) {
        // Update training data
        this.trainingData = new ArrayList<>(newTrainingData);
        
        // Retrain models
        trainModels();
        
        // Return training statistics
        Map<String, Object> stats = new HashMap<>();
        stats.put("trainingDataSize", trainingData.size());
        stats.put("categories", Arrays.stream(CreditCategory.values())
                .collect(HashMap::new,
                        (map, category) -> map.put(category.name(),
                                trainingData.stream()
                                        .filter(p -> p.getPredictedCategory() == category)
                                        .count()),
                        HashMap::putAll));
        
        return stats;
    }
} 