package homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ThreadStarterTest {

    // Все тесты должны проходить, менять тесты не надо.

    @Test
    @DisplayName("Проверяем, что класс Customer не сломан (Test1)")
    void setterCustomerTest() {
        ThreadStarter counter = new ThreadStarter();
        boolean result = counter.start();
        //then
        assertThat(result).isEqualTo(true);
    }
}