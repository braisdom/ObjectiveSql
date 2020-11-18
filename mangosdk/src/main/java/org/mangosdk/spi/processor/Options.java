/*

Copyright 2008 TOPdesk, the Netherlands

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package org.mangosdk.spi.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class Options {
	static final String SPI_DISABLED_OPTION = "spi_disabled";
	static final String SPI_DIR_OPTION = "spi_dir";
	static final String SPI_LOG_OPTION = "spi_log";
	static final String SPI_VERBOSE_OPTION = "spi_verbose";
	
	private final List<String> warnings = new ArrayList<String>();
	
	private final boolean disabled;
	private final boolean log;
	private final boolean verbose;
	private final String dir;
	private final String report;

	public Options(Map<String,String> values) {
		disabled = getBooleanParameter(values, SPI_DISABLED_OPTION);
		log = getBooleanParameter(values, SPI_LOG_OPTION);
		verbose = getBooleanParameter(values, SPI_VERBOSE_OPTION);
		dir = cleanPath(values.get(SPI_DIR_OPTION));
		
		report = createReport(values);
	}
	
	public boolean disabled() {
		return disabled;
	}
	
	public boolean verbose() {
		return verbose;
	}
	
	public boolean logging() {
		return log;
	}
	
	public String dir() {
		return dir;
	}
	
	public Collection<String> getWarnings() {
		return Collections.unmodifiableCollection(warnings);
	}
	
	public String report() {
		return report;
	}

	private String createReport(Map<String, String> values) {
		StringBuilder result = new StringBuilder();
		
		result
			.append("Initializing Annotation Processor ")
			.append(SpiProcessor.NAME)
			.append("\nUsed options:\n");
		
		writeOption(result, values, SPI_DISABLED_OPTION);
		writeOption(result, values, SPI_VERBOSE_OPTION);
		writeOption(result, values, SPI_LOG_OPTION);
		writeOption(result, values, SPI_DIR_OPTION);

		return result.toString();
	}
	

	private static String cleanPath(String path) {
		if (path == null) {
			return "";
		}
		
		String backSlashless = path.replace("\\", "/");
		
		if (backSlashless.endsWith("/")) {
			return backSlashless;
		}
		
		return backSlashless + "/";
	}

	private boolean getBooleanParameter(Map<String, String> values, String optionName) {
		if (!values.containsKey(optionName)) {
			return false;
		}
		
		String optionValue = values.get(optionName);
		if (optionValue == null || "true".equalsIgnoreCase(optionValue)) {
			return true;
		}
		
		if (!"false".equalsIgnoreCase(optionValue)) {
			warnings.add("Unrecognized value for parameter '" + optionName + "'. Found '" + optionValue + "'.  Legal values: 'true', 'false'.");
		}
		return false;
	}
	
	private void writeOption(StringBuilder result, Map<String, String> values, String optionName) {
		result
			.append(" - ")
			.append(optionName)
			.append(": ")
			.append(optionMessage(values, optionName))
			.append("\n");
	}
	
	private String optionMessage(Map<String, String> values, String optionName) {
		if (values.containsKey(optionName)) {
			String optionValue = values.get(optionName);
			if (optionValue == null) {
				return "''";
			}
			return "'" + optionValue + "'";
		}
		return "missing";
	}
}
