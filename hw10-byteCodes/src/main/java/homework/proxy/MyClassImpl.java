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

    @Override
    public String toString() {
        return "MyClassImpl{}";
    }
}
