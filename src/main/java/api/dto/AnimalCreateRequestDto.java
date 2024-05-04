package api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalCreateRequestDto {
    private String name;
    private String type;
    private String sex;
    private int weight;
    private int cost;
}
