package com.iboy.dependency.analysis;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.iboy.dependency.model.DependencyRelation;
import com.iboy.dependency.model.ProjectModel;
import com.iboy.dependency.util.MavenURIBuilder;

class MavenAnalyzerTest extends MavenAnalyzer {

	@Test
	void 引数に与えられたpomファイルから正しくモデルを作成できる() {
		MavenAnalyzer analyzer = new MavenAnalyzer();

		// 空のパスで絶対に存在しないファイルパスを作成する
		Path p = FileSystems.getDefault().getPath("src", "test", "resources", "maven-analyzer-test", "pom.xml");

		List<DependencyRelation> relations = analyzer.analyze(p);

		assertEquals(relations.size(), 4);

		List<ProjectModel> models = relations.stream()
											 .map(r -> r.getFrom())
											 .distinct()
											 .collect(Collectors.toList());

		assertEquals(models.size(), 1);


		List<ProjectModel> toModels = relations.stream()
											    .map(r -> r.getTo())
											    .collect(Collectors.toList());
		assertEquals(toModels.size(), 4);
		List<ProjectModel> expected = List.of(new ProjectModel("org.apache.maven", "maven-model", "3.6.0"),
											   new ProjectModel("org.codehaus.plexus", "plexus-utils", "3.1.0"),
											   new ProjectModel("org.projectlombok", "lombok", "1.18.4"),
											   new ProjectModel("org.junit.jupiter", "junit-jupiter-engine", "5.0.2"));

		assertIterableEquals(toModels, expected);
	}

	@Test
	void 引数に与えられたpomFileパスがnullの場合に空のリストを返すことができる() {
		MavenAnalyzer analyzer = new MavenAnalyzer();
		List<DependencyRelation> relations = analyzer.analyze((Path)null);

		assertTrue(relations.isEmpty());
	}

	@Test
	void 引数に与えられたpomFileパスにファイルが存在しないの場合に空のリストを返すことができる() {
		MavenAnalyzer analyzer = new MavenAnalyzer();

		// 空のパスで絶対に存在しないファイルパスを作成する
		Path p = FileSystems.getDefault().getPath("");

		List<DependencyRelation> relations = analyzer.analyze(p);

		assertTrue(relations.isEmpty());
	}

	@Test
	void 引数に与えられたpomFileのファイルがpomファイルでない場合に空のリストを返すことができる() {
		MavenAnalyzer analyzer = new MavenAnalyzer();

		// 空のパスで絶対に存在しないファイルパスを作成する
		Path p = FileSystems.getDefault().getPath("src", "test", "java", "com", "iboy", "dependency", "analysis", "MavenAnalyzerTest.java");

		List<DependencyRelation> relations = analyzer.analyze(p);

		assertTrue(relations.isEmpty());
	}

	@Test
	void 引数に与えられたURIから正しくモデルを生成することができる() throws Exception {
		URL url = new URL("http://central.maven.org/maven2/");
		MavenURIBuilder builder = new MavenURIBuilder(url, "org.springframework", "spring-core", "5.1.3.RELEASE");

		MavenAnalyzer analyzer = new MavenAnalyzer();
		List<DependencyRelation> relations = analyzer.analyze(builder.build());

		assertEquals(relations.size(), 10);
		List<ProjectModel> to = relations.stream().map(r -> r.getTo()).collect(Collectors.toList());
		assertThat(to, hasItems(new ProjectModel("org.springframework", "spring-jcl", "5.1.3.RELEASE"),
								  new ProjectModel("io.netty", "netty-buffer", "4.1.31.Final"),
								  new ProjectModel("io.projectreactor", "reactor-core", "3.2.3.RELEASE"),
								  new ProjectModel("io.reactivex.rxjava2", "rxjava", "2.2.4"),
								  new ProjectModel("io.reactivex", "rxjava", "1.3.8"),
								  new ProjectModel("net.sf.jopt-simple", "jopt-simple", "5.0.4"),
								  new ProjectModel("org.aspectj", "aspectjweaver", "1.9.2"),
								  new ProjectModel("org.jetbrains.kotlin", "kotlin-reflect", "1.2.71"),
								  new ProjectModel("org.jetbrains.kotlin", "kotlin-stdlib", "1.2.71"),
								  new ProjectModel("io.reactivex", "rxjava-reactive-streams", "1.2.1")));
	}

	@Test
	void 引数に与えられたURIがnullの場合に空のリストを返すことができる() {
		MavenAnalyzer analyzer = new MavenAnalyzer();
		List<DependencyRelation> relations = analyzer.analyze((URI)null);

		assertTrue(relations.isEmpty());
	}

	@Test
	void 引数に与えられたURIにファイルが存在しないの場合に空のリストを返すことができる() throws Exception {
		URL url = new URL("http://central.maven.org/maven2/");
		MavenURIBuilder builder = new MavenURIBuilder(url, "org.springframework", "spring-core", "5.1.3-test");

		MavenAnalyzer analyzer = new MavenAnalyzer();
		List<DependencyRelation> relations = analyzer.analyze(builder.build());

		assertTrue(relations.isEmpty());
	}

	@Test
	void 引数に与えられたURIのファイルがpomファイルでない場合に空のリストを返すことができる() throws MalformedURLException {
		URL url = new URL("http://central.maven.org/maven2/");
		MavenURIBuilder builder = new MavenURIBuilder(url, "org.springframework", "spring-core", "test");

		MavenAnalyzer analyzer = new MavenAnalyzer();
		List<DependencyRelation> relations = analyzer.analyze(builder.build());
		assertTrue(relations.isEmpty());
	}

}
