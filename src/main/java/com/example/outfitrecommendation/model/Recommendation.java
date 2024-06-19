package com.example.outfitrecommendation.model;


import lombok.Builder;

import java.util.List;
@Builder
public record Recommendation(List<InventoryItem> items) {
}
