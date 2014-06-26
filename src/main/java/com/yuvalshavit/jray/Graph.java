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

public class Graph {
  private final Set<Node> nodes = new HashSet<>();
  private final Map<Relationship, Map<Node, Collection<Node>>> edges = new EnumMap<>(Relationship.class);
  private final Map<Node, EnumSet<NodeAttribute>> nodeAttributes = new HashMap<>();

  public Set<Edge> getEdges() {
    Set<Edge> allEdges = new HashSet<>();
    for (Map.Entry<Relationship, Map<Node, Collection<Node>>> edgesForRelationship : edges.entrySet()) {
      Relationship relationship = edgesForRelationship.getKey();
      for (Map.Entry<Node, Collection<Node>> edgeEntry : edgesForRelationship.getValue().entrySet()) {
        Node from = edgeEntry.getKey();
        edgeEntry.getValue().stream().map(to -> new Edge(from, relationship, to)).forEach(allEdges::add);
      }
    }
    return allEdges;
  }

  public Set<Node> getNodes() {
    return nodes;
  }

  public void add(Node from, Relationship relationship, Node to) {
    Map<Node, Collection<Node>> edgesForRelationship = edges.get(relationship);
    if (edgesForRelationship == null) {
      edgesForRelationship = new HashMap<>();
      edges.put(relationship, edgesForRelationship);
    }
    Collection<Node> edgesForFromNode = edgesForRelationship.get(from);
    if (edgesForFromNode == null) {
      edgesForFromNode = new HashSet<>();
      edgesForRelationship.put(from, edgesForFromNode);
    }
    edgesForFromNode.add(to);
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
