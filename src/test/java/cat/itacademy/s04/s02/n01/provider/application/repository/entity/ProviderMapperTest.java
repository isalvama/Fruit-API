package cat.itacademy.s04.s02.n01.provider.application.repository.entity;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import cat.itacademy.s04.s02.n01.provider.domain.model.Country;
import cat.itacademy.s04.s02.n01.provider.domain.model.Provider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProviderMapperTest {

    private static final String PROVIDER_NAME = "Provider";
    private static final String COUNTRY = "UK";
    private static final Long ID = 321L;


    @Nested
    @DisplayName("Map To Domain")
    public class ToDomain{
        @Test
        void toDomainModel() {
            Provider provider = new Provider(null, Name.of(PROVIDER_NAME), Country.of(COUNTRY));

            ProviderJPAEntity providerJPAEntity = ProviderMapper.toEntity(provider);
            assertNull(providerJPAEntity.getId());
            assertEquals(PROVIDER_NAME, providerJPAEntity.getName());
            assertEquals(COUNTRY, providerJPAEntity.getCountry());
        }
    }

    @Nested
    @DisplayName("Map To Entity")
    public class ToEntity{
        @Test
        void toModelEntity() {
            ProviderJPAEntity providerJPAEntity = new ProviderJPAEntity(ID, PROVIDER_NAME, COUNTRY);
            Provider provider = ProviderMapper.toDomain(providerJPAEntity);
            assertEquals(PROVIDER_NAME, provider.getName().name());
            assertEquals(COUNTRY, provider.getCountry().name());
        }
    }
}