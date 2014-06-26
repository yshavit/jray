package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Edge;
import com.yuvalshavit.jray.node.Relationship;
import com.yuvalshavit.jray.plugin.FilterEdgesToKnownNodes;
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

  public Graph read() throws IOException {
    Graph graph = new Graph();
    Scanner scanner = new Scanner(graph);
    try (FileInputStream fileStreamRaw = new FileInputStream(zipFile);
         BufferedInputStream fileStream = new BufferedInputStream(fileStreamRaw);
         ZipInputStream zipStream = new ZipInputStream(fileStream))
    {
      for (ZipEntry entry = zipStream.getNextEntry(); entry != null; entry = zipStream.getNextEntry()) {
        if (entry.getName().endsWith(".class")) {
          ClassReader classReader = new ClassReader(zipStream);
          classReader.accept(scanner, 0);
        }
      }
    }
    return graph;
  }

  public static void main(String[] filePaths) {
    List<Consumer<Graph>> graphModifiers = Arrays.asList(
      new FilterEdgesToKnownNodes(),
      new RemoveSelfLinks(),
      new FoldInnerClassesIntoEnclosing());

    for (String filePath : filePaths) {
      File file = new File(filePath);
      if (!file.isFile()) {
        System.err.println("No such file: " + filePath);
      } else {
        try {
          Graph graph = new JarReader(file).read();
          for (Consumer<Graph> modifier : graphModifiers) {
            modifier.accept(graph);
          }
//          new TreeSet<>(graph.getEdges()).forEach(System.out::println);
//          new TreeSet<>(graph.getNodes()).forEach(System.out::println);
//          System.out.printf("%d nodes, %d edges%n", graph.getNodes().size(), graph.getEdges().size());
          printDotFile(file.getName(), graph.getEdges(), System.out);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static void printDotFile(String name, Set<Edge> edges, PrintStream out) throws IOException {
    out.printf("digraph \"%s\" {%n", name);
    edges.stream()
      .filter(e -> e.relationship() == Relationship.CONSUMES || e.relationship() == Relationship.PRODUCES)
      .sorted()
      .forEach(e -> out.printf("  \"%s\" -> \"%s\" [label=\"%s\"];%n",
                               e.from().getSimpleClasssName(),
                               e.to().getSimpleClasssName(),
                               e.relationship().name().toLowerCase()));
    out.println("}");
  }
}
