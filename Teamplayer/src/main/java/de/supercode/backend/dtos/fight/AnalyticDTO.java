package de.supercode.backend.dtos.fight;

import java.util.List;

public record AnalyticDTO(
        boolean hasWon,
        List<AnalyticPartDTO> parts
) {
}
