package com.yuvalshavit.jray;

import com.yuvalshavit.jray.plugin.FilterEdgesToKnownNodes;
import org.objectweb.asm.ClassReader;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;
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
    List<Consumer<Graph>> graphModifiers = Arrays.asList(new FilterEdgesToKnownNodes());


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
          graph.getEdges().forEach(System.out::println);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }
}
