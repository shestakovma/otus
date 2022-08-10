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
        boolean result = TestRunner.run("homework.tests.TestClass", 1);
        System.out.println(result);
        //then
        assertThat(result).isEqualTo(true);
    }
}