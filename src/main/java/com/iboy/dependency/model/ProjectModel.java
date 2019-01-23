package com.iboy.dependency.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@EqualsAndHashCode
public class ProjectModel {

	@Setter
	@Getter
	private String groupId;

	@Setter
	@Getter
	private String artifactId;

	@Setter
	@Getter
	private String version;


	@Override
	public String toString() {
		return groupId + ":" + artifactId + ":" + version;
	}
}
