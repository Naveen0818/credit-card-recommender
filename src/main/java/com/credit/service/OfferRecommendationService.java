package com.credit.service;

import com.credit.model.CreditProfile;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class OfferRecommendationService {
    
    private final String[][] cardData = {
        // cardName, offerID, creditScoreRange, purchaseCategory
        {"PLATINUM CARD", "OFF-PLT-2025-07", "720-850", "travel, points, noAnnualFee"},
        {"CASH REWARDS ELITE", "OFF-CRE-2025-12", "690-850", "cashBack, points, allCards"},
        {"TRAVEL MILES UNLIMITED", "OFF-TMU-2025-09", "700-850", "travel, points, noAnnualFee"},
        {"EVERYDAY POINTS", "OFF-EVP-2025-05", "670-850", "points, cashBack, lowIntroRate"},
        {"SECURED BUILDER", "OFF-SCB-2025-08", "580-670", "buildCredit, noAnnualFee"},
        {"US BANK ALTITUDE GO VISA SIGNATURE", "OFF-USGV-2025-13", "690-850", "cashBack, noAnnualFee, points"},
        {"US BANK CASH+ VISA SIGNATURE", "OFF-USCV-2025-14", "680-850", "cashBack, points, allCards"},
        {"US BANK ALTITUDE RESERVE VISA INFINITE", "OFF-USRV-2025-15", "740-850", "travel, points, allCards"}
    };

    public List<String> getPurchaseCategoryOffers(List<String> offersList, List<String> purchaseCategories) {
        return Arrays.stream(cardData)
            .filter(card -> {
                String[] cardCategories = card[3].split(", ");
                return purchaseCategories.stream()
                    .anyMatch(category -> Arrays.asList(cardCategories).contains(category)) &&
                    offersList.contains(card[1]);
            })
            .map(card -> card[1])
            .collect(Collectors.toList());
    }


    public int adjustFicoScore(String offer, CreditProfile profile) {
        //iterate through cardData and adjust fico score based on offer
        for (String[] card : cardData) {
            if (card[1].equals(offer)) {
                //Get ficorange from offer
                String ficoRange = card[2];
                //Get creditData fico score
                int creditDataFicoScore = profile.getFicoScore();
                //adjust the fico score to be the max of the range if it falls within the range
                if (creditDataFicoScore >= Integer.parseInt(ficoRange.split("-")[0]) && creditDataFicoScore <= Integer.parseInt(ficoRange.split("-")[1])) {
                    return Integer.parseInt(ficoRange.split("-")[1]);
                }
            }
        }
        return profile.getFicoScore();
    }
} 