package com.yuvalshavit.jray.test;

public class SimpleFlowTest extends AnalysisTestBase {
  @Override
  protected void expectEdges(EdgeBuilder builder) {

  }

  public static class Payload {

  }

  public static class From {
    public Payload create() {
      throw new UnsupportedOperationException();
    }
  }

  public static class To {
    public To(Payload input) {
      throw new UnsupportedOperationException();
    }
  }
}
