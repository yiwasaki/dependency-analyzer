package com.iboy.dependency;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.iboy.dependency.analysis.MavenAnalyzer;
import com.iboy.dependency.graph.DependencyGraph;
import com.iboy.dependency.graph.GraphLayout;
import com.iboy.dependency.model.DependencyRelation;


public class DependencyAnalyzer {

	/**
	 * パッケージマネージャによる依存関係を解析する.
	 * このメソッドでは、解析対象のディレクトリ内の定義ファイルを取得し、
	 * 定義ファイル間での依存関係を解析する,
	 *
	 * 依存関係の解析対象となるのは、
	 *  pom.xml, build.gradle(対応予定)
	 * である.
	 *
	 * @param inputDir 解析対象のディレクトリ
	 * @param output 解析結果ファイル
	 * @throws IOException ファイル操作中に例外が発生した場合
	 */
	public void analyze(Path inputDir, Path output) throws IOException {
		List<DependencyRelation> relations = new ArrayList<>();

		Set<Path> targetFiles = findAllTargetFiles(inputDir, "pom.xml");
		MavenAnalyzer ma = new MavenAnalyzer();
		for (Path target : targetFiles) {
			relations.addAll(ma.analyze(target));
		}

		// TODO: Gradleの解析を行う場合は、ここでrelationsにaddする

		DependencyGraph dg = new DependencyGraph(relations);
		GraphLayout layout = new GraphLayout();
		layout.layout(output, dg);
	}

	/**
	 * 解析対象のファイルパス一覧を取得する.
	 *
	 * 解析対象のファイルは、引数に与えられたフィルター条件にマッチするかどうかで
	 * 判断を行う,
	 *
	 * @param inputDir 解析対象のディレクトリ
	 * @param filter ファイル名のフィルタ条件
	 * @return 解析対象のファイルパス
	 */
	private Set<Path> findAllTargetFiles(Path inputDir, String filter) {
		Set<Path> retVal = new HashSet<>();

		for (File f : inputDir.toFile().listFiles()) {
			if (f.isDirectory()) {
				Set<Path> recursionResults = findAllTargetFiles(f.toPath(), filter);
				retVal.addAll(recursionResults);
			} else {
				if (f.getName().endsWith(filter)) {
					retVal.add(f.toPath());
				}
			}
		}

		return retVal;
	}
}
