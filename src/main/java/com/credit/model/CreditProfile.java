package com.credit.model;

import lombok.Data;

@Data
public class CreditProfile {
    // Annual income before taxes
    private double income;
    
    // Monthly debt payments (mortgage, car loans, student loans, etc.)
    private double debtToIncomeRatio;
    
    // Age of oldest credit account in years
    private int creditHistoryLength;
    
    // Number of active credit cards
    private int numberOfCreditCards;
    
    // Total number of loans (mortgage, car, student, personal)
    private int numberOfLoans;
    
    // Total credit card balances / Total credit limits (as a decimal, e.g., 0.3 for 30%)
    private double creditUtilization;
    
    // Number of on-time payments in the last 12 months (out of 12)
    private double paymentHistory;
    
    // Internal field for model prediction
    private CreditCategory category;
} 