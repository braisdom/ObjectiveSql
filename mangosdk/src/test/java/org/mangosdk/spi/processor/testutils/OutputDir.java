package org.mangosdk.spi.processor.testutils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class OutputDir {
	
	private OutputDir() {
		throw new UnsupportedOperationException();
	}
	
	public static List<String> getOptions() {
		String fileName = System.getProperty("spi.output.dir", "target/test-temp");
		File file = new File(fileName);
		if (!file.exists()) {
			file.mkdir();
			file.deleteOnExit();
		}
		if (file.exists() && file.isDirectory()) {
			return Arrays.asList("-d", fileName);
		}
		return Collections.emptyList();
	}

}
