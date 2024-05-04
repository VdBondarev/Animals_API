package api.dto;

import api.model.Sex;
import api.model.Type;

public record AnimalResponseDto(
        Long id,
        String name,
        Type type,
        Sex sex,
        int weight,
        int cost,
        Long categoryId
) {
}
