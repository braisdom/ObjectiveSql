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

import java.io.IOException;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import junit.framework.Assert;

import org.junit.Test;
import org.mangosdk.spi.processor.testutils.FileType;
import org.mangosdk.spi.processor.testutils.NoOutputTestBase;
import org.mangosdk.spi.processor.testutils.TestDiagnosticListener;
import org.mangosdk.spi.processor.testutils.TestOutput;

public class CompilerOkTest extends NoOutputTestBase {
	
	@Test
	public void testGenerics() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.VALID, "GenericsTestClass");
		TestOutput.out().bypass().println(list);
		Assert.assertTrue(list.isEmpty());
	}

	@Test
	public void testSimple() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.VALID, "SimpleTestClass");
		Assert.assertTrue(list.isEmpty());
	}
	
	@Test
	public void testMultipleInterfaces() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.VALID, "MultipleInterfacesTestClass");
		Assert.assertTrue(list.isEmpty());
	}
	
	@Test
	public void testSuperClassImplementsInterface() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.VALID, "SuperClass", "SubTestClass");
		Assert.assertTrue(list.isEmpty());
	}
	
	@Test
	public void testStaticInnerClass() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.VALID, "StaticInnerTestClass");
		Assert.assertTrue(list.isEmpty());
	}
	
	@Test
	public void testProvidesClass() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.VALID, "ProvidesClassTestClass");
		Assert.assertTrue(list.isEmpty());
	}
}
