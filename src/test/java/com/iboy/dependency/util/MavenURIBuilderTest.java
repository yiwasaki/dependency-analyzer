package com.iboy.dependency.util;

import static org.junit.jupiter.api.Assertions.*;

import java.net.URI;
import java.net.URL;

import org.junit.jupiter.api.Test;

class MavenURIBuilderTest extends MavenURIBuilder {

	@Test
	void 正しくURIが生成される() throws Exception {
		URL url = new URL("http://central.maven.org/maven2/");
		MavenURIBuilder builder = new MavenURIBuilder(url, "org.apache.logging.log4j", "log4j-slf4j-impl", "2.11.1");
		URI uri = builder.build();

		assertEquals("http://central.maven.org/maven2/org/apache/logging/log4j/log4j-slf4j-impl/2.11.1/log4j-slf4j-impl-2.11.1.pom", uri.toString());
	}

	@Test
	void rootリポジトリ情報がない場合にエラーが発火される() {
		MavenURIBuilder builder = new MavenURIBuilder(null, null, null, null);
		assertThrows(IllegalStateException.class, () -> builder.build());
	}

	@Test
	void groupId情報がない場合にエラーが発火される() throws Exception {
		URL url = new URL("http://central.maven.org/maven2/");
		MavenURIBuilder builder = new MavenURIBuilder(url, null, null, null);

		assertThrows(IllegalStateException.class, () -> builder.build());
	}

	@Test
	void artifactId情報がない場合にエラーが発火される() throws Exception {
		URL url = new URL("http://central.maven.org/maven2/");
		MavenURIBuilder builder = new MavenURIBuilder(url, "org.apache.logging.log4j", null, null);

		assertThrows(IllegalStateException.class, () -> builder.build());
	}

}
