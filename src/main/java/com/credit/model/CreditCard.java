package com.credit.model;

import lombok.Data;

@Data
public class CreditCard {
    private String name;
    private String description;
    private double annualFee;
    private double interestRate;
    private int creditScoreRequirement;
    private CreditCategory minimumCategory;
    private String benefits;
} 