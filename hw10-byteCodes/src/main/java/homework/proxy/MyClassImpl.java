package homework.proxy;


import homework.annotations.Log;

public class MyClassImpl {

    @Log
    public void secureAccess(String param) {
        System.out.println("secureAccess, param:" + param);
    }

    @Log
    public void secureAccess(String firstParam, String secondParam) {
        System.out.println("secureAccess, firstParam:" + firstParam + ", second param: " + secondParam);
    }

    @Log
    public void secureAccess(Integer value) {
        System.out.println("secureAccess, intParam:" + value);
    }

    @Log
    public void secureAccess() {
        System.out.println("secureAccess, noParams");
    }

    @Log
    public void methodWithInt(String paramString, int paramInt) {
        System.out.println("methodWithInt, int value: " + paramInt);
    }

    @Log
    public void methodWithBoolean(String paramString, boolean paramBoolean) {
        System.out.println("methodWithBoolean, boolean value: " + paramBoolean);
    }

    @Log
    public void methodWithDouble(String paramString, double paramDouble) {
        System.out.println("methodWithDouble, double value: " + paramDouble);
    }

    @Log
    public void multiSum(int intValue, double doubleValue, float floatValue) {
        System.out.println("multiSum, result: " + (intValue + doubleValue + floatValue));
    }

    @Override
    public String toString() {
        return "MyClassImpl{}";
    }
}
