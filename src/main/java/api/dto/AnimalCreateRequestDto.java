package api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class AnimalCreateRequestDto {
    private String name;
    private String type;
    private String sex;
    private int weight;
    private int cost;
}
