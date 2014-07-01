package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Node;
import com.yuvalshavit.jray.node.Relationship;
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

  private final Graph graph;
  private Node visiting;


  public Scanner(Graph graph) {
    super(Opcodes.ASM5);
    this.graph = graph;
  }

  @Override
  public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
    visiting = node(name);
    graph.add(visiting);
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
    if ((Opcodes.ACC_PUBLIC & access) != 0) {
      Type t = Type.getMethodType(desc);
      handleIfRef(t.getReturnType(), to -> link(visiting, Relationship.FLOW, to));
      for (Type argType : t.getArgumentTypes()) {
        handleIfRef(argType, argNode -> link(argNode, Relationship.FLOW, visiting));
      }
    }
    return null;
  }

  @Override
  public void visitInnerClass(String name, String outerName, String innerName, int access) {
    if (outerName != null) {
      Node outer = node(outerName);
      Node inner = node(name);
      link(inner, Relationship.ENCLOSED_BY, outer);
    }
  }

  @Override
  public void visitOuterClass(String owner, String name, String desc) {
    link(visiting, Relationship.ENCLOSED_BY, node(owner));
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

  private void link(Node from, Relationship relationship, Node to) {
    graph.add(from, relationship, to);
  }
}
