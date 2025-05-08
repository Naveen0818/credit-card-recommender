package com.credit.model;

import java.util.List;

import lombok.Data;

@Data
public class CreditProfile {
    // Annual income before taxes
    private double income;

    private int ficoScore;
    
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

    private List<String> purchaseCategory;

    private List<String> offersList;

    public double getOnTimePayments() {
        return paymentHistory;
    }

    public double getMonthlyDebtPayments() {
        return income * debtToIncomeRatio / 12;
    }

    public double getAnnualIncome() {
        return income;
    }

    public double getCreditCardUsage() {
        return creditUtilization;
    }

    public CreditCategory getPredictedCategory() {
        return category;
    }

    public int getOldestAccountAge() {
        return creditHistoryLength;
    }

    public int getTotalLoans() {
        return numberOfLoans;
    }

    public int getActiveCreditCards() {
        return numberOfCreditCards;
    }

    public void setTotalLoans(int numberOfLoans) {
        this.numberOfLoans = numberOfLoans;
    }

    public void setAnnualIncome(double income) {
        this.income = income;
    }

    public void setMonthlyDebtPayments(double monthlyDebtPayments) {
        this.debtToIncomeRatio = monthlyDebtPayments * 12 / this.income;
    }

    public void setOldestAccountAge(int oldestAccountAge) {
        this.creditHistoryLength = oldestAccountAge;
    }

    public void setActiveCreditCards(int activeCreditCards) {
        this.numberOfCreditCards = activeCreditCards;
    }

    public void setCreditCardUsage(double creditCardUsage) {
        this.creditUtilization = creditCardUsage;
    }

    public void setOnTimePayments(int onTimePayments) {
        this.paymentHistory = onTimePayments;
    }

    public void setPredictedCategory(CreditCategory category) {
        this.category = category;
    }

    public void setPurchaseCategory(List<String> purchaseCategory) {
        this.purchaseCategory = purchaseCategory;
    }

    public void setOffersList(List<String> offersList) {
        this.offersList = offersList;
    }

    public List<String> getPurchaseCategory() {
        return purchaseCategory;
    }

    public List<String> getOffersList() {
        return offersList;
    }

    public int getFicoScore() {
        return ficoScore;
    }

    public void setFicoScore(int ficoScore) {
        this.ficoScore = ficoScore;
    }
}