package api.service.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import api.dto.AnimalCreateRequestDto;
import api.mapper.AnimalMapper;
import api.model.Animal;
import api.model.enums.Sex;
import api.model.enums.Type;
import api.service.category.FourthAnimalCategoryService;
import api.service.category.SecondAnimalCategoryService;
import api.service.strategy.AnimalCategoryStrategy;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileParserImplTest {
    @Mock
    private AnimalMapper animalMapper;
    @Mock
    private AnimalCategoryStrategy categoryStrategy;
    @InjectMocks
    private FileParserImpl fileParser;

    @Test
    void parse_ValidInput_ReturnsValidResponse() {
        AnimalCreateRequestDto buddyAnimalRequest =
                createAnimalRequestDto("Buddy", "cat", "female", "51", "25");
        AnimalCreateRequestDto kittyAnimalRequest =
                createAnimalRequestDto("Kitty", "dog", "male", "33", "111");
        AnimalCreateRequestDto nonValidNameRequest =
                createAnimalRequestDto("mUHA", "cat", "male", "12", "22");
        AnimalCreateRequestDto nonValidSexRequest =
                createAnimalRequestDto("Cooper", "dog", "banana", "22", "22");
        AnimalCreateRequestDto nonValidTypeRequest =
                createAnimalRequestDto("Pal", "banana", "male", "25", "1");
        AnimalCreateRequestDto nonValidCostRequest =
                createAnimalRequestDto("Pal", "dog", "female", "21", "0");
        AnimalCreateRequestDto nonValidWeightRequest =
                createAnimalRequestDto("Weight", "dog", "female", "0", "15");

        List<AnimalCreateRequestDto> inputList;
        inputList = List.of(
                buddyAnimalRequest,
                kittyAnimalRequest,
                nonValidCostRequest,
                nonValidSexRequest,
                nonValidNameRequest,
                nonValidTypeRequest,
                nonValidWeightRequest
        );

        Animal buddy = fromRequestDto(buddyAnimalRequest);
        Animal kitty = fromRequestDto(kittyAnimalRequest);

        when(categoryStrategy.getAnimalCategoryService(buddyAnimalRequest.getCost()))
                .thenReturn(mock(SecondAnimalCategoryService.class));
        when(categoryStrategy.getAnimalCategoryService(kittyAnimalRequest.getCost()))
                .thenReturn(mock(FourthAnimalCategoryService.class));
        when(animalMapper.toModel(buddyAnimalRequest)).thenReturn(buddy);
        when(animalMapper.toModel(kittyAnimalRequest)).thenReturn(kitty);

        List<Animal> expected = List.of(buddy, kitty);

        List<Animal> actual = fileParser.parse(inputList);

        assertEquals(expected, actual);
    }

    private Animal fromRequestDto(AnimalCreateRequestDto requestDto) {
        return new Animal()
                .setWeight(requestDto.getWeight())
                .setCost(requestDto.getCost())
                .setType(Type.fromString(requestDto.getType()))
                .setSex(Sex.fromString(requestDto.getSex()))
                .setName(requestDto.getName());
    }

    private AnimalCreateRequestDto createAnimalRequestDto(
            String name,
            String type,
            String sex,
            String weight,
            String cost
    ) {
        return new AnimalCreateRequestDto()
                .setCost(Integer.parseInt(cost))
                .setName(name)
                .setType(type)
                .setSex(sex)
                .setWeight(Integer.parseInt(weight));
    }
}
