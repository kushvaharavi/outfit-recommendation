package com.example.outfitrecommendation.service;

import com.example.outfitrecommendation.exception.RecommendationException;
import com.example.outfitrecommendation.model.InventoryItem;
import com.example.outfitrecommendation.model.Recommendation;
import com.example.outfitrecommendation.model.UserInput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {

    private final InventoryService inventoryService;

    public RecommendationService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public List<Recommendation> generateRecommendations(UserInput userInput) {
        try {
            List<InventoryItem> filteredItems = inventoryService.filterInventory(userInput);
            return createRecommendations(filteredItems);
        } catch (Exception ex) {
            throw new RecommendationException("Failed to generate recommendations: " + ex.getMessage());
        }
    }

    private List<Recommendation> createRecommendations(List<InventoryItem> items) {
        List<Recommendation> recommendations = new ArrayList<>();
        if (!items.isEmpty()) {
            recommendations.add(Recommendation.builder().items(items).build());
        }
        return recommendations;
    }
}



