package com.yuvalshavit.jray.test;

public class HolderIsProducerTest extends AnalysisTestBase {
  @Override
  protected void expectEdges(EdgeBuilder builder) {
    builder
      .add(Holder.class, Payload.class);
  }

  public interface Payload {
  }

  @SuppressWarnings("unused")
  public static class Holder {
    private final Payload input;

    public Holder(Payload input) {
      this.input = input;
    }

    public Payload get() {
      return input;
    }
  }
}
