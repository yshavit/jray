package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Node;
import com.yuvalshavit.jray.node.Edge;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Graph {
  private final Map<Node, Set<Node>> edges = new HashMap<>();

  public Set<Edge> getEdges() {
    Stream<Edge> edgesStreams = edges.entrySet().stream().flatMap(entry -> {
      Node from = entry.getKey();
      return entry.getValue().stream().flatMap(to -> Stream.of(new Edge(from, to)));
    });
    return edgesStreams.collect(Collectors.toSet());
  }

  public Set<Node> getSuccessors(Node from) {
    Set<Node> outgoing = edges.get(from);
    if (outgoing == null) {
      return Collections.emptySet();
    }
    return Collections.unmodifiableSet(outgoing);
  }

  public boolean hasEdge(Node from, Node to) {
    Set<Node> outgoing = edges.get(from);
    return outgoing != null && outgoing.contains(to);
  }

  public void add(Node from, Node to) {
    Set<Node> outgoing = edges.get(from);
    if (outgoing == null) {
      outgoing = new HashSet<>();
      edges.put(from, outgoing);
    }
    outgoing.add(to);
  }

  public void add(Edge edge) {
    add(edge.from(), edge.to());
  }

  public void remove(Edge edge) {
    Set<Node> outgoing = edges.get(edge.from());
    if (outgoing != null) {
      outgoing.remove(edge.to());
    }
  }
}
