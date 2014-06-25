package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Node;
import com.yuvalshavit.jray.node.Edge;

import java.util.HashSet;
import java.util.Set;

public class Graph {
  private final Set<Node> nodes = new HashSet<>();
  private final Set<Edge> edges = new HashSet<>();

  public Set<Edge> getEdges() {
    return edges;
  }

  public Set<Node> getNodes() {
    return nodes;
  }

  public void add(Edge edge) {
    edges.add(edge);
  }

  public void add(Node node) {
    nodes.add(node);
  }
}
