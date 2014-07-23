package com.yuvalshavit.jray.test;

public class StoreThenUseTest extends AnalysisTestBase {
  @Override
  protected void expectEdges(EdgeBuilder builder) {
    builder
      .add(Consumer.class, Payload.class);
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

    public void use() {
      input.deliver();
    }
  }
}
