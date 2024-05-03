package api.service.parser;

import api.dto.AnimalCreateRequestDto;
import api.mapper.AnimalMapper;
import api.model.Animal;
import api.model.Sex;
import api.model.Type;
import api.service.category.AnimalCategoryStrategy;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FileParserImpl implements FileParser {
    private final AnimalMapper animalMapper;
    private final AnimalCategoryStrategy categoryStrategy;

    @Override
    public List<Animal> parse(List<AnimalCreateRequestDto> requestDtos) {
        return requestDtos
                .stream()
                .map(dto -> {
                    if (isNotValid(dto)) {
                        return null;
                    }
                    try {
                        Animal animal = animalMapper.toModel(dto);
                        Type type = Type.fromString(dto.getType());
                        Sex sex = Sex.fromString(dto.getSex());
                        Long categoryId = categoryStrategy
                                .getAnimalCategoryService(dto.getCost())
                                .getCategory(dto.getCost());
                        animal.setType(type)
                                .setSex(sex)
                                .setCategoryId(categoryId);
                        return animal;
                    } catch (Exception e) {
                        /**
                         * just skipping non-valid animals
                         * for example:
                         * exception occurred when parsing type or sex etc.
                         */
                        return null;
                    }
                })
                // filtering null animals and returning valid only
                .filter(Objects::nonNull)
                .toList();
    }

    private boolean isNotValid(AnimalCreateRequestDto dto) {
        return (dto.getName() == null || dto.getName().isEmpty())
                && dto.getCost() <= 0 && dto.getWeight() <= 0;
    }
}
