package api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import api.dto.AnimalCreateRequestDto;
import api.dto.AnimalResponseDto;
import api.mapper.AnimalMapper;
import api.model.Animal;
import api.model.Sex;
import api.model.Type;
import api.repository.AnimalRepository;
import api.service.parser.FileParser;
import api.service.reader.CsvFileReader;
import api.service.reader.XmlFileReader;
import api.service.strategy.AnimalReaderStrategy;
import liquibase.changelog.filter.ActuallyExecutedChangeSetFilter;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class AnimalServiceImplTest {
    @Mock
    private AnimalMapper animalMapper;
    @Mock
    private AnimalReaderStrategy readerStrategy;
    @Mock
    private FileParser fileParser;
    @Mock
    private AnimalRepository animalRepository;
    @InjectMocks
    private AnimalServiceImpl animalService;


    @Test
    @DisplayName("Verify that upload() method works fine for csv file")
    void upload_ValidCsvFile_ReturnsValidResponse() throws FileUploadException {
        String csvContent = """
                Name,Type,Sex,Weight,Cost
                Buddy,cat,female,51,25
                Cooper,,female,46,25
                Kitty,dog,male,33,111
                mUHA,dog,female,12,22
                """; // don't ask why Kitty is a dog :)

        MultipartFile csvFile;
        csvFile = new MockMultipartFile("test.csv", "test.csv",
                "text/plain", csvContent.getBytes());

        AnimalCreateRequestDto buddyAnimalRequest =
                createAnimalRequestDto("Buddy", "cat", "female", "51", "25");
        AnimalCreateRequestDto kittyAnimalRequest =
                createAnimalRequestDto("Kitty", "dog", "male", "33", "111");

        // expecting only 2 zoo passed the validation
        List<AnimalCreateRequestDto> expectedRequestList;
        expectedRequestList = List.of(buddyAnimalRequest, kittyAnimalRequest);

        Animal buddy = fromRequestDto(buddyAnimalRequest);
        Animal kitty = fromRequestDto(kittyAnimalRequest);

        List<Animal> expectedAnimalList = List.of(buddy, kitty);

        CsvFileReader mockedFileReader = mock(CsvFileReader.class);

        AnimalResponseDto buddyResponseDto = fromAnimal(buddy);
        AnimalResponseDto kittyResponseDto = fromAnimal(kitty);

        List<AnimalResponseDto> expected = List.of(buddyResponseDto, kittyResponseDto);

        when(readerStrategy.getFileReader(csvFile.getContentType())).thenReturn(mockedFileReader);
        when(mockedFileReader.readFromFile(csvFile)).thenReturn(expectedRequestList);
        when(fileParser.parse(expectedRequestList)).thenReturn(expectedAnimalList);
        when(animalRepository.saveAll(expectedAnimalList)).thenReturn(expectedAnimalList);
        when(animalMapper.toResponseDto(buddy)).thenReturn(buddyResponseDto);
        when(animalMapper.toResponseDto(kitty)).thenReturn(kittyResponseDto);

        List<AnimalResponseDto> actual = animalService.upload(csvFile);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify that upload() method works fine for xml file")
    void upload_XmlContent_ReturnsValidResponse() throws FileUploadException {
        String xmlContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <animals>
                <animal>
                    <Name>Buddy</Name>
                    <Type>cat</Type>
                    <Sex>female</Sex>
                    <Weight>51</Weight>
                    <Cost>25</Cost>
                </animal>
                <animal>
                    <Name>Cooper</Name>
                    <Type></Type>
                    <Sex>female</Sex>
                    <Weight>46</Weight>
                    <Cost>25</Cost>
                </animal>
                <animal>
                    <Name>Kitty</Name>
                    <Type>dog</Type>
                    <Sex>male</Sex>
                    <Weight>33</Weight>
                    <Cost>111</Cost>
                </animal>
                <animal>
                    <Name>mUHA</Name>
                    <Type>dog</Type>
                    <Sex>female</Sex>
                    <Weight>12</Weight>
                    <Cost>22</Cost>
                </animal>
            </animals>
                """;

        MultipartFile xmlFile;
        xmlFile = new MockMultipartFile("test.xml", "test.xml",
                "text/xml", xmlContent.getBytes());

        AnimalCreateRequestDto buddyAnimalRequest =
                createAnimalRequestDto("Buddy", "cat", "female", "51", "25");
        AnimalCreateRequestDto kittyAnimalRequest =
                createAnimalRequestDto("Kitty", "dog", "male", "33", "111");

        // expecting only 2 zoo passed the validation
        List<AnimalCreateRequestDto> expectedRequestList;
        expectedRequestList = List.of(buddyAnimalRequest, kittyAnimalRequest);

        Animal buddy = fromRequestDto(buddyAnimalRequest);
        Animal kitty = fromRequestDto(kittyAnimalRequest);

        List<Animal> expectedAnimalList = List.of(buddy, kitty);

        XmlFileReader mockedFileReader = mock(XmlFileReader.class);

        AnimalResponseDto buddyResponseDto = fromAnimal(buddy);
        AnimalResponseDto kittyResponseDto = fromAnimal(kitty);

        List<AnimalResponseDto> expected = List.of(buddyResponseDto, kittyResponseDto);

        when(readerStrategy.getFileReader(xmlFile.getContentType())).thenReturn(mockedFileReader);
        when(mockedFileReader.readFromFile(xmlFile)).thenReturn(expectedRequestList);
        when(fileParser.parse(expectedRequestList)).thenReturn(expectedAnimalList);
        when(animalRepository.saveAll(expectedAnimalList)).thenReturn(expectedAnimalList);
        when(animalMapper.toResponseDto(buddy)).thenReturn(buddyResponseDto);
        when(animalMapper.toResponseDto(kitty)).thenReturn(kittyResponseDto);

        List<AnimalResponseDto> actual = animalService.upload(xmlFile);

        assertEquals(expected, actual);
    }

    @Test
    void search() {
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

    private Animal fromRequestDto(AnimalCreateRequestDto requestDto) {
        return new Animal()
                .setCost(requestDto.getCost())
                .setName(requestDto.getName())
                .setType(Type.fromString(requestDto.getType()))
                .setSex(Sex.fromString(requestDto.getSex()))
                .setWeight(requestDto.getWeight());
    }

    private AnimalResponseDto fromAnimal(Animal animal) {
        return new AnimalResponseDto(
                animal.getId(),
                animal.getName(),
                animal.getType(),
                animal.getSex(),
                animal.getWeight(),
                animal.getCost(),
                animal.getCategoryId()
        );
    }
}
/**
 *  private final AnimalRepository animalRepository;
 *     private final AnimalMapper animalMapper;
 *     private final AnimalReaderStrategy readerStrategy;
 *     private final FileParser fileParser;
 *
 *     @Override
 *     @Transactional
 *     public List<AnimalResponseDto> upload(MultipartFile file) throws FileUploadException {
 *         String contentType = file.getContentType();
 *         FileReader reader = readerStrategy.getFileReader(contentType);
 *         List<AnimalCreateRequestDto> requestDtos = reader.readFromFile(file);
 *         List<Animal> parsedAnimals = fileParser.parse(requestDtos);
 *         animalRepository.saveAll(parsedAnimals);
 *         return parsedAnimals
 *                 .stream()
 *                 .map(animalMapper::toResponseDto)
 *                 .toList();
 *     }
 *
 *     @Override
 *     public List<AnimalResponseDto> search(
 *             AnimalSearchParamsRequestDto requestDto, Pageable pageable
 *     ) {
 *         ExampleMatcher matcher = ExampleMatcher.matching()
 *                 .withIgnoreNullValues()
 *                 .withIgnorePaths(COST_FIELD, WEIGHT_FIELD)
 *                 .withMatcher(CATEGORY_ID_FIELD, ExampleMatcher.GenericPropertyMatchers.exact())
 *                 .withMatcher(SEX_FIELD, ExampleMatcher.GenericPropertyMatchers.exact())
 *                 .withMatcher(TYPE_FIELD, ExampleMatcher.GenericPropertyMatchers.exact())
 *                 .withMatcher(
 *                         NAME_FIELD, ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase()
 *                 );
 *
 *         Example<Animal> example = Example.of(
 *                 fromSearchParams(requestDto),
 *                 matcher
 *         );
 *         return animalRepository.findAll(example, pageable)
 *                 .stream()
 *                 .map(animalMapper::toResponseDto)
 *                 .toList();
 *     }
 *
 *     private Animal fromSearchParams(AnimalSearchParamsRequestDto requestDto) {
 *         return new Animal()
 *                 .setCategoryId(requestDto.categoryId())
 *                 .setSex(requestDto.sex() == null
 *                         ? null
 *                         : Sex.fromString(requestDto.sex())
 *                 )
 *                 .setType(requestDto.type() == null
 *                         ? null
 *                         : Type.fromString(requestDto.type())
 *                 )
 *                 .setName(requestDto.name());
 *     }
 */