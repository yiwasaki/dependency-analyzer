package com.iboy.dependency.analysis.mapper;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;

import com.iboy.dependency.model.DependencyRelation;
import com.iboy.dependency.model.ProjectModel;

public class MavenDependencyMapper {

	/**
	 * pomモデルをDependencyRelationで表現する関係にマップする
	 * @param pom POMモデル
	 * @return POMでの依存関係
	 */
	public List<DependencyRelation> mapToDependencyRelations(Model pom) {
		if (pom == null || pom.getDependencies() == null) {
			return Collections.emptyList();
		}

		List<DependencyRelation> retVal = new LinkedList<>();
		var from = mapPomToProjectModel(pom);

		for (Dependency d : pom.getDependencies()) {
			ProjectModel dependency = mapDependencyToModel(d);
			retVal.add(new DependencyRelation(from, dependency));
		}


		return retVal;
	}

	/**
	 * Mavenのpomモデルから、このプロジェクト内でのプロジェクトモデルの表現に変換する.
	 *
	 * @param pom Mavenのpomモデル
	 * @return プロジェクトモデル
	 */
	private ProjectModel mapPomToProjectModel(Model pom) {
		String groupId = pom.getGroupId();
		if (groupId == null && pom.getParent() != null) {
			groupId = pom.getParent().getGroupId();
		}
		String version = pom.getVersion();
		if (version == null && pom.getParent() != null) {
			version = pom.getParent().getVersion();
		}
		return new ProjectModel(groupId, pom.getArtifactId(), version);
	}

	/**
	 * MavenのPOMのdependencyで記述されている情報をProjectModelに変換
	 * @param dependency 依存情報
	 * @return プロジェクトモデル
	 */
	private ProjectModel mapDependencyToModel(Dependency dependency) {
		return new ProjectModel(dependency.getGroupId(), dependency.getArtifactId(), dependency.getVersion());
	}
}
