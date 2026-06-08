package cat.itacademy.s04.s02.n01.provider.application.repository;

import cat.itacademy.s04.s02.n01.provider.application.repository.entity.ProviderJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaProviderSpringDataRepository extends JpaRepository<ProviderJPAEntity, String> {
    Optional<ProviderJPAEntity> findByName(String name);
}
