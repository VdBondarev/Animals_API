package api.controller;

import static api.holder.LinksHolder.ADD_ANIMALS_TO_ANIMALS_FILE_PATH;
import static api.holder.LinksHolder.REMOVE_ALL_ANIMALS_FILE_PATH;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import api.dto.AnimalResponseDto;
import api.dto.AnimalSearchParamsRequestDto;
import api.model.Sex;
import api.model.Type;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class AnimalControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .build();
    }

    @Test
    @DisplayName("Verify that search() endpoint works as expected with valid input")
    @Sql(
            scripts = {
                    ADD_ANIMALS_TO_ANIMALS_FILE_PATH
            },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
    )
    @Sql(
            scripts = {
                    REMOVE_ALL_ANIMALS_FILE_PATH
            },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD
    )
    void search_ValidParams_Success() throws Exception {
        AnimalSearchParamsRequestDto requestDto =
                new AnimalSearchParamsRequestDto("Barsik", "cat", "male", 1L);

        String content = objectMapper.writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(get("/animals/search")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        AnimalResponseDto expectedDto = new AnimalResponseDto(
                1L,
                "Barsik",
                Type.CAT,
                Sex.MALE,
                10,
                9,
                1L
        );

        AnimalResponseDto[] expectedArray = new AnimalResponseDto[] {expectedDto};
        AnimalResponseDto[] actualArray = objectMapper.readValue(
                result.getResponse().getContentAsByteArray(), AnimalResponseDto[].class);

        assertEquals(expectedArray[0], actualArray[0]);
    }
}
