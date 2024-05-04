package api.repository;

import api.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AnimalRepository extends JpaRepository<Animal, Long>,
        JpaSpecificationExecutor<Animal> {
}
