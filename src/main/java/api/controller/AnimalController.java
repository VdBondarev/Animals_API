package api.controller;

import api.dto.AnimalResponseDto;
import api.dto.AnimalSearchParamsRequestDto;
import api.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Animals controller",
        description = """
                Endpoints for managing animals
                """)
@RestController
@RequiredArgsConstructor
@RequestMapping("/animals")
public class AnimalController {
    private final AnimalService animalService;

    @Operation(summary = "Search animals by params")
    @GetMapping("/search")
    public List<AnimalResponseDto> search(
            @RequestBody AnimalSearchParamsRequestDto requestDto, Pageable pageable
    ) {
        return animalService.search(requestDto, pageable);
    }
}
