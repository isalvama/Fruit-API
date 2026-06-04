package cat.itacademy.s04.s02.n01.fruit.application.usecases;
import cat.itacademy.s04.s02.n01.fruit.domain.model.Fruit;

public interface CreateFruitUseCase {

    Fruit createFruit(String name, double weightAmount, String magnitude);
}
