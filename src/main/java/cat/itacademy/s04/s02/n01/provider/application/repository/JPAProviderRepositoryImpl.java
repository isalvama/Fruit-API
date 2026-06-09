package cat.itacademy.s04.s02.n01.provider.application.repository;

import cat.itacademy.s04.s02.n01.provider.application.repository.entity.ProviderJPAEntity;
import cat.itacademy.s04.s02.n01.provider.application.repository.entity.ProviderMapper;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JPAProviderRepositoryImpl implements ProviderRepository {

    private final JpaProviderSpringDataRepository jpaProviderSpringDataRepository;

    @Override
    public Provider saveProvider(Provider provider) {
        if (provider.getId() == null) {
            ProviderJPAEntity providerJPAEntity = ProviderMapper.toEntity(provider);
            providerJPAEntity = jpaProviderSpringDataRepository.save(providerJPAEntity);
            return ProviderMapper.toDomain(providerJPAEntity);
        }
        ProviderJPAEntity providerJPAEntity =  jpaProviderSpringDataRepository.findById(String.valueOf(provider.getId()))
                .map(entity -> {
                    entity.updateProvider(
                            provider.getName().name(),
                            provider.getCountry().name()
                    );
                    return jpaProviderSpringDataRepository.save(entity);
                }).orElseGet(() -> jpaProviderSpringDataRepository.save(ProviderMapper.toEntity(provider)));
        return ProviderMapper.toDomain(providerJPAEntity);
    }

    @Override
    public Optional<Provider> getProviderByName(String name) {
        Optional<ProviderJPAEntity> providerJpaEntity = jpaProviderSpringDataRepository.findByName(name);
        if (providerJpaEntity.isPresent()){
            return providerJpaEntity.map(ProviderMapper::toDomain);
        }
       return Optional.empty();
    }

    @Override
    public Optional<Provider> getProviderById(Long id) {
        Optional<ProviderJPAEntity> providerJpaEntity = jpaProviderSpringDataRepository.findById(id.toString());
        if (providerJpaEntity.isPresent()){
            return providerJpaEntity.map(ProviderMapper::toDomain);
        }
        return Optional.empty();
    }

    @Override
    public void deleteProviderById(Long id){
        jpaProviderSpringDataRepository.deleteById(String.valueOf(id));
    }
}

