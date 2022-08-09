package homework.proxy;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;

import java.util.ArrayList;

public class CustomClassVisitorGetAnnotation extends ClassVisitor {

    public ArrayList<MethodInfo> methods = new ArrayList<MethodInfo>();

    public CustomClassVisitorGetAnnotation(final int api, final ClassVisitor classVisitor, ArrayList<MethodInfo> methods) {
        super(api, classVisitor);
        this.methods = methods;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String methodSignature = name + descriptor;
        methods.add(new MethodInfo(methodSignature, name, descriptor));
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        CustomMethodVisitor cmv = new CustomMethodVisitor(mv, methodSignature, methods);

        return cmv;
        //return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
