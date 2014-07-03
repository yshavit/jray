package com.yuvalshavit.jray.plugin;

import com.yuvalshavit.jray.Graph;
import com.yuvalshavit.jray.Scanner;

import java.util.function.Consumer;

public final class RemoveSelfLinks implements Consumer<Scanner> {
  @Override
  public void accept(Scanner scanner) {
    Graph graph = scanner.getFlow();
    graph.getEdges().stream().filter(e -> e.from().equals(e.to())).forEach(graph::remove);
  }
}
