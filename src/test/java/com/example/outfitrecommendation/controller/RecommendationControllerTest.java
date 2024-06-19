package com.example.outfitrecommendation.controller;

import com.example.outfitrecommendation.model.InventoryItem;
import com.example.outfitrecommendation.model.Recommendation;
import com.example.outfitrecommendation.model.UserInput;
import com.example.outfitrecommendation.model.primitives.Category;
import com.example.outfitrecommendation.model.primitives.EventType;
import com.example.outfitrecommendation.model.primitives.Style;
import com.example.outfitrecommendation.service.RecommendationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecommendationControllerTest {

    @Mock
    private RecommendationService recommendationService;

    @InjectMocks
    private RecommendationController recommendationController;

    private UserInput userInput;
    private List<Recommendation> recommendations;

    @BeforeEach
    public void setup() {
        new UserInput(EventType.WEDDING.name(), "CASUAL", 2000.0);
        recommendations = List.of(
                Recommendation.builder()
                        .items(List.of(
                                new InventoryItem(6, "Casual Jacket", Style.CASUAL, 1200, 10, EventType.BIRTHDAY, Category.JACKET),
                                new InventoryItem(7, "Graphic Tee", Style.CASUAL, 200, 0, EventType.BIRTHDAY, Category.SHIRT),
                                new InventoryItem(8, "Jeans", Style.CASUAL, 600, 15, EventType.BIRTHDAY, Category.TROUSERS),
                                new InventoryItem(9, "Sneakers", Style.CASUAL, 800, 5, EventType.BIRTHDAY, Category.SHOES)
                        ))
                        .build()
        );
    }

    @Test
    void testGetRecommendations_success() {
        when(recommendationService.generateRecommendations(userInput)).thenReturn(recommendations);
        ResponseEntity<List<Recommendation>> response = recommendationController.getRecommendations(userInput);
        assertEquals(recommendations, response.getBody());
    }
}
