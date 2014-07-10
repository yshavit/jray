package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Node;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Set;

public class ConsumerAnalysis extends ClassVisitor {

  private final Scanner scanner;
  private final Graph flows = new Graph();
  private Node visiting;
  private Set<Node> inputs;

  public ConsumerAnalysis(Scanner scanner) {
    super(Opcodes.ASM5);
    this.scanner = scanner;
    scanner.getProducers().getEdges().forEach(flows::add);
  }

  /**
   * Node C is a consumer of various things. For each one of those inputs I, C can do one of a few things:
   *  1) invoke a method on I but not use the result (incl if the method returns void); assume the flow is C -> I
   *  2) invoke a method on I, and do use the result; assume the flow is I -> C
   *  3) pass I as an argument to someone else.
   *
   * Note that another case, of C returning I, is covered by the producer/consumer distinction, since it makes C a
   * producer of I. We're only interested in consumers here.
   *
   * The third case above is a bit tricky. It comes up if for instance: class A gives class B a reference of class C,
   * and class B (only) uses that reference to pass it to an object of type D. In this case, class B is essentially
   * just a transport. The flow we'd like to see is A -> B -> D, disregarding the actual "payload" C. We'd get this
   * for free by having A call the setting B (setting up A -> B) and B calling the setter on D (B -> D).
   */
  @Override
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    Node node = new Node(name);

    inputs = scanner.getConsumers().getSuccessors(node);
    visiting = inputs.isEmpty()
      ? null
      : node;
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
    if ((visiting != null) && (Opcodes.ACC_PUBLIC & access) != 0) {
      MethodNode methodNode = new MethodNode(access, name, desc, signature, exceptions);
      return new FlowMethodVisistor(methodNode);
    } else {
      return null;
    }
  }

  public Graph getFlow() {
    return flows;
  }

  public Scanner getScanner() {
    return scanner;
  }

  private class FlowMethodVisistor extends MethodVisitor {
    private final MethodNode node;

    FlowMethodVisistor(MethodNode node) {
      super(Opcodes.ASM5, node);
      this.node = node;
    }

    @Override
    public void visitEnd() {
      InsnList instructions = node.instructions;
      Node previousInstMethodOwner = null;
      for (int i = 0, max = instructions.size(); i < max; ++i) {
        AbstractInsnNode instruction = instructions.get(i);
        if (previousInstMethodOwner != null) {
          // The previous instruction was an invocation of a non-void method call.
          // Set up a flow visiting -> owner if we just pop the result, or else owner -> visiting.
          boolean isPop = (instruction instanceof InsnNode) && (instruction.getOpcode() == Opcodes.POP);
          if (isPop) {
            flows.add(visiting, previousInstMethodOwner);
          } else {
            flows.add(previousInstMethodOwner, visiting);
          }
          previousInstMethodOwner = null;
        }
        if (instruction instanceof MethodInsnNode) {
          MethodInsnNode methodInst = (MethodInsnNode) instruction;
          Node methodOwner = new Node(methodInst.owner);
          if (inputs.contains(methodOwner)) {
            Type t = Type.getMethodType(methodInst.desc);
            if ("void".equals(t.getReturnType().getClassName())) {
              flows.add(visiting, methodOwner);
            } else {
              previousInstMethodOwner = methodOwner;
            }
          }
        }
      }
    }
  }
}
