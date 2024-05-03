package api.service.parser;

import api.dto.AnimalCreateRequestDto;
import api.mapper.AnimalMapper;
import api.model.Animal;
import api.model.Sex;
import api.model.Type;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class FileParserImpl implements FileParser {
    private final AnimalMapper animalMapper;

    @Override
    public List<Animal> parse(List<AnimalCreateRequestDto> requestDtos) {
        requestDtos
                .stream()
                .map(dto -> {
                    Animal animal = animalMapper.toModel(dto);
                    try {
                        Type type = Type.fromString(dto.getType());
                        Sex sex = Sex.fromString(dto.getSex());
                        animal.setType(type)
                                .setSex(sex)
                                .setCategoryId()
                    }
                })
    }
}
