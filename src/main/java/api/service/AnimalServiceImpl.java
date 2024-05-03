package api.service;

import api.dto.AnimalResponseDto;
import api.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository animalRepository;

    @Override
    public List<AnimalResponseDto> upload(MultipartFile file) {
        return null;
    }
}
