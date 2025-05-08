package com.credit.model;

import lombok.Data;

@Data
public class CreditProfile {
    // Annual income before taxes
    private double annualIncome;
    
    // Monthly debt payments (mortgage, car loans, student loans, etc.)
    private double monthlyDebtPayments;
    
    // Age of oldest credit account in years
    private int oldestAccountAge;
    
    // Number of active credit cards
    private int activeCreditCards;
    
    // Total number of loans (mortgage, car, student, personal)
    private int totalLoans;
    
    // Total credit card balances / Total credit limits (as a decimal, e.g., 0.3 for 30%)
    private double creditCardUsage;
    
    // Number of on-time payments in the last 12 months (out of 12)
    private int onTimePayments;
    
    // Internal field for model prediction
    private CreditCategory predictedCategory;
} 