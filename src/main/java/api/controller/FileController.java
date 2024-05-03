package api.controller;

import api.dto.AnimalResponseDto;
import api.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Files controller",
        description = """
        An endpoint for uploading a file with entities for persisting into database
        """)
@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class FileController {
    private final AnimalService animalService;

    @Operation(summary = "Upload endpoint",
            description = """
                    Upload a csv or xml file with animals to persist into database
                    """)
    @PostMapping("/upload")
    public List<AnimalResponseDto> upload(@RequestParam MultipartFile file) {
        return animalService.upload(file);
    }
}
