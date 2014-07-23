package com.yuvalshavit.jray.test;

public class HolderIsntConsumerTest extends AnalysisTestBase {
  @Override
  protected void expectEdges(EdgeBuilder builder) {
    builder.none();
  }

  @SuppressWarnings("unused")
  public interface Payload {
    void deliver();
  }

  @SuppressWarnings("unused")
  public static class Consumer {
    private final Payload input;
    public Consumer(Payload input) {
      this.input = input;
    }
  }
}
