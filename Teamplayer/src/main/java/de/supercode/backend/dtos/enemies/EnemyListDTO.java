package de.supercode.backend.dtos.enemies;

public record EnemyListDTO(
        String teamName,

        int teamRating,

        String ownerName,

        int totalRating
) {
}
