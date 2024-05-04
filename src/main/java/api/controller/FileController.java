package api.controller;

import api.dto.AnimalResponseDto;
import api.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
                    Upload a file with animal entities for saving them to a database.
                    If some animals do not have all required params, they will not be saved.
                    If non-valid params for animals are passed, they will not be saved as well
                    Allowed for admins only
                    """)
    @PostMapping("/upload")
    @ResponseStatus(HttpStatus.CREATED)
    public List<AnimalResponseDto> upload(
            @RequestParam MultipartFile file
    ) throws FileUploadException {
        return animalService.upload(file);
    }
}
