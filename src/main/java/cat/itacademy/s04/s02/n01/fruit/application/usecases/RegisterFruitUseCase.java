package cat.itacademy.s04.s02.n01.fruit.application.usecases;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;

public interface RegisterFruitUseCase {

    Fruit registerFruit(String name, Double weightAmount, String magnitudeString, Long ProviderId);
}
