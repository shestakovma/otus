package homework.tests;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

public class TestClass {
    //статические поля для хранения статистики по запуску тестов
    private static int runOk = 0;
    private static int runError = 0;
    private static int runTotal = 0;

    @Before
    public void runBefore(int value1, int value2) {
        System.out.println("TestClass.runBefore called, args: " + value1 + ", " + value2);
    }

    @Test
    public void runTest1(int value1, int value2) {
        int result = value1 / (value1 + value2);
        System.out.println("TestClass.runTest1 passed, args: " + value1 + ", " + value2 + ", result: " + result);
    }

    @Test
    public void runTest2(int value1, int value2) {
        int result = value1 / (value1 - value2);
        System.out.println("TestClass.runTest2 passed, args: " + value1 + ", " + value2 + ", result: " + result);
    }

    @Test
    public void runTest3(int value1, int value2) {
        int result = value1 * (value1 - value2);
        System.out.println("TestClass.runTest3 passed, args: " + value1 + ", " + value2 + ", result: " + result);
    }

    @After
    public void runAfter(int value1, int value2) {
        System.out.println("TestClass.runAfter called, args: " + value1 + ", " + value2);
    }
}
