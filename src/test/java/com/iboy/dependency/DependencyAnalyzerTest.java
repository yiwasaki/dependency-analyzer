package com.iboy.dependency;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import com.iboy.dependency.test.util.FileCompare;

class DependencyAnalyzerTest extends DependencyAnalyzer {

	@Test
	void 依存関係定義ファイルの読み込みを行って依存関係をSVGで出力できる() throws IOException {
		Path inputDir = FileSystems.getDefault().getPath(".");
		Path output = FileSystems.getDefault().getPath("src", "test", "resources", "maven-analyzer-test", "output.svg");

		DependencyAnalyzer da = new DependencyAnalyzer();
		da.analyze(inputDir, output);

		assertTrue(FileCompare.fileCompare(output, FileSystems.getDefault().getPath("src", "test", "resources", "maven-analyzer-test", "expected.svg")));

	}

	@Test
	void 依存関係ファイルの読み込みを行って依存関係をフィルターした結果を出力できる() throws IOException {
		Path inputDir = FileSystems.getDefault().getPath(".");
		Path output = FileSystems.getDefault().getPath("src", "test", "resources", "maven-analyzer-test", "output-filter.svg");

		DependencyAnalyzer da = new DependencyAnalyzer();
		da.analyze(inputDir, output, r -> r.getFrom().getGroupId().equals("com.iboy"));

		assertTrue(FileCompare.fileCompare(output, FileSystems.getDefault().getPath("src", "test", "resources", "maven-analyzer-test", "expected-filter.svg")));

	}

}
