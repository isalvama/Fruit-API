package cat.itacademy.s04.s02.n01.fruit.application.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table (name = "fruit")
public class FruitJpaEntity {
    @Id
    @GeneratedValue
    @Getter
    private Long id;
    @Getter
    private String name;
    @Getter
    private Double weightInKg;

    public FruitJpaEntity(Long id, String name, double weightInKg) {
        this.id = id;
        this.name = name;
        this.weightInKg = weightInKg;
    }

    public void updateFruit(String name, Double weightInKg){
        this.name = name;
        this.weightInKg = weightInKg;
    }


}
