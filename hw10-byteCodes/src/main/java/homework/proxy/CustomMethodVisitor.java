package homework.proxy;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.Method;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

public class CustomMethodVisitor extends MethodVisitor {
    private boolean isLoggable = false;
    private Method currentMethod;

    public CustomMethodVisitor(MethodVisitor mv, Method currentMethod) {
        super(Opcodes.ASM5, mv);
        this.currentMethod = currentMethod;
    }

    @Override
    public AnnotationVisitor visitAnnotation(final String descriptor, final boolean visible) {
        if (descriptor.equals(Agent.annotationLogName)) {
            isLoggable = true;
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitCode()
    {
        if (isLoggable) {
            String signatureParamsOnly = getSignatureParamsOnly(currentMethod.getDescriptor());
            var handle = new Handle(
                    H_INVOKESTATIC,
                    Type.getInternalName(java.lang.invoke.StringConcatFactory.class),
                    "makeConcatWithConstants",
                    MethodType.methodType(CallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class, Object[].class).toMethodDescriptorString(),
                    false);

            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

            int paramCount = 0;
            int stackIndex = 1;
            for(Type arg : currentMethod.getArgumentTypes()) {
                System.out.println(arg);
                if (arg.getDescriptor().equals("J") || arg.getDescriptor().equals("D")) {
                    //long и double обрабатываем отдельно, т.к. они занимают по 2 слота в стеке
                    int opCode = arg.getDescriptor().equals("J") ? Opcodes.LLOAD : Opcodes.DLOAD;
                    super.visitVarInsn(opCode, stackIndex);
                    stackIndex++;
                }
                else if (arg.getDescriptor().equals("F")) {
                    //float
                    super.visitVarInsn(Opcodes.FLOAD, stackIndex);
                } else if(arg.getDescriptor().equals("I") || arg.getDescriptor().equals("Z")) {
                    //int, boolean
                    super.visitVarInsn(Opcodes.ILOAD, stackIndex);
                } else {
                    //ref
                    super.visitVarInsn(Opcodes.ALOAD, stackIndex);
                }
                stackIndex++;
                paramCount++;

            }

            mv.visitInvokeDynamicInsn("makeConcatWithConstants", "(" + signatureParamsOnly + ")Ljava/lang/String;", handle, "called method: " + currentMethod.getName() + ", logged param:\u0001".repeat(paramCount));
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        super.visitCode();
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
