package com.credit.model;

import java.util.List;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditProfile {
    private double annualIncome;
    private double monthlyDebtPayments;
    private int oldestAccountAge;
    private int ficoScore;
    private int missedPayments;
    private CreditCategory category;
    private List<String> purchaseCategory;
    private List<String> offersList;

   
}