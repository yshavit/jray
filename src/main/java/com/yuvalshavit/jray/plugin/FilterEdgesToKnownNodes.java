package com.yuvalshavit.jray.plugin;

import com.yuvalshavit.jray.Graph;
import com.yuvalshavit.jray.node.Node;
import com.yuvalshavit.jray.node.Edge;
import com.yuvalshavit.util.CommonPackageFinder;

import java.util.Iterator;
import java.util.function.Consumer;

public class FilterEdgesToKnownNodes implements Consumer<Graph> {
  @Override
  public void accept(Graph graph) {
    String commonPackage = CommonPackageFinder.get(graph.getNodes(), Node::toString);
    if (!commonPackage.endsWith(".")) {
      int lastDot = commonPackage.lastIndexOf('.');
      if (lastDot < 0) {
        commonPackage = "";
      } else {
        commonPackage = commonPackage.substring(0, lastDot + 1);
      }
    }

    if (!commonPackage.isEmpty()) {
      for (Iterator<Edge> iterator = graph.getEdges().iterator(); iterator.hasNext(); ) {
        Edge edge = iterator.next();
        if (!(nodeIsInPackage(edge.from(), commonPackage) && nodeIsInPackage(edge.to(), commonPackage))) {
          iterator.remove();
        }
      }
    }
  }

  private static boolean nodeIsInPackage(Node n, String packageName) {
    return n.getClassName().startsWith(packageName);
  }
}
