package com.example.outfitrecommendation.service;

import com.example.outfitrecommendation.exception.RecommendationException;
import com.example.outfitrecommendation.model.InventoryItem;
import com.example.outfitrecommendation.model.Recommendation;
import com.example.outfitrecommendation.model.UserInput;
import com.example.outfitrecommendation.model.primitives.Category;
import com.example.outfitrecommendation.model.primitives.EventType;
import com.example.outfitrecommendation.model.primitives.Style;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationServiceTest {

    @Mock
    private InventoryService inventoryService;

    @InjectMocks
    private RecommendationService recommendationService;

    private UserInput userInput;
    private List<InventoryItem> filteredItems;

    @BeforeEach
    public void setup() {
        userInput = new UserInput(EventType.WEDDING.name(), "CASUAL", 2000.0);
        filteredItems = List.of(
                new InventoryItem(6, "Casual Jacket", Style.CASUAL, 1200, 10, EventType.BIRTHDAY, Category.JACKET),
                new InventoryItem(7, "Graphic Tee", Style.CASUAL, 200, 0, EventType.BIRTHDAY, Category.SHIRT),
                new InventoryItem(8, "Jeans", Style.CASUAL, 600, 15, EventType.BIRTHDAY, Category.TROUSERS),
                new InventoryItem(9, "Sneakers", Style.CASUAL, 800, 5, EventType.BIRTHDAY, Category.SHOES)
        );
    }

    @Test
    void testGenerateRecommendations_success() {
        when(inventoryService.filterInventory(userInput)).thenReturn(filteredItems);

        List<Recommendation> recommendations = recommendationService.generateRecommendations(userInput);

        assertNotNull(recommendations);
        assertEquals(1, recommendations.size());
        assertEquals(filteredItems, recommendations.get(0).items());
    }

    @Test
    void testGenerateRecommendations_failure() {
        when(inventoryService.filterInventory(userInput)).thenThrow(new RecommendationException("No items found matching the criteria."));

        assertThrows(RecommendationException.class, () -> recommendationService.generateRecommendations(userInput));
    }
}
