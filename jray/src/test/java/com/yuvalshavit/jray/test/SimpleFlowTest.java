package com.yuvalshavit.jray.test;

public class SimpleFlowTest extends AnalysisTestBase {
  @Override
  protected void expectEdges(EdgeBuilder builder) {
    builder
      .add(Producer.class, Payload.class)
      .add(Consumer.class, Payload.class);
  }

  public interface Payload {
    void deliver();
  }

  public static class Producer {
    public Payload create() {
      throw new UnsupportedOperationException();
    }
  }

  public static class Consumer {
    public Consumer(Payload input) {
      input.deliver();
    }
  }
}
