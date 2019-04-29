package com.iboy.dependency.analysis.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.junit.jupiter.api.Test;

import com.iboy.dependency.model.DependencyRelation;
import com.iboy.dependency.model.ProjectModel;


class MavenDependencyMapperTest extends MavenDependencyMapper {

	@Test
	void MavenのModelをMavenDependencyの形式に正しく変換できる() throws Exception {
		Path path = FileSystems.getDefault().getPath("src", "test", "resources", "dependency-mapper-test", "pom.xml");
		Model model;
        try (Reader source = new InputStreamReader(new FileInputStream(path.toFile()))){
            var reader = new MavenXpp3Reader();
            model = reader.read(source);
        } catch (Exception e) {
        	throw e;
		}

        MavenDependencyMapper mapper = new MavenDependencyMapper();
        var list = mapper.mapToDependencyRelations(model);

        assertEquals(list.size(), 4);

        var expected = List.of(new DependencyRelation(new ProjectModel("com.iboy", "dependency-analysis", "0.0.1-SNAPSHOT"), new ProjectModel("org.apache.maven", "maven-model", "3.6.0")),
        		                new DependencyRelation(new ProjectModel("com.iboy", "dependency-analysis", "0.0.1-SNAPSHOT"), new ProjectModel("org.codehaus.plexus", "plexus-utils", "3.1.0")),
        		                new DependencyRelation(new ProjectModel("com.iboy", "dependency-analysis", "0.0.1-SNAPSHOT"), new ProjectModel("org.projectlombok", "lombok", "1.18.4")),
        		                new DependencyRelation(new ProjectModel("com.iboy", "dependency-analysis", "0.0.1-SNAPSHOT"), new ProjectModel("org.junit.jupiter", "junit-jupiter-engine", "5.0.2")));

		assertIterableEquals(list, expected);

	}

	@Test
	void 引数に与えられた変数がNullの時に空のリストが返る() {
		MavenDependencyMapper mapper = new MavenDependencyMapper();
		var list = mapper.mapToDependencyRelations(null);

		assertTrue(list.isEmpty());
	}

	@Test
	void 引数に与えられたModelのDependencies属性がnullの時に空のリストが返る() {
		var model = new Model();
		model.setDependencies(null);

		MavenDependencyMapper mapper = new MavenDependencyMapper();
		var list = mapper.mapToDependencyRelations(model);
		assertTrue(list.isEmpty());

	}

	@Test
	void 親子関係のPomも正しくマップできる() throws Exception {
		Path path = FileSystems.getDefault().getPath("src", "test", "resources", "parent-child", "pom.xml");
		Model model;
        try (Reader source = new InputStreamReader(new FileInputStream(path.toFile()))){
            var reader = new MavenXpp3Reader();
            model = reader.read(source);
        } catch (Exception e) {
        	throw e;
		}
        
        MavenDependencyMapper mapper = new MavenDependencyMapper();
        var list = mapper.mapToDependencyRelations(model);

        assertEquals(1, list.size());
        List<DependencyRelation> expected = List.of(new DependencyRelation(new ProjectModel("org.sample", "sample", "1.0"),
        																	new ProjectModel("org.apache.maven", "maven-model", "3.6.0")));
        assertIterableEquals(list, expected);

	}
}
