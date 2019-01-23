package com.iboy.dependency.analysis;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.iboy.dependency.analysis.mapper.MavenDependencyMapper;
import com.iboy.dependency.model.DependencyRelation;

public class MavenAnalyzer implements IAnalyzer {
	// ログ出力用のロガー
	private static final Logger logger = LoggerFactory.getLogger(MavenAnalyzer.class);

	// MavenDependencyのマッパー
	private final MavenDependencyMapper mapper = new MavenDependencyMapper();

	@Override
	public List<DependencyRelation> analyze(Path pomPath) {
		// Validation
		if (pomPath == null) {
			return Collections.emptyList();
		}
		if (!pomPath.toFile().exists()) {
			return Collections.emptyList();
		}

		// ロジックの処理
		Model pom;
		try {
			pom = readModel(pomPath);
		} catch (IOException | XmlPullParserException e) {
			logger.error(e.getMessage(), e);
			return Collections.emptyList();
		}
		return mapper.mapToDependencyRelations(pom);
	}

    private Model readModel(final Path path) throws IOException, XmlPullParserException {
        Model model = null;
        try (Reader source = new InputStreamReader(new FileInputStream(path.toFile()))){
            var reader = new MavenXpp3Reader();
            model = reader.read(source);
        }

        InputStream is = new FileInputStream(path.toFile());
        Reader source = new InputStreamReader(is);

        return read(is, source);
    }

	@Override
	public List<DependencyRelation> analyze(URI uri) {
		// Validation
		if (uri == null) {
			return Collections.emptyList();
		}

		// ロジック
		Model pom;
		try {
			pom = readModel(uri.toURL());
		} catch (IOException | XmlPullParserException e) {
			logger.error(e.getMessage(), e);
			return Collections.emptyList();
		}
		return mapper.mapToDependencyRelations(pom);
	}


    private Model readModel(final URL url) throws IOException, XmlPullParserException {
        Model model = null;
        URLConnection connection = url.openConnection();
        InputStream is = connection.getInputStream();
        Reader source = new InputStreamReader(is);
        return read(is, source);
    }

    /**
     * 引数に与えられたInputStream, Readerを用いてModelを読み込む.
     * Modelを読み込んだ後、引数のauto Closeが呼ばれる.
     *
     * @param is 入力
     * @param source 入力となるreader
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    private Model read(InputStream is, Reader source) throws IOException, XmlPullParserException {
    	Model model;
    	try (is; source) {
    		var reader = new MavenXpp3Reader();
    		model = reader.read(source);
    	}
    	return model;
    }

}
