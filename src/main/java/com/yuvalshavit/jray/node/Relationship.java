package com.yuvalshavit.jray.node;

public enum Relationship {
  FLOW, ENCLOSED_BY;

  @Override
  public String toString() {
    return name().replace('_', ' ').toLowerCase();
  }

  public boolean isUsageFlow() {
    return this == FLOW;
  }
}
