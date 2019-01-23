package com.iboy.dependency.util;

import java.net.URI;
import java.net.URL;

import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;

/**
 * Mavenのリポジトリに問い合わせを行うためのURIを生成するためのクラス.
 *
 * @author 12yoh
 *
 */
public class MavenURIBuilder {
	/**
	 * リポジトリのルートURL情報
	 */
	private URL repositoryRoot;
	/**
	 * MavenのグループID情報
	 */
	private String groupId;
	/**
	 * ArtifactId情報
	 */
	private String artifactId;
	/**
	 * Version情報
	 */
	private String version;

	/**
	 * コンストラクタ.
	 */
	public MavenURIBuilder() {

	}

	/**
	 * コンストラクタ.
	 * @param repoRoot
	 * @param groupId
	 * @param artifactId
	 * @param version
	 */
	public MavenURIBuilder(URL repoRoot, String groupId, String artifactId, String version) {
		this.repositoryRoot = repoRoot;
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
	}

	/**
	 * ローカルリポジトリのルート情報
	 * @param repoRoot ローカルリポジトリのルートURI情報
	 * @return このビルダー(method chain用)
	 */
	public MavenURIBuilder setRepositoryRoot(URL repoRoot) {
		this.repositoryRoot = repoRoot;
		return this;
	}

	/**
	 * GroupID情報をセットする
	 * @param groupId GroupID
	 * @return このビルダー(method chain用)
	 */
	public MavenURIBuilder setGroupId(String groupId) {
		this.groupId = groupId;
		return this;
	}

	/**
	 * ArtifactIdをセットする
	 * @param artifactId ArtifactId
	 * @return このビルダー(method chain用)
	 */
	public MavenURIBuilder setArtifactId(String artifactId) {
		this.artifactId = artifactId;
		return this;
	}

	/**
	 * バージョン情報をセットする
	 * @param version バージョン情報
	 * @return このビルダー(method chain用)
	 */
	public MavenURIBuilder setVersion(String version) {
		this.version = version;
		return this;
	}

	/**
	 * セットされた情報からURIインスタンスを生成する
	 * @return URI
	 */
	public URI build() {
		if (repositoryRoot == null) {
			throw new IllegalStateException("参照先のURLが指定されていません");
		}
		if (groupId == null || groupId.isEmpty()) {
			throw new IllegalStateException("groupIdが指定されていません");
		}
		if (artifactId == null || artifactId.isEmpty()) {
			throw new IllegalStateException("artifactIdが指定されていません");
		}
		// TODO versionが指定されていない場合の処理を追加する


		UriBuilderFactory factory = new DefaultUriBuilderFactory(this.repositoryRoot.toExternalForm());
		UriBuilder builder = factory.builder();

		 // Maven2 リポジトリのディレクトリ構成に合わせて URL を生成。
		groupId = this.groupId.replaceAll("\\.", "/");
		builder.path(groupId);
		builder.path("/" + artifactId);
		builder.path("/" + version);

		String pomFileName = artifactId + "-" + version + ".pom";
		builder.path("/" + pomFileName);
		return builder.build();
	}
}
