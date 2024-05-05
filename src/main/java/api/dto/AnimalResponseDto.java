package api.dto;

import api.model.enums.Sex;
import api.model.enums.Type;

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
