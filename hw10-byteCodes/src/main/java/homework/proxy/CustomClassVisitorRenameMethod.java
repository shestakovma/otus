package homework.proxy;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

public class CustomClassVisitorRenameMethod extends ClassVisitor {

    public ArrayList<MethodInfo> methods = new ArrayList<MethodInfo>();

    public CustomClassVisitorRenameMethod(final int api, final ClassVisitor classVisitor, ArrayList<MethodInfo> methods) {
        super(api, classVisitor);
        this.methods = methods;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String methodSignature = name + descriptor;
        if (methods != null && methods.stream().filter(methodInfo -> methodInfo.getSignature().equals(methodSignature)).count() > 0) {
            MethodInfo info = (MethodInfo)methods.stream().filter(methodInfo -> methodInfo.getSignature().equals(methodSignature)).toArray()[0];
            if (info.getIsLoggable()) {
                info.setNameRenamed(name + "Proxied");
                return super.visitMethod(access, info.getNameRenamed(), descriptor, signature, exceptions);
            }
        }
        return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
