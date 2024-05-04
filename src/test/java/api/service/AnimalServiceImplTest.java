package api.service;

import static api.constant.FilesRelatedConstants.CSV_CONTENT_TYPE;
import static api.constant.FilesRelatedConstants.XML_CONTENT_TYPE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import api.dto.AnimalCreateRequestDto;
import api.dto.AnimalResponseDto;
import api.dto.AnimalSearchParamsRequestDto;
import api.mapper.AnimalMapper;
import api.model.Animal;
import api.model.Sex;
import api.model.Type;
import api.repository.AnimalRepository;
import api.service.parser.FileParser;
import api.service.reader.CsvFileReader;
import api.service.reader.XmlFileReader;
import api.service.strategy.AnimalReaderStrategy;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

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
    private Animal buddyAnimal;
    private Animal kittyAnimal;
    private AnimalResponseDto buddyResponseDto;
    private AnimalResponseDto kittyResponseDto;

    @BeforeEach
    void setUp() {
        buddyAnimal = createAnimal(
                1L, "Buddy", "cat", "female", 51, 25, 2L
        );
        kittyAnimal = createAnimal(
                2L, "Kitty", "dog", "male", 33, 111, 4L
        );

        buddyResponseDto = fromAnimal(buddyAnimal);
        kittyResponseDto = fromAnimal(kittyAnimal);
    }

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
                CSV_CONTENT_TYPE, csvContent.getBytes());

        AnimalCreateRequestDto buddyAnimalRequest =
                createAnimalRequestDto("Buddy", "cat", "female", "51", "25");
        AnimalCreateRequestDto kittyAnimalRequest =
                createAnimalRequestDto("Kitty", "dog", "male", "33", "111");

        // expecting only 2 zoo passed the validation
        List<AnimalCreateRequestDto> expectedRequestList;
        expectedRequestList = List.of(buddyAnimalRequest, kittyAnimalRequest);

        List<Animal> expectedAnimalList;
        expectedAnimalList = List.of(buddyAnimal, kittyAnimal);

        CsvFileReader mockedFileReader = mock(CsvFileReader.class);

        AnimalResponseDto buddyResponseDto = fromAnimal(buddyAnimal);
        AnimalResponseDto kittyResponseDto = fromAnimal(kittyAnimal);

        List<AnimalResponseDto> expected;
        expected = List.of(buddyResponseDto, kittyResponseDto);

        when(readerStrategy.getFileReader(csvFile.getContentType())).thenReturn(mockedFileReader);
        when(mockedFileReader.readFromFile(csvFile)).thenReturn(expectedRequestList);
        when(fileParser.parse(expectedRequestList)).thenReturn(expectedAnimalList);
        when(animalRepository.saveAll(expectedAnimalList)).thenReturn(expectedAnimalList);
        when(animalMapper.toResponseDto(buddyAnimal)).thenReturn(buddyResponseDto);
        when(animalMapper.toResponseDto(kittyAnimal)).thenReturn(kittyResponseDto);

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
                XML_CONTENT_TYPE, xmlContent.getBytes());

        AnimalCreateRequestDto buddyAnimalRequest =
                createAnimalRequestDto("Buddy", "cat", "female", "51", "25");
        AnimalCreateRequestDto kittyAnimalRequest =
                createAnimalRequestDto("Kitty", "dog", "male", "33", "111");

        // expecting only 2 zoo passed the validation
        List<AnimalCreateRequestDto> expectedRequestList;
        expectedRequestList = List.of(buddyAnimalRequest, kittyAnimalRequest);

        List<Animal> expectedAnimalList;
        expectedAnimalList = List.of(buddyAnimal, kittyAnimal);

        XmlFileReader mockedFileReader = mock(XmlFileReader.class);

        AnimalResponseDto buddyResponseDto = fromAnimal(buddyAnimal);
        AnimalResponseDto kittyResponseDto = fromAnimal(kittyAnimal);

        List<AnimalResponseDto> expected;
        expected = List.of(buddyResponseDto, kittyResponseDto);

        when(readerStrategy.getFileReader(xmlFile.getContentType())).thenReturn(mockedFileReader);
        when(mockedFileReader.readFromFile(xmlFile)).thenReturn(expectedRequestList);
        when(fileParser.parse(expectedRequestList)).thenReturn(expectedAnimalList);
        when(animalRepository.saveAll(expectedAnimalList)).thenReturn(expectedAnimalList);
        when(animalMapper.toResponseDto(buddyAnimal)).thenReturn(buddyResponseDto);
        when(animalMapper.toResponseDto(kittyAnimal)).thenReturn(kittyResponseDto);

        List<AnimalResponseDto> actual = animalService.upload(xmlFile);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Verify that search() works fine with valid params")
    void search_ValidParams_ReturnsValidResponse() {
        AnimalSearchParamsRequestDto paramsRequestDto =
                new AnimalSearchParamsRequestDto(null, "cat", null, null);

        PageRequest pageable = PageRequest.of(0, 5);
        Page<Animal> page = new PageImpl<>(
                List.of(buddyAnimal),
                pageable,
                List.of(buddyAnimal).size());

        AnimalResponseDto expected = buddyResponseDto;

        when(animalRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(page);
        when(animalMapper.toResponseDto(buddyAnimal)).thenReturn(expected);

        List<AnimalResponseDto> expectedList = List.of(expected);

        List<AnimalResponseDto> actualList = animalService.search(paramsRequestDto, pageable);

        assertEquals(expectedList, actualList);
        assertEquals(expected, actualList.get(0));
    }

    @Test
    @DisplayName("Verify that search() throws an exception when passing non-valid params")
    void search_NullPassedParams_ThrowsException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> animalService.search(null, PageRequest.of(0, 5)));

        String expected = """
                Searching should be done by at least 1 param
                """;
        String actual = exception.getMessage();

        assertEquals(expected, actual);

        exception = assertThrows(IllegalArgumentException.class, () -> animalService.search(
                new AnimalSearchParamsRequestDto(null, null, null, null),
                PageRequest.of(0, 5))
        );

        actual = exception.getMessage();

        assertEquals(expected, actual);
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

    private Animal createAnimal(
            long id, String name, String type, String sex, int weight, int cost, Long categoryId) {
        return new Animal()
                .setCategoryId(categoryId)
                .setId(id)
                .setType(Type.fromString(type))
                .setSex(Sex.fromString(sex))
                .setName(name)
                .setWeight(weight)
                .setCost(cost);
    }
}
