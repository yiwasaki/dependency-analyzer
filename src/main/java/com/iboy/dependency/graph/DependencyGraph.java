package com.iboy.dependency.graph;

import java.util.List;

import com.iboy.dependency.model.DependencyRelation;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DependencyGraph {

	@Getter
	private List<DependencyRelation> relations;

}
