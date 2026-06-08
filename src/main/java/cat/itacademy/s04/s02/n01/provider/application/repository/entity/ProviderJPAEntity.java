package cat.itacademy.s04.s02.n01.provider.application.repository.entity;

import cat.itacademy.s04.s02.n01.fruit.application.repository.entity.FruitJpaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProviderJPAEntity {
        @Id
        @GeneratedValue
        private Long id;
        private String name;
        private String country;
    }
