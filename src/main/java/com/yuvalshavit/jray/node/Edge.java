package com.yuvalshavit.jray.node;

public class Edge implements Comparable<Edge> {
  private final Node from;
  private final Relationship relationship;
  private final Node to;

  public Edge(Node from, Relationship relationship, Node to) {
    this.from = from;
    this.relationship = relationship;
    this.to = to;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Edge edge = (Edge) o;
    return from.equals(edge.from) && relationship == edge.relationship && to.equals(edge.to);

  }

  @Override
  public int hashCode() {
    int result = from.hashCode();
    result = 31 * result + relationship.hashCode();
    result = 31 * result + to.hashCode();
    return result;
  }

  @Override
  public String toString() {
    return String.format("%s %s %s", from, relationship, to);
  }

  @Override
  public int compareTo(Edge o) {
    int cmp = from.compareTo(o.from);
    if (cmp != 0) {
      return cmp;
    }
    cmp = relationship.compareTo(o.relationship);
    if (cmp != 0) {
      return cmp;
    }
    return to.compareTo(o.to);
  }

  public Node from() {
    return from;
  }

  public Node to() {
    return to;
  }
}
