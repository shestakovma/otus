package homework.proxy;

import homework.annotations.Log;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

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
                    return addProxyMethod(classfileBuffer, classBeingRedefined);
                }
                return classfileBuffer;
            }
        });

    }

    private static byte[] addProxyMethod(byte[] originalClass, Class<?> classInitial) {
        ArrayList<MethodInfo> methods = new ArrayList<>();

        var cr = new ClassReader(originalClass);
        var cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cvAnnotation = new CustomClassVisitorGetAnnotation(Opcodes.ASM5, cw, methods);
        cr.accept(cvAnnotation, Opcodes.ASM5);

        cr = new ClassReader(originalClass);
        cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS);
        ClassVisitor cvRename = new CustomClassVisitorRenameMethod(Opcodes.ASM5, cw, methods);
        cr.accept(cvRename, Opcodes.ASM5);

        for (var method : methods) {
            if (!method.getIsLoggable())
                continue;
            int paramCount = getParamCount(method.getDescriptor());
            String signatureParamsOnly = getSignatureParamsOnly(method.getDescriptor());

            System.out.println(method.getName() + ", " + method.getDescriptor() + ", " + method.getSignature() + ", " + method.getIsLoggable() + ", " + paramCount);

            MethodVisitor mv = cw.visitMethod(Opcodes.ACC_PUBLIC, method.getName(), method.getDescriptor(), null, null);

            var handle = new Handle(
                    H_INVOKESTATIC,
                    Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                    "makeConcatWithConstants",
                    MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                    false);

            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            for (int i = 1; i <= paramCount; i++) {
                mv.visitVarInsn(Opcodes.ALOAD, i);
            }

            mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(" + signatureParamsOnly + ")Ljava/lang/String;", handle, "called method: " + method.getName() + ", logged param:\u0001".repeat(paramCount));
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);


            mv.visitVarInsn(Opcodes.ALOAD, 0);
            for (int i = 1; i <= paramCount; i++) {
                mv.visitVarInsn(Opcodes.ALOAD, i);
            }
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "homework/proxy/MyClassImpl", method.getNameRenamed(), method.getDescriptor(), false);

            mv.visitInsn(Opcodes.RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
        }
        byte[] finalClass = cw.toByteArray();

        try (OutputStream fos = new FileOutputStream("proxyASM.class")) {
            fos.write(finalClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("asm finished");
        return finalClass;
    }

    private static int getParamCount(String signatureFull) {
        int index = signatureFull.indexOf(")");
        int result = 0;
        if (index > 0) {
            String s = signatureFull.substring(1, index);
            if (s.indexOf(";") >= 0) {
                String[] paramTypes = s.split(";");
                result = paramTypes.length;
            }
        }
        return result;
    }

    private static String getSignatureParamsOnly(String signatureFull) {
        int index = signatureFull.indexOf(")");
        String result = "";
        if (index > 0) {
            result = signatureFull.substring(1, index);
        }
        return result;
    }
}
