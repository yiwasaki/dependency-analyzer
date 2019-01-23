package com.iboy.dependency.analysis;

import java.net.URI;
import java.nio.file.Path;
import java.util.List;

import com.iboy.dependency.model.DependencyRelation;

public interface IAnalyzer {

	List<DependencyRelation> analyze(Path buildFilePath);

	List<DependencyRelation> analyze(URI uri);
}
