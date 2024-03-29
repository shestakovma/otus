/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package homework;

import homework.proxy.MyClassImpl;

/*
    java -javaagent:hw10-bytecodes.jar -jar hw10-bytecodes.jar
*/
public class App {
    public static void main(String[] args) {
        var myClass = new MyClassImpl();
        myClass.secureAccess("Security Param");
        myClass.secureAccess("First param", "Second param");
        myClass.secureAccess(20);
        myClass.secureAccess();
        myClass.methodWithInt("str", 123);
        myClass.methodWithDouble("str", 555);
        myClass.multiSum(1, 2, 3);
        myClass.methodWithBoolean("str", true);
    }
}
