package homework.proxy;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.Method;

import java.util.ArrayList;

public class CustomClassVisitor extends ClassVisitor {

    public CustomClassVisitor(final int api, final ClassVisitor classVisitor) {
        super(api, classVisitor);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        Method method = new Method(name, descriptor);

        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        CustomMethodVisitor cmv = new CustomMethodVisitor(mv, method);

        return cmv;
        //return super.visitMethod(access, name, descriptor, signature, exceptions);
    }
}
