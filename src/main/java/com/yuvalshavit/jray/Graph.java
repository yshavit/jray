package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Node;
import com.yuvalshavit.jray.node.Edge;
import com.yuvalshavit.jray.node.NodeAttribute;
import com.yuvalshavit.jray.node.Relationship;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {
  private final Set<Node> nodes = new HashSet<>();
  private final Map<Node, Map<Relationship, Set<Node>>> edges = new HashMap<>();
  private final Map<Node, EnumSet<NodeAttribute>> nodeAttributes = new HashMap<>();

  public Set<Edge> getEdges() {
    Stream<Edge> edgesStreams = edges.entrySet().stream().flatMap(entry -> {
      Node from = entry.getKey();
      return entry.getValue().entrySet().stream().flatMap(halfEdge -> {
        Relationship relationship = halfEdge.getKey();
        return halfEdge.getValue().stream().map(to -> new Edge(from, relationship, to));
      });
    });
    return edgesStreams.collect(Collectors.toSet());
  }

  public Set<Node> getSuccessors(Node from, Relationship relationship) {
    Map<Relationship, Set<Node>> outgoing = edges.get(from);
    if (outgoing == null) {
      return Collections.emptySet();
    }
    Set<Node> successors = outgoing.get(relationship);
    return successors != null
      ? Collections.unmodifiableSet(successors)
      : Collections.emptySet();
  }

  public Set<Node> getNodes() {
    return nodes;
  }

  public void add(Node from, Relationship relationship, Node to) {
    Map<Relationship, Set<Node>> outgoing = edges.get(from);
    if (outgoing == null) {
      outgoing = new EnumMap<>(Relationship.class);
      edges.put(from, outgoing);
    }
    Set<Node> toCollection = outgoing.get(relationship);
    if (toCollection == null) {
      toCollection = new HashSet<>();
      outgoing.put(relationship, toCollection);
    }
    toCollection.add(to);
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
