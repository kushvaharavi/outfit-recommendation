package com.example.outfitrecommendation.service;

import com.example.outfitrecommendation.exception.InventoryException;
import com.example.outfitrecommendation.model.InventoryItem;
import com.example.outfitrecommendation.model.UserInput;
import com.example.outfitrecommendation.model.primitives.Category;
import com.example.outfitrecommendation.model.primitives.EventType;
import com.example.outfitrecommendation.model.primitives.Style;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final List<InventoryItem> inventory = new ArrayList<>();

    public InventoryService() {
        // Adding various inventory items with different event types, styles, and quantities
        addItem(new InventoryItem(1, "Parka Jacket", Style.WESTERN, 2500, 10, EventType.CHRISTMAS, Category.JACKET));
        addItem(new InventoryItem(2, "Flannel Shirt", Style.WESTERN, 400, 20, EventType.CHRISTMAS, Category.SHIRT));
        addItem(new InventoryItem(3, "Ripped Trousers", Style.WESTERN, 800, 0, EventType.CHRISTMAS, Category.TROUSERS));
        addItem(new InventoryItem(4, "Cowboy Boots", Style.WESTERN, 1500, 5, EventType.CHRISTMAS, Category.SHOES));
        addItem(new InventoryItem(5, "Santa Hat", Style.WESTERN, 100, 10, EventType.CHRISTMAS, Category.ACCESSORY));
        addItem(new InventoryItem(6, "Casual Jacket", Style.CASUAL, 1200, 10, EventType.BIRTHDAY, Category.JACKET));
        addItem(new InventoryItem(7, "Graphic Tee", Style.CASUAL, 200, 0, EventType.BIRTHDAY, Category.SHIRT));
        addItem(new InventoryItem(8, "Jeans", Style.CASUAL, 600, 15, EventType.BIRTHDAY, Category.TROUSERS));
        addItem(new InventoryItem(9, "Sneakers", Style.CASUAL, 800, 5, EventType.BIRTHDAY, Category.SHOES));
        addItem(new InventoryItem(10, "Festive Socks", Style.WESTERN, 50, 20, EventType.CHRISTMAS, Category.ACCESSORY));
        addItem(new InventoryItem(11, "Tuxedo", Style.FORMAL, 3000, 10, EventType.WEDDING, Category.JACKET));
        addItem(new InventoryItem(12, "Dress Shirt", Style.FORMAL, 500, 20, EventType.WEDDING, Category.SHIRT));
        addItem(new InventoryItem(13, "Formal Trousers", Style.FORMAL, 1000, 15, EventType.WEDDING, Category.TROUSERS));
        addItem(new InventoryItem(14, "Dress Shoes", Style.FORMAL, 1200, 5, EventType.WEDDING, Category.SHOES));
        addItem(new InventoryItem(15, "Party Hat", Style.CASUAL, 80, 0, EventType.BIRTHDAY, Category.ACCESSORY));
        addItem(new InventoryItem(16, "Leather Jacket", Style.WESTERN, 2500, 10, EventType.ANNIVERSARY, Category.JACKET));
        addItem(new InventoryItem(17, "Denim Shirt", Style.WESTERN, 300, 20, EventType.ANNIVERSARY, Category.SHIRT));
        addItem(new InventoryItem(18, "Ripped Jeans", Style.WESTERN, 600, 15, EventType.ANNIVERSARY, Category.TROUSERS));
        addItem(new InventoryItem(19, "Ankle Boots", Style.WESTERN, 1200, 5, EventType.ANNIVERSARY, Category.SHOES));
        addItem(new InventoryItem(20, "Birthday Crown", Style.CASUAL, 150, 20, EventType.BIRTHDAY, Category.ACCESSORY));
        addItem(new InventoryItem(21, "Sports Jacket", Style.SPORTY, 1500, 10, EventType.NEW_YEAR, Category.JACKET));
        addItem(new InventoryItem(22, "Running Shoes", Style.SPORTY, 1000, 5, EventType.NEW_YEAR, Category.SHOES));
        addItem(new InventoryItem(23, "Elegant Dress", Style.ELEGANT, 2000, 8, EventType.OFFICE_PARTY, Category.DRESS));
        addItem(new InventoryItem(24, "Office Blazer", Style.ELEGANT, 1800, 12, EventType.OFFICE_PARTY, Category.JACKET));
        addItem(new InventoryItem(25, "Tie", Style.FORMAL, 200, 15, EventType.WEDDING, Category.ACCESSORY));
    }

    private void addItem(InventoryItem item) {
        inventory.add(item);
    }

    public List<InventoryItem> filterInventory(UserInput userInput) {
        List<InventoryItem> filteredItems = inventory.stream()
                .filter(item -> item.eventType().name().equalsIgnoreCase(userInput.eventType())
                        && item.style().name().equalsIgnoreCase(userInput.style())
                        && item.price() <= userInput.budget()
                        && item.stockQuantity() > 0)
                .toList();

        if (filteredItems.isEmpty()) {
            throw new InventoryException("No items found matching the criteria.");
        }

        return recommendFullOutfit(filteredItems, userInput.budget());
    }

    List<InventoryItem> recommendFullOutfit(List<InventoryItem> items, double budget) {
        Map<Category, List<InventoryItem>> itemsByCategory = items.stream()
                .collect(Collectors.groupingBy(InventoryItem::category));

        List<InventoryItem> fullOutfit = new ArrayList<>();
        double totalCost = 0;

        for (Category category : Category.values()) {
            double finalTotalCost = totalCost;
            Optional<InventoryItem> selectedItem = itemsByCategory.getOrDefault(category, List.of()).stream()
                    .filter(item -> finalTotalCost + item.price() <= budget)
                    .findFirst();

            if (selectedItem.isPresent()) {
                fullOutfit.add(selectedItem.get());
                totalCost += selectedItem.get().price();
            }
        }

        if (fullOutfit.isEmpty() || totalCost > budget) {
            throw new InventoryException("Unable to recommend a full outfit within the given budget.");
        }

        return fullOutfit;
    }
}
