package homework.proxy;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.ArrayList;

public class Agent {
    private static final String innerClassName = "homework/proxy/MyClassImpl";
    private static final String innerClassNameReflection = "homework.proxy.MyClassImpl";
    public static final String annotationLogName = "Lhomework/annotations/Log;";

    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        inst.addTransformer(new ClassFileTransformer() {
            @Override
            public byte[] transform(ClassLoader loader, String className,
                                    Class<?> classBeingRedefined,
                                    ProtectionDomain protectionDomain,
                                    byte[] classfileBuffer) {
                if (className.equals(innerClassName)) {
                    return modifyClass(classfileBuffer, classBeingRedefined);
                }
                return classfileBuffer;
            }
        });

    }

    private static byte[] modifyClass(byte[] originalClass, Class<?> classInitial) {
        var cr = new ClassReader(originalClass);
        var cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cvAnnotation = new CustomClassVisitor(Opcodes.ASM5, cw);
        cr.accept(cvAnnotation, Opcodes.ASM5);

        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("proxyASM.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("asm finished");
        return finalClass;
    }
}
