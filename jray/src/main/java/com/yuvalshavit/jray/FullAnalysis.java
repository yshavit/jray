package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Edge;

import java.util.Set;
import java.util.TreeSet;

public class FullAnalysis {
  private final Set<Edge> flows;
  private final Set<Edge> couplings;

  public FullAnalysis(Set<Edge> flows, Set<Edge> couplings) {
    this.flows = flows;
    this.couplings = couplings;
  }

  public Set<Edge> getFlows() {
    return flows;
  }

  public Set<Edge> getCouplings() {
    return couplings;
  }

  public Set<Edge> getAllEdges() {
    Set<Edge> all = new TreeSet<>();
    all.addAll(flows);
    all.addAll(couplings);
    couplings.forEach(edge -> all.add(new Edge(edge.to(), edge.from())));
    return all;
  }
}
