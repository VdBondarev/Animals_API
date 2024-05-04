package api.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AnimalCreateRequestDto {
    private String name;
    private String type;
    private String sex;
    private int weight;
    private int cost;
}
