package com.yuvalshavit.jray.node;

public enum Relationship {
  CONSUMES, PRODUCES, ENCLOSED_BY;

  @Override
  public String toString() {
    return name().replace('_', ' ').toLowerCase();
  }

  public boolean isUsageFlow() {
    return this == CONSUMES || this == PRODUCES;
  }
}
