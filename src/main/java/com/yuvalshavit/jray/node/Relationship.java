package com.yuvalshavit.jray.node;

public enum Relationship {
  IS_A, IMPLEMENTS, CONSUMES, PRODUCES, ENCLOSED_BY;

  @Override
  public String toString() {
    return name().replace('_', ' ').toLowerCase();
  }

}
