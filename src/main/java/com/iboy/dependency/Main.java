package com.iboy.dependency;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.FileSystems;
import java.nio.file.Path;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;

public class Main {

	public static void main(String[] args) {
		Main m = new Main();
		Model model = m.readModel();
		model.toString();
	}



    public Model readModel() {
    	Path p = FileSystems.getDefault().getPath("src/test/resources/pom.xml");

    	return readModel(p);
    }

    protected Model readModel(final Path path) {
        Model model = null;
        try (Reader source = new InputStreamReader(new FileInputStream(path.toFile()))){
            // pom.xml の解析
            MavenXpp3Reader reader = new MavenXpp3Reader();
            model = reader.read(source);
        } catch(IOException | XmlPullParserException e) {
        	e.printStackTrace();
        }
        return model;
    }
}
