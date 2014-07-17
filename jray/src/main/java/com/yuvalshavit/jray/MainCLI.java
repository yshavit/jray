package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Edge;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Set;

public class MainCLI {
  private MainCLI() {}


  public static void main(String[] filePaths) {
    for (String filePath : filePaths) {
      File file = new File(filePath);
      if (!file.isFile()) {
        System.err.println("No such file: " + filePath);
      } else {
        try {
          JarReader jarReader = new JarReader(file);
          FullAnalysis analysis = Analyzer.analyze(jarReader);
          printDotFile(file.getName(), analysis.getFlows(), analysis.getCouplings(), System.out);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static void printDotFile(String name, Set<Edge> directed, Set<Edge> undirected, PrintStream out) throws IOException {
    out.printf("digraph \"%s\" {%n", name);
    out.println("  subgraph oneDirectional {");
    directed.stream()
      .sorted()
      .forEach(e -> out.printf("    \"%s\" -> \"%s\";%n", e.from().getSimpleClassName(), e.to().getSimpleClassName()));
    out.println("  }");
    out.println("  subgraph twoDirectional {");
    out.println("  edge [dir=both, arrowhead=dot, arrowtail=dot, color=red];");
    undirected.stream()
      .sorted()
      .forEach(e -> out.printf("    \"%s\" -> \"%s\";%n", e.from().getSimpleClassName(), e.to().getSimpleClassName()));
    out.println("  }");
    out.println("}");
  }
}
