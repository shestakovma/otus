package homework;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class TestRunner {

    private static final String STATS_RUN_TOTAL = "runTotal";
    private static final String STATS_RUN_OK = "runOk";
    private static final String STATS_RUN_ERROR = "runError";

    private static final String ANNOTATION_BEFORE = "homework.annotations.Before";
    private static final String ANNOTATION_AFTER = "homework.annotations.After";
    private static final String ANNOTATION_TEST = "homework.annotations.Test";

    public static Boolean run(String className, Object... args)  {
        Boolean result = true;

        try {
            Class<?> testClass = getClass(className);
            Object testObject = getObject(className);

            List<Method> methodsTest = getMethods(testObject, ANNOTATION_TEST);
            for (Method method: methodsTest) {
                runTest(testObject, method, args);
            }

            printStats(testClass);
        }
        catch (Exception ex) {
            result = false;
        }
        return result;
    }

    private static void runTest(Object object, Method methodTest, Object... args) throws Exception {
        try {
            //получаем набор методов Before
            List<Method> methodsBefore = getMethods(object, ANNOTATION_BEFORE);
            //выполняем набор
            callMethods(object, methodsBefore, args);
            //выполняем основной метод
            methodTest.invoke(object, args);
            //получаем набор методов After
            List<Method> methodsAfter = getMethods(object, ANNOTATION_AFTER);
            //выполняем набор
            callMethods(object, methodsAfter, args);

            //обновляем статистику по успехам
            increaseStaticIntegerValue(object.getClass(), STATS_RUN_OK);
        }
        catch(Exception ex) {
            //обновляем статистику по ошибкам
            increaseStaticIntegerValue(object.getClass(), STATS_RUN_ERROR);
            System.out.println("Test failed, method: " + methodTest.getName() + ", exception: " + ex.getMessage());
        }
        finally {
            //обновляем статистику по запускам
            increaseStaticIntegerValue(object.getClass(), STATS_RUN_TOTAL);
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

    private static List<Method> getMethods(Object testObject, String annotation) throws Exception {
        List<Method> methodsAll = Arrays.stream(testObject.getClass().getMethods()).toList();
        return methodsAll.stream().filter(method ->
                Arrays.stream(method.getDeclaredAnnotations()).filter(
                        annotation1 -> annotation1.annotationType().getName() == annotation).count() > 0
        ).toList();
    }

    private static void callMethods(Object testObject, List<Method> methods, Object... args) throws Exception {
        for (Method method: methods) {
            method.invoke(testObject, args);
        }
    }

    private static void increaseStaticIntegerValue(Class<?> testClass, String staticFieldName) throws Exception {
        Field staticField = Arrays.stream(testClass.getDeclaredFields()).filter(field ->
                    java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
                    field.getName() == staticFieldName
        ).toList().get(0);
        staticField.setAccessible(true);
        int currentValue = (Integer)staticField.get(null);
        staticField.set(null, currentValue + 1);
    }

    private static int getStaticIntegerValue(Class<?> testClass, String staticFieldName) throws Exception {
        Field staticField = Arrays.stream(testClass.getDeclaredFields()).filter(field ->
                java.lang.reflect.Modifier.isStatic(field.getModifiers()) &&
                        field.getName() == staticFieldName
        ).toList().get(0);
        staticField.setAccessible(true);
        return (Integer)staticField.get(null);
    }

    private static void printStats(Class<?> testClass) throws Exception {
        int testsRunTotal = getStaticIntegerValue(testClass, STATS_RUN_TOTAL);
        int testsRunOk = getStaticIntegerValue(testClass, STATS_RUN_OK);
        int testsRunError = getStaticIntegerValue(testClass, STATS_RUN_ERROR);

        System.out.println("tests run total: " + testsRunTotal);
        System.out.println("tests run ok: " + testsRunOk);
        System.out.println("tests run errors: " + testsRunError);
    }
}
