package com.yuvalshavit.jray.test;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import com.yuvalshavit.jray.Analyzer;
import com.yuvalshavit.jray.ClassFinder;
import com.yuvalshavit.jray.FullAnalysis;
import com.yuvalshavit.jray.node.Edge;
import com.yuvalshavit.jray.node.Node;
import org.objectweb.asm.ClassReader;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Set;
import java.util.TreeSet;

import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertEquals;

abstract class AnalysisTestBase {
  protected abstract void expectEdges(EdgeBuilder builder);

  @Test
  public void runAnalysis() throws IOException {
    String packageLoc = getClass().getPackage().getName().replace('.', '/');
    URL packageUrl = Resources.getResource(packageLoc);
    String classPrefix = getClass().getSimpleName() + "$";
    String classSuffix = ".class";

    ClassFinder classFinder = consumer -> Resources.readLines(packageUrl, Charsets.UTF_8)
      .stream()
      .filter(s -> s.startsWith(classPrefix) && s.endsWith(classSuffix))
      .map(name -> packageLoc + '/' + name)
      .map(Resources::getResource)
      .forEach(url -> {
        try (InputStream is = url.openStream()) {
          ClassReader reader = new ClassReader(is);
          consumer.accept(reader);
        } catch (IOException e) {
          throw new AssertionError(e);
        }
      });
    FullAnalysis analysis = Analyzer.analyze(classFinder, new Node(getClass().getName()));

    EdgeBuilder edgeBuilder = new EdgeBuilder();
    expectEdges(edgeBuilder);
    if (edgeBuilder.edges.isEmpty()) {
      assertTrue(edgeBuilder.explicitlyNone, "no edges provided, but neither was none() called");
    }
    Set<Edge> expected = edgeBuilder.edges;
    assertEquals(expected, analysis.getAllEdges());
  }

  protected static class EdgeBuilder {
    private Set<Edge> edges = new TreeSet<>();
    private boolean explicitlyNone;

    public EdgeBuilder add(Class<?> from, Class<?> to) {
      Node fromNode = new Node(from.getName());
      Node toNode = new Node(to.getName());
      return add(new Edge(fromNode, toNode));
    }

    public EdgeBuilder add(Edge edge) {
      Preconditions.checkState(!explicitlyNone, "can't call add(..) after none()");
      edges.add(edge);
      return this;
    }

    public void none() {
      explicitlyNone = true;
    }
  }
}
