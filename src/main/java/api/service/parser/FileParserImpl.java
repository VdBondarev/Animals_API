package api.service.parser;

import static api.constant.NumbersConstantsHolder.ZERO;

import api.dto.AnimalCreateRequestDto;
import api.mapper.AnimalMapper;
import api.model.Animal;
import api.model.enums.Sex;
import api.model.enums.Type;
import api.service.strategy.AnimalCategoryStrategy;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class FileParserImpl implements FileParser {
    private final AnimalMapper animalMapper;
    private final AnimalCategoryStrategy categoryStrategy;

    @Override
    public List<Animal> parse(List<AnimalCreateRequestDto> requestDtos) {
        List<Animal> animals = new ArrayList<>(requestDtos.size());
        for (AnimalCreateRequestDto requestDto : requestDtos) {
            if (isValid(requestDto)) {
                Type type = Type.fromString(requestDto.getType());
                Sex sex = Sex.fromString(requestDto.getSex());
                Long categoryId = categoryStrategy
                        .getAnimalCategoryService(requestDto.getCost())
                        .getCategory(requestDto.getCost());
                Animal animal = animalMapper.toModel(requestDto)
                        .setType(type)
                        .setSex(sex)
                        .setCategoryId(categoryId);
                animals.add(animal);
            }
        }
        return animals;
    }

    private boolean isValid(AnimalCreateRequestDto requestDto) {
        try {
            boolean isValid = requestDto != null
                    && requestDto.getName() != null
                    && !requestDto.getName().isEmpty()
                    && Character.isUpperCase(requestDto.getName().charAt(ZERO))
                    && requestDto.getCost() > ZERO
                    && requestDto.getWeight() > ZERO;
            Type.fromString(requestDto.getType());
            Sex.fromString(requestDto.getSex());
            return isValid;
        } catch (Exception e) {
            /**
             * if exception is thrown, the animal is not valid
             * for example:
             * some field is null
             * or type or sex of the animal is not present in enum
             */
            return false;
        }
    }
}
