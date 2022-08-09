package homework.proxy;

import java.util.ArrayList;
import java.util.Collection;

public class MethodInfo {
    public MethodInfo(String signature, String name, String descriptor) {
        this.signature = signature;
        this.name = name;
        this.descriptor = descriptor;
    }

    private String signature;
    private String name;
    private String descriptor;
    private boolean isLoggable;
    private String nameRenamed;

    public String getSignature() { return signature; }
    public String getName() { return name; }
    public String getDescriptor() { return descriptor; }
    public boolean getIsLoggable() { return isLoggable; }
    public String getNameRenamed() { return nameRenamed; }

    public void setSignature(String value) { signature = value; }
    public void setName(String value) { name = value; }
    public void setDescriptor(String value) { descriptor = value; }
    public void setIsLoggable(boolean value) { isLoggable = value; }
    public void setNameRenamed(String value) { nameRenamed = value; }
}
