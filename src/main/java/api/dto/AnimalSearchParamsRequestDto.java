package api.dto;

public record AnimalSearchParamsRequestDto(
        String name,
        String type,
        String sex,
        Long categoryId
) {
}
