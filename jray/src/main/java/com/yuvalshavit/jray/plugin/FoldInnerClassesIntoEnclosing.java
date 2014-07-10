package com.yuvalshavit.jray.plugin;

import com.yuvalshavit.jray.Graph;
import com.yuvalshavit.jray.node.Node;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public final class FoldInnerClassesIntoEnclosing implements Consumer<Graph> {

  private final Graph enclosures;

  public FoldInnerClassesIntoEnclosing(Graph enclosures) {
    this.enclosures = enclosures;
  }

  @Override
  public void accept(Graph flow) {
    flow.getEdges().forEach(edge -> {
      Node fromEnclosing = getEnclosingClass(edge.from(), enclosures);
      Node toEnclosing = getEnclosingClass(edge.to(), enclosures);
      flow.remove(edge);
      if (!toEnclosing.equals(fromEnclosing)) {
        flow.add(fromEnclosing, toEnclosing);
      }
    });
  }

  private static Node getEnclosingClass(Node from, Graph graph) {
    // TODO memoize
    Set<Node> tos = graph.getSuccessors(from);
    if (tos.isEmpty()) {
      return from;
    }
    Iterator<Node> iter = tos.iterator();
    Node to = iter.next();
    if (iter.hasNext()) {
      throw new IllegalStateException("multiple outgoing edges from " + from + ": " + tos);
    }
    return getEnclosingClass(to, graph);
  }
}
