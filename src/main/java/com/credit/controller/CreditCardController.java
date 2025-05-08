package com.credit.controller;

import com.credit.model.CreditCard;
import com.credit.model.CreditCategory;
import com.credit.model.CreditProfile;
import com.credit.service.CreditCardRecommendationService;
import com.credit.service.CreditPredictionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/credit")
@Tag(name = "Credit Card Recommendation API", description = "API for credit card recommendations based on credit profile")
public class CreditCardController {

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
    public ResponseEntity<Map<String, Object>> trainModel(
            @Parameter(description = "List of credit profiles for training") 
            @RequestBody List<CreditProfile> trainingData) {
        Map<String, Object> result = predictionService.trainModel(trainingData);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/predict")
    @Operation(
        summary = "Predict credit category",
        description = "Predicts the credit category based on the provided credit profile"
    )
    @ApiResponse(responseCode = "200", description = "Successfully predicted credit category")
    public ResponseEntity<CreditCategory> predictCreditCategory(@RequestBody CreditProfile profile) {
        CreditCategory predictedCategory = predictionService.predictCategory(profile);
        return ResponseEntity.ok(predictedCategory);
    }

    @PostMapping("/recommend")
    @Operation(
        summary = "Get personalized credit card recommendations",
        description = "Predicts credit category and returns matching credit card recommendations based on the provided profile"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved personalized credit card recommendations")
    public ResponseEntity<List<CreditCard>> recommendCreditCards(@RequestBody CreditProfile profile) {
        CreditCategory predictedCategory = predictionService.predictCategory(profile);
        List<CreditCard> recommendations = recommendationService.getRecommendedCards(profile, predictedCategory);
        return ResponseEntity.ok(recommendations);
    }

    @GetMapping("/cards")
    @Operation(
        summary = "Get all credit cards",
        description = "Returns a list of all available credit cards"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved all credit cards")
    public ResponseEntity<List<CreditCard>> getAllCards() {
        return ResponseEntity.ok(recommendationService.getAllCards());
    }

    @GetMapping("/cards/{category}")
    @Operation(
        summary = "Get credit cards by category",
        description = "Returns a list of credit cards for the specified category"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved credit cards by category")
    public ResponseEntity<List<CreditCard>> getCardsByCategory(@PathVariable CreditCategory category) {
        List<CreditCard> cards = recommendationService.getRecommendedCards(null, category);
        return ResponseEntity.ok(cards);
    }
} 