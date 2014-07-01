package com.yuvalshavit.jray;

import com.yuvalshavit.jray.node.Edge;
import com.yuvalshavit.jray.node.Node;
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

  public Scanner read() throws IOException {
    Scanner scanner = new Scanner();
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
    return scanner;
  }

  public static void main(String[] filePaths) {
    List<Consumer<Scanner>> graphModifiers = Arrays.asList(
      new FilterEdgesToKnownNodes(),
      new RemoveSelfLinks(),
      new FoldInnerClassesIntoEnclosing());

    for (String filePath : filePaths) {
      File file = new File(filePath);
      if (!file.isFile()) {
        System.err.println("No such file: " + filePath);
      } else {
        try {
          Scanner scanner = new JarReader(file).read();
          for (Consumer<Scanner> modifier : graphModifiers) {
            modifier.accept(scanner);
          }
//          new TreeSet<>(graph.getEdges()).forEach(System.out::println);
//          new TreeSet<>(graph.getNodes()).forEach(System.out::println);
//          System.out.printf("%d nodes, %d edges%n", graph.getNodes().size(), graph.getEdges().size());
          printDotFile(file.getName(), scanner.getFlow().getEdges(), System.out);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  private static void printDotFile(String name, Set<Edge> edges, PrintStream out) throws IOException {
    out.printf("digraph \"%s\" {%n", name);
    edges.stream()
      .sorted()
      .forEach(e -> {
        Node from = e.from();
        Node to = e.to();
        out.printf("  \"%s\" -> \"%s\";%n",
                               from.getSimpleClassName(),
                               to.getSimpleClassName());
      });
    out.println("}");
  }
}
