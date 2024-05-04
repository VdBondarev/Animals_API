package api.controller;

import static api.constant.FilesRelatedConstants.CSV_CONTENT_TYPE;
import static api.constant.FilesRelatedConstants.XML_CONTENT_TYPE;
import static api.holder.LinksHolder.CSV_FILE_PATH;
import static api.holder.LinksHolder.REMOVE_ALL_ANIMALS_FILE_PATH;
import static api.holder.LinksHolder.WORD_FILE_PATH;
import static api.holder.LinksHolder.XML_FILE_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import api.dto.AnimalResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FileControllerTest {
    protected static MockMvc mockMvc;
    private static final String UPLOAD_ENDPOINT = "/files/upload";
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @Test
    @DisplayName("Verify that upload() method works as expected with .csv file")
    @Sql(
            scripts = REMOVE_ALL_ANIMALS_FILE_PATH,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void upload_CsvFile_Success() throws Exception {
        File file = new File(CSV_FILE_PATH);

        byte[] fileContent = Files.readAllBytes(file.toPath());

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", file.getName(), CSV_CONTENT_TYPE, fileContent);

        MvcResult result = mockMvc.perform(
                        multipart(UPLOAD_ENDPOINT)
                                .file(multipartFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andReturn();

        AnimalResponseDto[] animalResponseDtos = objectMapper.readValue(result.getResponse()
                .getContentAsString(), AnimalResponseDto[].class);

        int lengthExpected = 8;
        Assertions.assertEquals(lengthExpected, animalResponseDtos.length);
    }

    @Test
    @DisplayName("Verify that upload() method works as expected with .xml file")
    @Sql(
            scripts = REMOVE_ALL_ANIMALS_FILE_PATH,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void upload_XmlFile_Success() throws Exception {
        File file = new File(XML_FILE_PATH);

        byte[] fileContent = Files.readAllBytes(file.toPath());

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", file.getName(), XML_CONTENT_TYPE, fileContent);

        MvcResult result = mockMvc.perform(
                        multipart(UPLOAD_ENDPOINT)
                                .file(multipartFile)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isCreated())
                .andReturn();

        AnimalResponseDto[] animalResponseDtos = objectMapper.readValue(result.getResponse()
                .getContentAsString(), AnimalResponseDto[].class);

        int lengthExpected = 7;
        Assertions.assertEquals(lengthExpected, animalResponseDtos.length);
    }

    @Test
    @DisplayName("Verify that upload() method works as expected with .word file")
    @Sql(
            scripts = REMOVE_ALL_ANIMALS_FILE_PATH,
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void upload_workFile_Fail() throws Exception {
        File file = new File(WORD_FILE_PATH);

        byte[] fileContent = Files.readAllBytes(file.toPath());

        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", file.getName(), "text/word", fileContent);

        mockMvc.perform(
                multipart(UPLOAD_ENDPOINT)
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
