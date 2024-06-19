package com.example.outfitrecommendation.controller;

import com.example.outfitrecommendation.model.Recommendation;
import com.example.outfitrecommendation.model.UserInput;
import com.example.outfitrecommendation.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @Operation(summary = "Get outfit recommendations")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the recommendations",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Recommendation.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)})

    @PostMapping
    public ResponseEntity<List<Recommendation>> getRecommendations(
            @Valid @RequestBody UserInput userInput) {
        List<Recommendation> recommendations = recommendationService.generateRecommendations(userInput);
        return ResponseEntity.ok(recommendations);
    }
}
