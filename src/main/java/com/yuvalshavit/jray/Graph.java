package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Node;
import com.yuvalshavit.jray.node.Edge;
import com.yuvalshavit.jray.node.Relationship;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph {
  private final Set<Node> nodes = new HashSet<>();
  private final Map<Relationship, Set<Edge>> edges = new EnumMap<>(Relationship.class);

  public Set<Edge> getEdges() {
    return edges.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
  }

  public Set<Node> getNodes() {
    return nodes;
  }

  public void add(Edge edge) {
    Set<Edge> edgeSet = edges.get(edge.relationship());
    if (edgeSet == null) {
      edgeSet = new HashSet<>();
      edges.put(edge.relationship(), edgeSet);
    }
    edgeSet.add(edge);
  }

  public void add(Node node) {
    nodes.add(node);
  }
}
