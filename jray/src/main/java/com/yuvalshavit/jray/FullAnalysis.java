package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Edge;

import java.util.Set;

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
}
