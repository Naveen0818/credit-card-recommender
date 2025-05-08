package com.credit.model;

import lombok.Data;
import java.util.List;

@Data
public class CreditCard {
    private String id;
    private String name;
    private String brand;
    private CreditCategory category;
    private double annualFee;
    private double interestRate;
    private double rewardsRate;
    private double creditLimit;
    private List<String> features;
    private List<String> eligibilityCriteria;
} 