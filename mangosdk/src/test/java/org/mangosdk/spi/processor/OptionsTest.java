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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import junit.framework.Assert;

import org.junit.Test;
import org.mangosdk.spi.processor.testutils.NoOutputTestBase;
import org.mangosdk.spi.processor.testutils.OutputDir;
import org.mangosdk.spi.processor.testutils.TestJavaFileObject;

public class OptionsTest extends NoOutputTestBase {
	
	private JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

	@Test 
	public void testDisabledDefault() {
		Options options = getOptions("-Aspi_disabled");
		Assert.assertTrue(options.disabled());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report("", null, null, null), options.report());
	}
	
	@Test 
	public void testDisabledFalse() {
		Options options = getOptions("-Aspi_disabled=false");
		Assert.assertFalse(options.disabled());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report("false", null, null, null), options.report());
	}
	
	@Test 
	public void testDisabledTrue() {
		Options options = getOptions("-Aspi_disabled=true");
		Assert.assertTrue(options.disabled());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report("true", null, null, null), options.report());
	}
	
	@Test 
	public void testDisabledTrueCaseInsensitive() {
		Options options = getOptions("-Aspi_disabled=TrUe");
		Assert.assertTrue(options.disabled());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report("TrUe", null, null, null), options.report());
	}
	
	@Test 
	public void testDisabledSomeValue() {
		Options options = getOptions("-Aspi_disabled=somevalue");
		Assert.assertFalse(options.disabled());
		Collection<String> warnings = options.getWarnings();
		Assert.assertEquals(1, warnings.size());
		Assert.assertEquals("Unrecognized value for parameter 'spi_disabled'. Found 'somevalue'.  Legal values: 'true', 'false'.", warnings.iterator().next());
		Assert.assertEquals(report("somevalue", null, null, null), options.report());
	}

	@Test 
	public void testVerboseTrue() {
		Options options = getOptions("-Aspi_verbose");
		Assert.assertFalse(options.disabled());
		Assert.assertTrue(options.verbose());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report(null, "", null, null), options.report());
	}
	
	@Test 
	public void testLoggingTrue() {
		Options options = getOptions("-Aspi_log");
		Assert.assertTrue(options.logging());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report(null, null, "", null), options.report());
	}
	
	@Test 
	public void testLoggingAndVerboseTrue() {
		Options options = getOptions("-Aspi_log", "-Aspi_verbose");
		Assert.assertTrue(options.logging());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report(null, "", "", null), options.report());
	}
	
	@Test 
	public void testDirNotEmpty() {
		Options options = getOptions("-Aspi_dir=temp");
		Assert.assertEquals("temp/", options.dir());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report(null, null, null, "temp"), options.report());
	}
	
	@Test 
	public void testDirNotEmptyTrailingSlash() {
		Options options = getOptions("-Aspi_dir=temp/");
		Assert.assertEquals("temp/", options.dir());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report(null, null, null, "temp/"), options.report());
	}
	
	@Test 
	public void testDirNotEmptyTrailingBackSlash() {
		Options options = getOptions("-Aspi_dir=temp\\");
		Assert.assertEquals("temp/", options.dir());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report(null, null, null, "temp\\"), options.report());
	}
	
	@Test 
	public void testDirNotEmptyContainingBackSlash() {
		Options options = getOptions("-Aspi_dir=temp\\temp");
		Assert.assertEquals("temp/temp/", options.dir());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report(null, null, null, "temp\\temp"), options.report());
	}
	
	@Test 
	public void testDirEmpty() {
		Options options = getOptions("-Aspi_dir");
		Assert.assertEquals("", options.dir());
		Assert.assertTrue(options.getWarnings().isEmpty());
		Assert.assertEquals(report(null, null, null, ""), options.report());
	}
	
	@Test 
	public void testDirMissing() {
		Options options = getOptions("-Aspi_disabled");
		Assert.assertEquals("", options.dir());
		Assert.assertTrue(options.getWarnings().isEmpty());
	}
	
	@Test 
	public void testOptionsLogging() {
		String expected = report(null, null, "", null);
		
		Assert.assertEquals(expected, getOptions("-Aspi_log").report());
	}
	
	private String report(String disabled, String verbose, String log, String dir) {
		StringBuilder message = new StringBuilder();
		message
			.append("Initializing Annotation Processor ").append(SpiProcessor.NAME).append("\n")
			.append("Used options:\n");
		
		appendValue(message, "spi_disabled", disabled);
		appendValue(message, "spi_verbose", verbose);
		appendValue(message, "spi_log", log);
		appendValue(message, "spi_dir", dir);
		return message.toString();
	}
	
	private void appendValue(StringBuilder message, String name, String value)  {
		message.append(" - ").append(name).append(": ");
		if (value == null) {
			message.append("missing");
		}
		else {
			message.append("'").append(value).append("'");
		}
		message.append("\n");
	}
	
	private Options getOptions(String... parameters) {
		List<String> compilerOptions = new ArrayList<String>();
		compilerOptions.addAll(Arrays.asList(parameters));
		compilerOptions.addAll(OutputDir.getOptions());
		CompilationTask task = compiler.getTask(null, null, null, compilerOptions, null, TestJavaFileObject.ONLY_HELLO_WORLD);
		TestProcessor processor = new TestProcessor();
		task.setProcessors(Collections.singleton(processor));
		task.call();
		return processor.getOptions();
	}


	@SupportedAnnotationTypes("*")
	@SupportedSourceVersion(SourceVersion.RELEASE_6)
	@SupportedOptions({Options.SPI_DIR_OPTION, Options.SPI_LOG_OPTION, Options.SPI_VERBOSE_OPTION, Options.SPI_DISABLED_OPTION})
	public static class TestProcessor extends AbstractProcessor {
		
		@Override
		public SourceVersion getSupportedSourceVersion() {
			return SourceVersion.latestSupported();
		}

		@Override
		public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
			return false;
		}
		
		Options getOptions() {
			return new Options(processingEnv.getOptions());
		}
	}
}
