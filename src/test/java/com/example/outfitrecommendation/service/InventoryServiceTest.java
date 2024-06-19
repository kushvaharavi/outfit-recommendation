package com.example.outfitrecommendation.service;

import com.example.outfitrecommendation.exception.InventoryException;
import com.example.outfitrecommendation.model.InventoryItem;
import com.example.outfitrecommendation.model.UserInput;
import com.example.outfitrecommendation.model.primitives.EventType;
import com.example.outfitrecommendation.model.primitives.Style;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InventoryServiceTest {

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void filterInventory_whenValidInput_thenReturnFilteredItems() {
        UserInput userInput = new UserInput(EventType.CHRISTMAS.name(), Style.WESTERN.name(), 3000);
        List<InventoryItem> filteredItems = inventoryService.filterInventory(userInput);
        assertNotNull(filteredItems);
        assertFalse(filteredItems.isEmpty());
        assertTrue(filteredItems.stream().allMatch(item -> item.eventType() == EventType.CHRISTMAS));
        assertTrue(filteredItems.stream().allMatch(item -> item.style() == Style.WESTERN));
        assertTrue(filteredItems.stream().allMatch(item -> item.price() <= userInput.budget()));
        assertTrue(filteredItems.stream().allMatch(item -> item.stockQuantity() > 0));
    }

    @Test
    void filterInventory_whenNoMatchingItems_thenThrowInventoryException() {
        UserInput userInput = new UserInput(EventType.CHRISTMAS.name(), Style.SPORTY.name(), 3000);
        InventoryException exception = assertThrows(InventoryException.class, () -> inventoryService.filterInventory(userInput));
        assertEquals("No items found matching the criteria.", exception.getMessage());
    }

    @Test
    void recommendFullOutfit_whenEmptyItems_thenThrowInventoryException() {
        List<InventoryItem> items = List.of();
        InventoryException exception = assertThrows(InventoryException.class, () -> inventoryService.recommendFullOutfit(items, 5000));
        assertEquals("Unable to recommend a full outfit within the given budget.", exception.getMessage());
    }
}
