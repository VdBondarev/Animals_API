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

class XmlFileReaderTest {
    private AnimalCreateRequestDto buddyAnimalRequest;
    private AnimalCreateRequestDto cooperAnimalRequest;
    private AnimalCreateRequestDto kittyAnimalRequest;
    private AnimalCreateRequestDto muhaAnimalRequest;
    private FileReader fileReader = new XmlFileReader();

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
            Verify that reading from xml file works as expected with valid input file
            """)
    public void readFromFile_ValidXmlFile_ReturnsValidResponse() throws FileUploadException {
        String xmlContent = """
            <?xml version="1.0" encoding="UTF-8"?>
                <animals>
                    <animal>
                        <name>Buddy</name>
                        <type>cat</type>
                        <sex>female</sex>
                        <weight>51</weight>
                        <cost>25</cost>
                    </animal>
                    <animal>
                        <name>Cooper</name>
                        <type></type>
                        <sex>female</sex>
                        <weight>46</weight>
                        <cost>25</cost>
                    </animal>
                    <animal>
                        <name>Kitty</name>
                        <type>dog</type>
                        <sex>male</sex>
                        <weight>33</weight>
                        <cost>111</cost>
                    </animal>
                    <animal>
                        <name>mUHA</name>
                        <type>dog</type>
                        <sex>female</sex>
                        <weight>12</weight>
                        <cost>22</cost>
                    </animal>
                </animals>
                """;

        MultipartFile xmlFile;
        xmlFile = new MockMultipartFile("test.xml", "test.xml",
                "application/xml", xmlContent.getBytes());

        List<AnimalCreateRequestDto> expected = List.of(
                buddyAnimalRequest,
                cooperAnimalRequest,
                kittyAnimalRequest,
                muhaAnimalRequest
        );

        List<AnimalCreateRequestDto> actual = fileReader.readFromFile(xmlFile);

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
