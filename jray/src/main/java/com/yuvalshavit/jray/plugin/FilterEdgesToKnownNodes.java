package com.yuvalshavit.jray.plugin;

import com.yuvalshavit.jray.FlowAnalyzer;
import com.yuvalshavit.jray.Graph;
import com.yuvalshavit.jray.node.Node;
import com.yuvalshavit.jray.node.Edge;
import com.yuvalshavit.util.CommonPackageFinder;

import java.util.function.Consumer;

public class FilterEdgesToKnownNodes implements Consumer<FlowAnalyzer> {
  @Override
  public void accept(FlowAnalyzer flows) {
    Graph graph = flows.getFlow();
    String commonPackage = CommonPackageFinder.get(flows.getScanner().getExplicitlySeenNodes(), Node::toString);
    if (!commonPackage.endsWith(".")) {
      int lastDot = commonPackage.lastIndexOf('.');
      if (lastDot < 0) {
        commonPackage = "";
      } else {
        commonPackage = commonPackage.substring(0, lastDot + 1);
      }
    }

    if (!commonPackage.isEmpty()) {
      for (Edge edge : graph.getEdges()) {
        if (!(nodeIsInPackage(edge.from(), commonPackage) && nodeIsInPackage(edge.to(), commonPackage))) {
          graph.remove(edge);
        }
      }
    }
  }

  private static boolean nodeIsInPackage(Node n, String packageName) {
    return n.getClassName().startsWith(packageName);
  }
}
