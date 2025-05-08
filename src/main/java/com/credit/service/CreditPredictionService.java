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
        0.25,  // normalizedIncome
        0.20,  // debtToIncomeRatio
        0.20,  // normalizedCreditHistory
        0.20,  // normalizedFicoScore
        0.15   // normalizedMissedPayments
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
                    (map, profile) -> map.merge(profile.getCategory(), 1L, Long::sum),
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
                double target = profile.getCategory() == category ? 1.0 : 0.0;
                regression.addData(combinedFeature, target);
            }
            
            regressionModels.put(category, regression);
            logger.debug("Completed training for category: {}", category);
        }
    }

    private double[] extractFeatures(CreditProfile profile) {
        // Calculate derived metrics from user-friendly fields
        double normalizedIncome = Math.min(profile.getAnnualIncome() / 200000.0, 1.0); // Cap at $200k
        double debtToIncomeRatio = profile.getMonthlyDebtPayments() * 12 / profile.getAnnualIncome();
        double normalizedCreditHistory = Math.min(profile.getOldestAccountAge() / 20.0, 1.0); // Cap at 20 years
        
        // Convert missed payments to a negative score (higher missed payments = lower score)
        double missedPayments = 12 - profile.getMissedPayments();
        double normalizedMissedPayments = 1.0 - (missedPayments / 12.0); // Convert to 0-1 scale where 0 is worst
        
        // Normalize FICO score to 0-1 scale (300-850 range)
        double normalizedFicoScore = (profile.getFicoScore() - 300.0) / 550.0; // 550 is the range (850-300)
        
        return new double[]{
            normalizedIncome,
            debtToIncomeRatio,
            normalizedCreditHistory,
            normalizedFicoScore,
            normalizedMissedPayments
        };
    }

    public CreditCategory predictCategory(CreditProfile profile) {
        Map<CreditCategory, Double> predictions = new EnumMap<>(CreditCategory.class);
        double[] features = extractFeatures(profile);
        
        // Calculate combined feature value with negative weights for missed payments
        double combinedFeature = 0.0;
        for (int i = 0; i < features.length; i++) {
            // For missed payments (index 4), multiply by -1 to make high values negative
            if (i == 4) {
                combinedFeature += (1.0 - features[i]) * FEATURE_WEIGHTS[i];
            } else {
                combinedFeature += features[i] * FEATURE_WEIGHTS[i];
            }
        }
        
        // Get predictions for each category
        for (Map.Entry<CreditCategory, SimpleRegression> entry : regressionModels.entrySet()) {
            double prediction = entry.getValue().predict(combinedFeature);
            predictions.put(entry.getKey(), prediction);
        }
        
        // Apply thresholds based on feature values
        double missedPayments = profile.getMissedPayments();
        double missedPaymentRate = missedPayments / 12.0;
        
        if (missedPaymentRate > 0.3) { // High missed payments
            return CreditCategory.POOR;
        } else if (missedPaymentRate <= 0.1) { // Excellent payment history
            return CreditCategory.EXCELLENT;
        } else if (missedPaymentRate <= 0.2) { // Good payment history
            return CreditCategory.GOOD;
        } else if (missedPaymentRate <= 0.3) { // Fair payment history
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
                                        .filter(p -> p.getCategory() == category)
                                        .count()),
                        HashMap::putAll));
        
        return stats;
    }
} 