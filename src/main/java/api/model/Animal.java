package api.model;

import api.annotation.StartsWithCapital;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@Table(name = "animals")
@SQLDelete(sql = "UPDATE animals SET is_deleted = TRUE WHERE id = ?")
@Where(clause = "is_deleted = FALSE")
@Accessors(chain = true)
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @StartsWithCapital
    private String name;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Type type;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Sex sex;

    @Min(1)
    private int weight;

    @Min(1)
    private int cost;

    @Min(1)
    private Long categoryId;

    private boolean isDeleted = false;
}
