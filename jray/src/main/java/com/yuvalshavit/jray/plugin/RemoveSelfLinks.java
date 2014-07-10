package com.yuvalshavit.jray.plugin;

import com.yuvalshavit.jray.ConsumerAnalysis;
import com.yuvalshavit.jray.Graph;

import java.util.function.Consumer;

public final class RemoveSelfLinks implements Consumer<ConsumerAnalysis> {
  @Override
  public void accept(ConsumerAnalysis flows) {
    Graph graph = flows.getFlow();
    graph.getEdges().stream().filter(e -> e.from().equals(e.to())).forEach(graph::remove);
  }
}
