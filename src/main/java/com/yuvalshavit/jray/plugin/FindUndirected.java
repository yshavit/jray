package com.yuvalshavit.jray.plugin;

import com.yuvalshavit.jray.Graph;
import com.yuvalshavit.jray.Scanner;
import com.yuvalshavit.jray.node.Edge;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class FindUndirected implements Consumer<Scanner> {

  private final Set<Edge> undirected = new HashSet<>();

  @Override
  public void accept(Scanner scanner) {
    Graph flow = scanner.getFlow();
    flow.getEdges().stream()
      .filter(edge -> flow.hasEdge(edge.to(), edge.from()))
      .map(this::normalize)
      .forEach(undirected::add);
    undirected.forEach(e -> {
      flow.remove(e);
      flow.remove(reverse(e));
    });
  }

  private Edge normalize(Edge edge) {
    int cmp = edge.from().compareTo(edge.to());
    return cmp <= 0
      ? edge
      : reverse(edge);
  }

  private static Edge reverse(Edge edge) {
    return new Edge(edge.to(), edge.from());
  }

  public Set<Edge> getUndirected() {
    return undirected;
  }
}
