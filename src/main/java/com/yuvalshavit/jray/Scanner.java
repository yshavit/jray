package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Node;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class Scanner extends ClassVisitor {

  private static final Set<String> primitives = new HashSet<>(Arrays.asList(
    "void", "byte", "char", "short", "int", "long", "float", "double", "boolean"));

  private final Graph flows = new Graph();
  private final Graph enclosures = new Graph();
  private final Set<Node> explicitlySeenNodes = new HashSet<>();
  private Node visiting;


  public Scanner() {
    super(Opcodes.ASM5);
  }

  public Graph getFlow() {
    return flows;
  }

  public Graph getEnclosures() {
    return enclosures;
  }

  public Set<Node> getExplicitlySeenNodes() {
    return explicitlySeenNodes;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    visiting = node(name);
    explicitlySeenNodes.add(visiting);
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
    if ((Opcodes.ACC_PUBLIC & access) != 0) {
      Type t = Type.getMethodType(desc);
      handleIfRef(t.getReturnType(), to -> link(flows, visiting, to));
      for (Type argType : t.getArgumentTypes()) {
        handleIfRef(argType, argNode -> link(flows, argNode, visiting));
      }
    }
    return null;
  }

  @Override
  public void visitInnerClass(String name, String outerName, String innerName, int access) {
    if (outerName != null) {
      Node outer = node(outerName);
      Node inner = node(name);
      link(enclosures, inner, outer);
    }
  }

  @Override
  public void visitOuterClass(String owner, String name, String desc) {
    link(enclosures, visiting, node(owner));
  }

  private void handleIfRef(Type type, Consumer<? super Node> action) {
    String className = type.getClassName();
    if (className != null) {
      if (!primitives.contains(className)) {
        action.accept(node(className));
      }
    }
  }

  private Node node(String name) {
    return new Node(name);
  }

  private void link(Graph graph, Node from, Node to) {
    graph.add(from, to);
  }
}
