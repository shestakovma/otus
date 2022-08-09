package homework.proxy;

import org.objectweb.asm.*;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class CustomMethodVisitor extends MethodVisitor {
    private String methodSignature = "";
    public ArrayList<MethodInfo> methods = new ArrayList<MethodInfo>();

    public CustomMethodVisitor(MethodVisitor mv, String methodSignature, ArrayList<MethodInfo> methods) {
        super(Opcodes.ASM5, mv);
        this.methodSignature = methodSignature;
        this.methods = methods;
    }
    public CustomMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        if (descriptor.equals(Agent.annotationLogName)) {
            if (methods != null && methods.stream().filter(methodInfo -> methodInfo.getSignature().equals(methodSignature)).count() > 0
            ) {
                MethodInfo info = (MethodInfo)methods.stream().filter(methodInfo -> methodInfo.getSignature().equals(methodSignature)).toArray()[0];
                info.setIsLoggable(true);
            }

        }
        return super.visitAnnotation(descriptor, visible);
    }
}
