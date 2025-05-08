package com.credit.controller;

import com.credit.model.CreditCard;
import com.credit.model.CreditProfile;
import com.credit.model.CreditCategory;
import com.credit.service.CreditPredictionService;
import com.credit.service.CreditCardRecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Credit Card Recommendation API", description = "API for credit card recommendations based on credit profile")
public class CreditCardController {

    private static final Logger log = LoggerFactory.getLogger(CreditCardController.class);

    private final CreditPredictionService predictionService;
    private final CreditCardRecommendationService recommendationService;

    @Autowired
    public CreditCardController(CreditPredictionService predictionService,
                              CreditCardRecommendationService recommendationService) {
        this.predictionService = predictionService;
        this.recommendationService = recommendationService;
    }

    @PostMapping("/train")
    @Operation(
        summary = "Train the credit prediction model",
        description = "Retrains the credit prediction model with new data"
    )
    @ApiResponse(responseCode = "200", description = "Successfully trained the model")
    public ResponseEntity<Map<String, Object>> trainModel(@RequestBody List<CreditProfile> trainingData) {
        log.info("Received request to train model with {} profiles", trainingData.size());
        Map<String, Object> trainingStats = predictionService.initialize();
        return ResponseEntity.ok(trainingStats);
    }

    @PostMapping("/predict")
    @Operation(
        summary = "Predict credit category",
        description = "Predicts credit category based on the provided credit profile"
    )
    @ApiResponse(responseCode = "200", description = "Successfully predicted credit category")
    public ResponseEntity<Map<String, Object>> predictCreditCategory(@RequestBody CreditProfile profile) {
        log.info("Received credit profile for prediction: {}", profile);
        Map<String, Object> prediction = predictionService.getPredictionDetails(profile);
        log.info("Prediction result: {}", prediction);
        return ResponseEntity.ok(prediction);
    }

    @GetMapping("/recommendations")
    @Operation(
        summary = "Get personalized credit card recommendations",
        description = "Predicts credit category and returns matching credit card recommendations based on the provided profile"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved personalized credit card recommendations")
    public ResponseEntity<List<CreditCard>> getRecommendations(@RequestBody CreditProfile profile) {
        log.info("Received request for credit card recommendations: {}", profile);
        List<CreditCard> recommendations = recommendationService.getRecommendations(profile);
        log.info("Generated {} recommendations", recommendations.size());
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/cards")
    @Operation(
        summary = "Get all credit cards",
        description = "Returns a list of all available credit cards"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all credit cards")
    public ResponseEntity<List<CreditCard>> getAllCards() {
        List<CreditCard> cards = recommendationService.getAllCards();
        log.info("Retrieved {} credit cards", cards.size());
        return ResponseEntity.ok(cards);
    }

    @GetMapping("/cards/{category}")
    @Operation(
        summary = "Get credit cards by category",
        description = "Returns a list of credit cards for the specified category"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved credit cards by category")
    public ResponseEntity<List<CreditCard>> getCardsByCategory(@PathVariable CreditCategory category) {
        CreditProfile profile = new CreditProfile();
        profile.setCategory(category);
        List<CreditCard> cards = recommendationService.getRecommendations(profile);
        return ResponseEntity.ok(cards);
    }
} 