package api.mapper;

import static api.constant.AnimalConstantsFolder.SEX_FIELD;
import static api.constant.AnimalConstantsFolder.TYPE_FIELD;

import api.config.MapperConfig;
import api.dto.AnimalCreateRequestDto;
import api.dto.AnimalResponseDto;
import api.model.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AnimalMapper {

    @Mapping(target = SEX_FIELD, ignore = true)
    @Mapping(target = TYPE_FIELD, ignore = true)
    Animal toModel(AnimalCreateRequestDto requestDto);

    AnimalResponseDto toResponseDto(Animal animal);
}
