package cat.itacademy.s04.s02.n01.fruit.application.repository.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table (name = "fruit")
public class FruitJpaEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private double weightInKg;

    public FruitJpaEntity(Long id, String name, double weightInKg) {
        this.id = id;
        this.name = name;
        this.weightInKg = weightInKg;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWeightInKg() {
        return weightInKg;
    }
}
