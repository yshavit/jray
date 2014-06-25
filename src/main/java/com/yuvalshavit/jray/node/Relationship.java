package com.yuvalshavit.jray.node;

public enum Relationship {
  IS_A, IMPLEMENTS, USES, BUILDS;

  @Override
  public String toString() {
    return name().replace('_', ' ').toLowerCase();
  }

}
