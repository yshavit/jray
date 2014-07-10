package com.yuvalshavit.jray.plugin;

import com.yuvalshavit.jray.Graph;

import java.util.function.Consumer;

public final class RemoveSelfLinks implements Consumer<Graph> {
  @Override
  public void accept(Graph graph) {
    graph.getEdges().stream().filter(e -> e.from().equals(e.to())).forEach(graph::remove);
  }
}
