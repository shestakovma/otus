package homework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class TestRunner {
    private static final String STATS_RUN_TOTAL = "runTotal";
    private static final String STATS_RUN_OK = "runOk";
    private static final String STATS_RUN_ERROR = "runError";

    public static boolean run(String className, Object... args)  {
        boolean result = true;
        HashMap<String, Integer> counters = new HashMap<>();
        counters.put(STATS_RUN_TOTAL, 0);
        counters.put(STATS_RUN_OK, 0);
        counters.put(STATS_RUN_ERROR, 0);

        Integer counterTotal = 0;
        Integer counterOk = 0;
        Integer counterError = 0;
        try {
            Class<?> testClass = getClass(className);
            List<Method> methodsTest = getMethods(testClass, Test.class);

            //получаем набор методов Before
            List<Method> methodsBefore = getMethods(testClass, Before.class);
            //получаем набор методов After
            List<Method> methodsAfter = getMethods(testClass, After.class);

            for (Method method: methodsTest) {
                //запускаем тест каждый раз для нового объекта
                Object testObject = getObject(className);
                runTest(testObject, method, methodsBefore, methodsAfter, counters, args);
            }

            printStats(counters);
        }
        catch (Exception ex) {
            System.out.println("TestRunner.run exception: " + ex.getMessage());
            result = false;
        }
        return result;
    }

    private static void runTest(
            Object object,
            Method methodTest,
            List<Method> methodsBefore,
            List<Method> methodsAfter,
            HashMap<String, Integer> counters,
            Object... args
    ) throws Exception {
        try {
            //выполняем набор Before
            callMethods(object, methodsBefore, args);
            //выполняем основной метод
            methodTest.invoke(object, args);
            //выполняем набор After
            callMethods(object, methodsAfter, args);

            //обновляем статистику по успехам
            increaseCounter(counters, STATS_RUN_OK);
        }
        catch(Exception ex) {
            //обновляем статистику по ошибкам
            increaseCounter(counters, STATS_RUN_ERROR);
            System.out.println("Test failed, method: " + methodTest.getName() + ", exception: " + ex.getMessage());
        }
        finally {
            //обновляем статистику по запускам
            increaseCounter(counters, STATS_RUN_TOTAL);
        }
    }

    private static Object getObject(String className) throws Exception {
        Object result = null;
        Class<?> testClass = Class.forName(className);
        Constructor<?> constructor = testClass.getConstructor();
        return constructor.newInstance();
    }

    private static Class<?> getClass(String className) throws Exception {
        return Class.forName(className);
    }

    private static List<Method> getMethods(Class<?> testClass, Class<? extends java.lang.annotation.Annotation> annotationClass) throws Exception {
        List<Method> methodsAll = Arrays.stream(testClass.getMethods()).toList();
        return methodsAll.stream().filter(method -> method.isAnnotationPresent(annotationClass)).toList();
    }

    private static void callMethods(Object testObject, List<Method> methods, Object... args) throws Exception {
        for (Method method: methods) {
            method.invoke(testObject, args);
        }
    }

    private static void increaseCounter(HashMap<String, Integer> counters, String counterKey) {
        if (counters.containsKey(counterKey)) {
            counters.put(counterKey, counters.get(counterKey) + 1);
        }
    }

    private static Integer getCounter(HashMap<String, Integer> counters, String counterKey) {
        if (counters.containsKey(counterKey)) {
            return counters.get(counterKey);
        }
        return 0;
    }

    private static void printStats(HashMap<String, Integer> counters) {
        System.out.println("tests run total: " + getCounter(counters, STATS_RUN_TOTAL));
        System.out.println("tests run ok: " + getCounter(counters, STATS_RUN_OK));
        System.out.println("tests run errors: " + getCounter(counters, STATS_RUN_ERROR));
    }
}
