package com.credit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.credit.model.CreditProfile;
import java.io.File;
import java.util.List;

public class TrainingDataFileGenerator {
    public static void main(String[] args) {
        try {
            // Generate training data
            List<CreditProfile> profiles = TrainingDataGenerator.generateTrainingData();
            
            // Create ObjectMapper for JSON serialization
            ObjectMapper mapper = new ObjectMapper();
            
            // Write to file
            File outputFile = new File("src/main/resources/training-data.json");
            mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, profiles);
            
            System.out.println("Training data generated successfully!");
            System.out.println("Total profiles: " + profiles.size());
            System.out.println("File saved to: " + outputFile.getAbsolutePath());
            
        } catch (Exception e) {
            System.err.println("Error generating training data: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 