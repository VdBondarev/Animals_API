package api.mapper;

import api.config.MapperConfig;
import api.dto.AnimalCreateRequestDto;
import api.model.Animal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AnimalMapper {

    @Mapping(target = "sex", ignore = true)
    @Mapping(target = "type", ignore = true)
    Animal toModel(AnimalCreateRequestDto requestDto);
}
