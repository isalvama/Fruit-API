package cat.itacademy.s04.s02.n01.fruit.application.repository;

import cat.itacademy.s04.s02.n01.fruit.application.repository.entity.FruitJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaFruitSpringDataRepository extends JpaRepository<FruitJpaEntity, String> {
    List<FruitJpaEntity> findByProviderId(Long providerId);
}

