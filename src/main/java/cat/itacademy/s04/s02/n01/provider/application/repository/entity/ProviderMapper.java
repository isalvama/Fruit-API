package cat.itacademy.s04.s02.n01.provider.application.repository.entity;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.provider.domain.model.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;

public class ProviderMapper {

    private ProviderMapper(){}

    public static Provider toDomain(ProviderJPAEntity providerJPAEntity){
        return new Provider(
                providerJPAEntity.getId(),
                Name.of(providerJPAEntity.getName()),
                Country.of(providerJPAEntity.getCountry())
        );
    }

    public static ProviderJPAEntity toEntity(Provider provider){
        return new ProviderJPAEntity(
                provider.getId() != null ? provider.getId() : null,
                provider.getName().name(),
                provider.getCountry().name()
        );
    }
}
