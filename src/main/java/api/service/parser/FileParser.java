package api.service.parser;

import api.dto.AnimalCreateRequestDto;
import api.model.Animal;
import java.util.List;

public interface FileParser {

    List<Animal> parse(List<AnimalCreateRequestDto> requestDtos);
}
