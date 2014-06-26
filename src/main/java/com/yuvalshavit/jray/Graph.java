package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Node;
import com.yuvalshavit.jray.node.Edge;
import com.yuvalshavit.jray.node.NodeAttribute;
import com.yuvalshavit.jray.node.Relationship;

import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Graph {
  private final Set<Node> nodes = new HashSet<>();
  private final Map<Relationship, Set<Edge>> edges = new EnumMap<>(Relationship.class);
  private final Map<Node, EnumSet<NodeAttribute>> nodeAttributes = new HashMap<>();

  public Set<Edge> getEdges() {
    return edges.values().stream().flatMap(Collection::stream).collect(Collectors.toSet());
  }

  public Set<Node> getNodes() {
    return nodes;
  }

  public void add(Node from, Relationship relationship, Node to) {
    Edge edge = new Edge(from, relationship, to);
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

  public void addNodeAttribute(Node node, NodeAttribute attribute) {
    EnumSet<NodeAttribute> attributes = nodeAttributes.get(node);
    if (attributes == null) {
      nodeAttributes.put(node, EnumSet.of(attribute));
    } else {
      attributes.add(attribute);
    }
  }
}
