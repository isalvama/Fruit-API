package cat.itacademy.s04.s02.n01.fruit.application.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "fruit")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FruitJpaEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Double weightInKg;

    public void updateFruit(String name, Double weightInKg){
        this.name = name;
        this.weightInKg = weightInKg;
    }


}
