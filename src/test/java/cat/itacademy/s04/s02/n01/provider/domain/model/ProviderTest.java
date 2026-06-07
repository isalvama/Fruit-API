package cat.itacademy.s04.s02.n01.provider.domain.model;

import cat.itacademy.s04.s02.n01.common.domain.value_object.Name;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProviderTest {

    private static final Country COUNTRY = Country.of("EU");
    private static final Name NAME = Name.of("provider");

    @Test
    void shouldCreateProviderWithFactoryMethod() {
        Provider provider = Provider.create(NAME, COUNTRY);

        assertNotNull(provider);
        assertEquals(COUNTRY, provider.getCountry());
        assertEquals(NAME, provider.getName());
    }
}