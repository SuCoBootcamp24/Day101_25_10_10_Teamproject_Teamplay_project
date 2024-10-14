package de.supercode.backend.dtos.fight;

import java.util.List;

public record AnalyticDTO(
        boolean hasWon,
        List<AnalyticPartDTO> parts
) {
}


/*
Rookie 1 -10
Normal 11-30
Veteran 31-50
Legendär 51-80

 */