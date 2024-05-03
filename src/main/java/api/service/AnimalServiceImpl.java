package api.service;

import api.dto.AnimalResponseDto;
import api.mapper.AnimalMapper;
import api.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

    @Override
    public List<AnimalResponseDto> upload(MultipartFile file) {
        String contentType = file.getContentType();
        return new ArrayList<>();
    }
}
