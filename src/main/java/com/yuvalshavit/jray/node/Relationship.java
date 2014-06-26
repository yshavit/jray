package com.yuvalshavit.jray.node;

public enum Relationship {
  IS_A, IMPLEMENTS, USES, BUILDS, ENCLOSED_BY;

  @Override
  public String toString() {
    return name().replace('_', ' ').toLowerCase();
  }

}
