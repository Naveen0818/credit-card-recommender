package com.credit.model;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.datasets.iterator.impl.ListDataSetIterator;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@Component
public class NeuralNetworkCreditPredictor {
    private MultiLayerNetwork network;
    private static final int NUM_FEATURES = 7;
    private static final int NUM_CATEGORIES = 4;
    private static final int BATCH_SIZE = 32;
    private static final int EPOCHS = 100;
    private static final double LEARNING_RATE = 0.001;

    public void train(List<CreditProfile> profiles) {
        // Prepare training data
        List<DataSet> trainingData = new ArrayList<>();
        
        for (CreditProfile profile : profiles) {
            // Extract features
            double[] features = extractFeatures(profile);
            
            // Create one-hot encoded label
            double[] label = new double[NUM_CATEGORIES];
            label[profile.getCategory().ordinal()] = 1.0;
            
            // Create DataSet
            INDArray featuresArray = Nd4j.create(features).reshape(1, NUM_FEATURES);
            INDArray labelsArray = Nd4j.create(label).reshape(1, NUM_CATEGORIES);
            trainingData.add(new DataSet(featuresArray, labelsArray));
        }
        
        // Create DataSetIterator
        DataSetIterator iterator = new ListDataSetIterator<>(trainingData, BATCH_SIZE);
        
        // Configure neural network
        MultiLayerConfiguration config = new NeuralNetConfiguration.Builder()
            .seed(123)
            .weightInit(WeightInit.XAVIER)
            .updater(new Adam(LEARNING_RATE))
            .list()
            .layer(0, new DenseLayer.Builder()
                .nIn(NUM_FEATURES)
                .nOut(64)
                .activation(Activation.RELU)
                .build())
            .layer(1, new DenseLayer.Builder()
                .nIn(64)
                .nOut(32)
                .activation(Activation.RELU)
                .build())
            .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.NEGATIVELOGLIKELIHOOD)
                .nIn(32)
                .nOut(NUM_CATEGORIES)
                .activation(Activation.SOFTMAX)
                .build())
            .build();
        
        // Initialize and train network
        network = new MultiLayerNetwork(config);
        network.init();
        
        // Train for specified number of epochs
        for (int i = 0; i < EPOCHS; i++) {
            iterator.reset();
            network.fit(iterator);
        }
    }
    
    public CreditCategory predict(CreditProfile profile) {
        // Extract features
        double[] features = extractFeatures(profile);
        INDArray featuresArray = Nd4j.create(features).reshape(1, NUM_FEATURES);
        
        // Get prediction
        INDArray output = network.output(featuresArray, false);
        
        // Get category with highest probability
        int maxIndex = Nd4j.argMax(output, 1).getInt(0);
        return CreditCategory.values()[maxIndex];
    }
    
    private double[] extractFeatures(CreditProfile profile) {
        return new double[] {
            normalizeIncome(profile.getIncome()),
            normalizeDebtToIncome(profile.getDebtToIncomeRatio()),
            normalizeCreditHistory(profile.getCreditHistoryLength()),
            normalizeCreditCards(profile.getNumberOfCreditCards()),
            normalizeLoans(profile.getNumberOfLoans()),
            normalizeCreditUtilization(profile.getCreditUtilization()),
            normalizePaymentHistory(profile.getPaymentHistory())
        };
    }
    
    private double normalizeIncome(double income) {
        return (income - 20000) / (200000 - 20000);
    }
    
    private double normalizeDebtToIncome(double ratio) {
        return (ratio - 0.1) / (0.8 - 0.1);
    }
    
    private double normalizeCreditHistory(double years) {
        return (years - 1) / (20 - 1);
    }
    
    private double normalizeCreditCards(int cards) {
        return (cards - 1) / (10 - 1);
    }
    
    private double normalizeLoans(int loans) {
        return (loans - 0) / (5 - 0);
    }
    
    private double normalizeCreditUtilization(double utilization) {
        return (utilization - 0.1) / (0.9 - 0.1);
    }
    
    private double normalizePaymentHistory(double history) {
        return (history - 60) / (100 - 60);
    }
} 