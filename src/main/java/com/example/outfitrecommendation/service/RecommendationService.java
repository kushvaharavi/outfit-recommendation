package com.example.outfitrecommendation.service;

import com.example.outfitrecommendation.exception.RecommendationException;
import com.example.outfitrecommendation.exception.InvalidUserInputException;
import com.example.outfitrecommendation.model.InventoryItem;
import com.example.outfitrecommendation.model.Recommendation;
import com.example.outfitrecommendation.model.UserInput;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Service for generating recommendations based on user preferences.
 */
@Service
public class RecommendationService {

    private final InventoryService inventoryService;

    public RecommendationService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public List<Recommendation> generateRecommendations(final UserInput userInput) {
        validateUserInput(userInput);
        try {
            var filteredItems = inventoryService.filterInventory(userInput);
            return createRecommendations(filteredItems);
        } catch (Exception ex) {
            throw new RecommendationException("Failed to generate recommendations: " + ex.getMessage());
        }
    }

    /**
     * Validates the user input.
     *
     * @param userInput the user input to validate
     * @throws InvalidUserInputException if the user input is invalid
     */
    private void validateUserInput(final UserInput userInput) {
        Optional.ofNullable(userInput.eventType())
                .filter(eventType -> !eventType.isBlank())
                .orElseThrow(() -> new InvalidUserInputException("Event type cannot be null or empty."));

        Optional.ofNullable(userInput.style())
                .filter(style -> !style.isBlank())
                .orElseThrow(() -> new InvalidUserInputException("Style cannot be null or empty."));

        if (userInput.budget() <= 0) {
            throw new InvalidUserInputException("Budget must be greater than zero.");
        }
    }

    private List<Recommendation> createRecommendations(final List<InventoryItem> items) {
        var recommendations = new ArrayList<Recommendation>();
        if (!items.isEmpty()) {
            recommendations.add(Recommendation.builder().items(items).build());
        }
        return recommendations;
    }
}



