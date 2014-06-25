package com.yuvalshavit.jray.node;

public class Node implements Comparable<Node> {
  private final String className;

  public Node(String className) {
    if (className == null) {
      throw new IllegalArgumentException();
    }
    if (className.endsWith("[]")) {
      className = className.substring(0, className.length() - 2);
    }
    className = className.replace('/', '.');
    this.className = className;
  }

  @Override
  public String toString() {
    return className;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Node node = (Node) o;
    return className.equals(node.className);

  }

  @Override
  public int hashCode() {
    return className.hashCode();
  }

  @Override
  public int compareTo(Node o) {
    return className.compareTo(o.className);
  }

  public String getClassName() {
    return className;
  }
}
