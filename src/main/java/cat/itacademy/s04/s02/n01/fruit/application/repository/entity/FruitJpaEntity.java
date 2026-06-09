package cat.itacademy.s04.s02.n01.fruit.application.repository.entity;

import cat.itacademy.s04.s02.n01.provider.application.repository.entity.ProviderJPAEntity;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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
    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false)
    private ProviderJPAEntity provider;

    public void updateFruit(String name, Double weightInKg, ProviderJPAEntity provider){
        this.name = name;
        this.weightInKg = weightInKg;
        this.provider = provider;
    }


}
