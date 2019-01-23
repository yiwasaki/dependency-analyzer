package com.iboy.dependency.test.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class FileCompare {
	/**
	 * 引数に与えられたファイルが同一かどうかを判定する
	 * @param actual 実際のファイルのパス
	 * @param expected 期待値ファイルのパス
	 * @return 同じ場合はtrue, 違う場合はfalse
	 * @throws IOException ファイルの比較時の例外
	 */
	public static boolean fileCompare(Path actual, Path expected) throws IOException {
	    return Arrays.equals(Files.readAllBytes(actual), Files.readAllBytes(expected));
	}

}
