/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package gradle;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test void HelloOtus_GuavaExampleIntMath_checkedAdd() {
        String res = null;
        try {
            HelloOtus.GuavaExampleIntMath_checkedAdd(10, 10);
        } catch (Exception ex) {
            res = ex.getMessage();
        }
        assertNull(res, "Test error: " + res);
    }
}
