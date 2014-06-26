package com.yuvalshavit.jray.node;

public enum Relationship {
  IS_A, IMPLEMENTS, CONSUMES, PRODUCES, ENCLOSED_BY;

  @Override
  public String toString() {
    return name().replace('_', ' ').toLowerCase();
  }

  public boolean isUsageFlow() {
    return this == CONSUMES || this == PRODUCES;
  }
}
