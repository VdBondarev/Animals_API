package api.service;

import api.dto.AnimalResponseDto;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface AnimalService {

    List<AnimalResponseDto> upload(MultipartFile file);
}
