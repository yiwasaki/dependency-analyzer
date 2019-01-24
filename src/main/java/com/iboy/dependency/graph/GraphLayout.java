package com.iboy.dependency.graph;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iboy.dependency.model.DependencyRelation;

import guru.nidi.graphviz.attribute.RankDir;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.engine.GraphvizCmdLineEngine;
import guru.nidi.graphviz.model.Factory;
import guru.nidi.graphviz.model.Graph;
import guru.nidi.graphviz.model.Node;

public class GraphLayout {
	public void layout(Path outputPath, DependencyGraph dg) throws IOException {
		Collection<Node> nodes = createNodes(dg);
		Graph graph = Factory.graph("example").directed()
							 .graphAttr().with(RankDir.LEFT_TO_RIGHT)
						        .with(
						        	nodes.toArray(new Node[1])
						        );
		Graphviz.useEngine(List.of(new GraphvizCmdLineEngine()));

		// TODO: PNGで出力しようとするとライブラリ側の問題でうまく処理できない。
		Graphviz.fromGraph(graph).height(100).render(Format.SVG).toFile(outputPath.toFile());
	}

	/**
	 *
	 * @param graph
	 * @return
	 */
	private Collection<Node> createNodes(DependencyGraph graph) {
		Map<String, Node> nodeMap = new HashMap<>();
		for (DependencyRelation edge : graph.getRelations()) {
			if (!nodeMap.containsKey(edge.getFrom().toString())) {
				nodeMap.put(edge.getFrom().toString(), Factory.node(edge.getFrom().toString()));
			}
			if (!nodeMap.containsKey(edge.getTo().toString())) {
				nodeMap.put(edge.getTo().toString(), Factory.node(edge.getTo().toString()));
			}
		}

		for (DependencyRelation edge : graph.getRelations()) {
			 Node node = nodeMap.get(edge.getFrom().toString());
			 node = node.link(nodeMap.get(edge.getTo().toString()));
			 // 意図的にマップを上書きすることで、情報をアップデートする
			 nodeMap.put(edge.getFrom().toString(), node);
		}
		return nodeMap.values();
	}

}
