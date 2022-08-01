package homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class HomeworkTest {

    // Все тесты должны проходить, менять тесты не надо.

    @Test
    @DisplayName("Проверяем, что TestRunner отрабатывает")
    void runTestRunner() {
        //given
        Boolean result = TestRunner.run("homework.TestClass", 1);
        //then
        assertThat(result).isEqualTo(true);
    }
}