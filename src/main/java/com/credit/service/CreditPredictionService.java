package com.credit.service;

import com.credit.model.CreditProfile;
import com.credit.model.CreditCategory;
import com.credit.model.NeuralNetworkCreditPredictor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreditPredictionService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final NeuralNetworkCreditPredictor neuralNetwork = new NeuralNetworkCreditPredictor();
    private List<CreditProfile> trainingData;
    
    @PostConstruct
    public Map<String, Object> initialize() {
        try {
            // Load training data
            ClassPathResource resource = new ClassPathResource("training-data.json");
            trainingData = objectMapper.readValue(resource.getInputStream(), 
                new TypeReference<List<CreditProfile>>() {});
            
            log.info("Loaded {} training profiles", trainingData.size());
            
            // Train neural network
            neuralNetwork.train(trainingData);
            log.info("Neural network training completed");
            
            // Return training statistics
            Map<String, Object> stats = new HashMap<>();
            stats.put("trainingDataSize", trainingData.size());
            
            // Count profiles per category
            Map<String, Long> categoryCounts = trainingData.stream()
                .collect(HashMap::new,
                    (map, profile) -> map.merge(profile.getCategory().name(), 1L, Long::sum),
                    HashMap::putAll);
            stats.put("categories", categoryCounts);
            
            return stats;
            
        } catch (IOException e) {
            log.error("Error loading training data", e);
            throw new RuntimeException("Failed to initialize credit prediction service", e);
        }
    }
    
    public CreditCategory predictCreditCategory(CreditProfile profile) {
        return neuralNetwork.predict(profile);
    }
    
    public Map<String, Object> getPredictionDetails(CreditProfile profile) {
        CreditCategory category = predictCreditCategory(profile);
        
        Map<String, Object> details = new HashMap<>();
        details.put("category", category);
        details.put("profile", profile);
        
        return details;
    }
} 