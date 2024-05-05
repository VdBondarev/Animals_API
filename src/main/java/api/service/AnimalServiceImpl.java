package api.service;

import static api.constant.AnimalConstantsFolder.CATEGORY_ID_FIELD;
import static api.constant.AnimalConstantsFolder.COST_FIELD;
import static api.constant.AnimalConstantsFolder.NAME_FIELD;
import static api.constant.AnimalConstantsFolder.SEX_FIELD;
import static api.constant.AnimalConstantsFolder.TYPE_FIELD;
import static api.constant.AnimalConstantsFolder.WEIGHT_FIELD;

import api.dto.AnimalCreateRequestDto;
import api.dto.AnimalResponseDto;
import api.dto.AnimalSearchParamsRequestDto;
import api.mapper.AnimalMapper;
import api.model.Animal;
import api.model.enums.Sex;
import api.model.enums.Type;
import api.repository.AnimalRepository;
import api.service.parser.FileParser;
import api.service.reader.FileReader;
import api.service.strategy.AnimalReaderStrategy;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class AnimalServiceImpl implements AnimalService {
    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;
    private final AnimalReaderStrategy readerStrategy;
    private final FileParser fileParser;

    @Override
    @Transactional
    public List<AnimalResponseDto> upload(MultipartFile file) throws FileUploadException {
        if (file.isEmpty()) {
            throw new FileUploadException("""
                    File is empty
                    Upload another one
                    """);
        }
        String contentType = file.getContentType();
        FileReader reader = readerStrategy.getFileReader(contentType);
        List<AnimalCreateRequestDto> requestDtos = reader.readFromFile(file);
        List<Animal> parsedAnimals = fileParser.parse(requestDtos);
        animalRepository.saveAll(parsedAnimals);
        return parsedAnimals
                .stream()
                .map(animalMapper::toResponseDto)
                .toList();
    }

    @Override
    public List<AnimalResponseDto> search(
            AnimalSearchParamsRequestDto requestDto, Pageable pageable
    ) {
        if (isNotValid(requestDto)) {
            throw new IllegalArgumentException("""
                    Searching should be done by at least 1 param
                    """);
        }
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths(COST_FIELD, WEIGHT_FIELD)
                .withMatcher(CATEGORY_ID_FIELD, ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(SEX_FIELD, ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(TYPE_FIELD, ExampleMatcher.GenericPropertyMatchers.exact())
                .withMatcher(
                        NAME_FIELD, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()
                );

        Example<Animal> example = Example.of(
                fromSearchParams(requestDto),
                matcher
        );
        return animalRepository.findAll(example, pageable)
                .stream()
                .map(animalMapper::toResponseDto)
                .toList();
    }

    private boolean isNotValid(AnimalSearchParamsRequestDto requestDto) {
        return requestDto == null || (
                requestDto.categoryId() == null
                && (requestDto.name() == null || requestDto.name().isEmpty())
                && (requestDto.sex() == null || requestDto.sex().isEmpty())
                && (requestDto.type() == null || requestDto.type().isEmpty())
            );
    }

    private Animal fromSearchParams(AnimalSearchParamsRequestDto requestDto) {
        return new Animal()
                .setCategoryId(requestDto.categoryId())
                .setSex(requestDto.sex() == null
                        ? null
                        : Sex.fromString(requestDto.sex())
                )
                .setType(requestDto.type() == null
                        ? null
                        : Type.fromString(requestDto.type())
                )
                .setName(requestDto.name());
    }
}
