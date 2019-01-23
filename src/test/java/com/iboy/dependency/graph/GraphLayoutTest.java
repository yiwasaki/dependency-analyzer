package com.iboy.dependency.graph;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.iboy.dependency.analysis.MavenAnalyzer;
import com.iboy.dependency.model.DependencyRelation;
import com.iboy.dependency.test.util.FileCompare;

class GraphLayoutTest extends GraphLayout {

	@Test
	void グラフのレイアウトが正しく実行できることを確認する() throws IOException {
		MavenAnalyzer analyzer = new MavenAnalyzer();

		// 空のパスで絶対に存在しないファイルパスを作成する
		Path p = FileSystems.getDefault().getPath("src", "test", "resources", "layout-test", "pom.xml");

		List<DependencyRelation> relations = analyzer.analyze(p);
		DependencyGraph graph = new DependencyGraph(relations);

		GraphLayout layout = new GraphLayout();
		Path p2 = FileSystems.getDefault().getPath("src", "test", "resources", "layout-test", "actual.svg");
		layout.layout(p2, graph);

		assertTrue(FileCompare.fileCompare(p2, FileSystems.getDefault().getPath("src", "test", "resources", "layout-test", "expected.svg")));
	}


}
