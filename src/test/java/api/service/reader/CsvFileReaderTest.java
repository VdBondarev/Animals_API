package api.service.reader;

import static org.junit.jupiter.api.Assertions.assertEquals;

import api.dto.AnimalCreateRequestDto;
import java.util.List;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class CsvFileReaderTest {
    private AnimalCreateRequestDto buddyAnimalRequest;
    private AnimalCreateRequestDto cooperAnimalRequest;
    private AnimalCreateRequestDto kittyAnimalRequest;
    private AnimalCreateRequestDto muhaAnimalRequest;
    private FileReader fileReader = new CsvFileReader();

    @BeforeEach
    void setUp() {
        buddyAnimalRequest =
                createAnimalRequestDto("Buddy", "cat", "female", "51", "25");
        cooperAnimalRequest =
                createAnimalRequestDto("Cooper", "", "female", "46", "25");
        kittyAnimalRequest =
                createAnimalRequestDto("Kitty", "dog", "male", "33", "111");
        muhaAnimalRequest =
                createAnimalRequestDto("mUHA", "dog", "female", "12", "22");
    }

    @Test
    @DisplayName("""
            Verify that reading from csv file works as expected with valid input file
            """)
    void readFromFile_ValidCsvFile_ReturnsValidResponse() throws FileUploadException {
        String csvContent = """
                Name,Type,Sex,Weight,Cost
                Buddy,cat,female,51,25
                Cooper,,female,46,25
                Kitty,dog,male,33,111
                mUHA,dog,female,12,22
                """;

        MultipartFile csvFile;
        csvFile = new MockMultipartFile("test.csv", "test.csv",
                "text/plain", csvContent.getBytes());

        List<AnimalCreateRequestDto> expected = List.of(
                buddyAnimalRequest,
                cooperAnimalRequest,
                kittyAnimalRequest,
                muhaAnimalRequest
        );

        List<AnimalCreateRequestDto> actual = fileReader.readFromFile(csvFile);

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
}
