package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Edge;
import com.yuvalshavit.jray.plugin.FilterEdgesToKnownNodes;
import com.yuvalshavit.jray.plugin.FindUndirected;
import com.yuvalshavit.jray.plugin.FoldInnerClassesIntoEnclosing;
import com.yuvalshavit.jray.plugin.RemoveSelfLinks;
import org.objectweb.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class JarReader {
  private final File zipFile;

  public JarReader(File zipFile) {
    this.zipFile = zipFile;
  }

  public void read(Consumer<ClassReader> classReaderConsumer) throws IOException {
    try (FileInputStream fileStreamRaw = new FileInputStream(zipFile);
         BufferedInputStream fileStream = new BufferedInputStream(fileStreamRaw);
         ZipInputStream zipStream = new ZipInputStream(fileStream))
    {
      for (ZipEntry entry = zipStream.getNextEntry(); entry != null; entry = zipStream.getNextEntry()) {
        if (entry.getName().endsWith(".class")) {
          ClassReader classReader = new ClassReader(zipStream);
          classReaderConsumer.accept(classReader);
        }
      }
    }
  }

  public static void main(String[] filePaths) {

    for (String filePath : filePaths) {
      File file = new File(filePath);
      if (!file.isFile()) {
        System.err.println("No such file: " + filePath);
      } else {
        try {
          JarReader jarReader = new JarReader(file);

          Scanner scanner = new Scanner();
          jarReader.read(cr -> cr.accept(scanner, 0));
          scanner.accept(new FilterEdgesToKnownNodes(scanner.getExplicitlySeenNodes()));
          scanner.accept(new FoldInnerClassesIntoEnclosing(scanner.getEnclosures()));

          ConsumerAnalysis analysis = new ConsumerAnalysis(scanner);
          jarReader.read(cr -> cr.accept(analysis, 0));
          new RemoveSelfLinks().accept(analysis.getFlow());
          FindUndirected findUndirected = new FindUndirected();
          findUndirected.accept(analysis.getFlow());

//          new TreeSet<>(graph.getEdges()).forEach(System.out::println);
//          new TreeSet<>(graph.getNodes()).forEach(System.out::println);
//          System.out.printf("%d nodes, %d edges%n", graph.getNodes().size(), graph.getEdges().size());
          printDotFile(file.getName(), analysis.getFlow().getEdges(), findUndirected.getUndirected(), System.out);
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
