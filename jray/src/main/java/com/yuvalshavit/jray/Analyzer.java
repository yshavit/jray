package com.yuvalshavit.jray;

import com.yuvalshavit.jray.plugin.FilterEdgesToKnownNodes;
import com.yuvalshavit.jray.plugin.FindUndirected;
import com.yuvalshavit.jray.plugin.FoldInnerClassesIntoEnclosing;
import com.yuvalshavit.jray.plugin.RemoveSelfLinks;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;

public class Analyzer {

  private Analyzer() {}

  public static FullAnalysis analyze(ClassFinder classes) {
    Scanner scanner = new Scanner();

    runStep(classes, scanner);
    scanner.accept(new FilterEdgesToKnownNodes(scanner.getExplicitlySeenNodes()));
    scanner.accept(new FoldInnerClassesIntoEnclosing(scanner.getEnclosures()));

    ConsumerAnalysis analysis = new ConsumerAnalysis(scanner);
    runStep(classes, analysis);
    new RemoveSelfLinks().accept(analysis.getFlow());

    FindUndirected findUndirected = new FindUndirected();
    findUndirected.accept(analysis.getFlow());

    return new FullAnalysis(analysis.getFlow().getEdges(), findUndirected.getUndirected());
  }

  private static void runStep(ClassFinder classes, ClassVisitor visitor) {
    try {
      classes.forEach(cr -> cr.accept(visitor, 0));
    } catch (IOException e) {
      throw new AnalysisException(e);
    }
  }

  public static class AnalysisException extends RuntimeException {
    public AnalysisException(Throwable cause) {
      super(cause);
    }
  }
}
