package com.example.outfitrecommendation.model;


import com.example.outfitrecommendation.model.primitives.Category;
import com.example.outfitrecommendation.model.primitives.EventType;
import com.example.outfitrecommendation.model.primitives.Style;

public record InventoryItem(int id, String name, Style style, double price, int stockQuantity, EventType eventType,
                            Category category) {
}
