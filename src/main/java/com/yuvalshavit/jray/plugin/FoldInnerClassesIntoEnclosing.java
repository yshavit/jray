package com.yuvalshavit.jray.plugin;

import com.yuvalshavit.jray.Graph;
import com.yuvalshavit.jray.node.Node;
import com.yuvalshavit.jray.node.Relationship;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public final class FoldInnerClassesIntoEnclosing implements Consumer<Graph> {

  @Override
  public void accept(Graph graph) {
    graph.getEdges().stream().filter(e -> e.relationship() != Relationship.ENCLOSED_BY).forEach(edge -> {
      Node fromEnclosing = getEnclosingClass(edge.from(), graph);
      if (!edge.from().equals(fromEnclosing)) {
        graph.remove(edge);
        graph.add(fromEnclosing, edge.relationship(), edge.to());
      }
    });
  }

  private Node getEnclosingClass(Node from, Graph graph) {
    // TODO memoize
    Set<Node> tos = graph.getSuccessors(from, Relationship.ENCLOSED_BY);
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
