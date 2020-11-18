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
import javax.tools.Diagnostic.Kind;

import junit.framework.Assert;

import org.junit.Test;
import org.mangosdk.spi.processor.testutils.FileType;
import org.mangosdk.spi.processor.testutils.NoOutputTestBase;
import org.mangosdk.spi.processor.testutils.TestDiagnosticListener;

public class CompilerErrorTest extends NoOutputTestBase {

	@Test
	public void testNoExtentionNotAllowed() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.INVALID, "NoExtentionTestClass");
		Assert.assertEquals(1, list.size());
		
		Diagnostic<JavaFileObject> diagnostic = list.get(0);
		Assert.assertEquals(Kind.ERROR, diagnostic.getKind());
		Assert.assertEquals(6, diagnostic.getLineNumber());
		Assert.assertEquals("NoExtentionTestClass does not extend java.util.ArrayList", getMessage(diagnostic));
	}
	@Test
	public void testNoImplementationNotAllowed() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.INVALID, "NoImplementationTestClass");
		Assert.assertEquals(1, list.size());
		
		Diagnostic<JavaFileObject> diagnostic = list.get(0);
		Assert.assertEquals(Kind.ERROR, diagnostic.getKind());
		Assert.assertEquals(6, diagnostic.getLineNumber());
		Assert.assertEquals("NoImplementationTestClass does not implement java.util.RandomAccess", getMessage(diagnostic));
	}
	
	@Test
	public void testNonStaticNotAllowed() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.INVALID, "NonStaticTestClass");
		Assert.assertEquals(1, list.size());
		
		Diagnostic<JavaFileObject> diagnostic = list.get(0);
		Assert.assertEquals(Kind.ERROR, diagnostic.getKind());
		Assert.assertEquals(8, diagnostic.getLineNumber());
		Assert.assertEquals("InnerNonStaticTestClass is not a static class", getMessage(diagnostic));
	}
	
	@Test
	public void testPackagePrivateNotAllowed() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.INVALID, "PackagePrivateTestClass");
		Assert.assertEquals(1, list.size());
		
		Diagnostic<JavaFileObject> diagnostic = list.get(0);
		Assert.assertEquals(Kind.ERROR, diagnostic.getKind());
		Assert.assertEquals(6, diagnostic.getLineNumber());
		Assert.assertEquals("PackagePrivateTestClass is not a public class", getMessage(diagnostic));
	}
	
	@Test
	public void testEnumNotAllowed() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.INVALID, "EnumTestClass");
		Assert.assertEquals(1, list.size());

		Diagnostic<JavaFileObject> diagnostic = list.get(0);
		Assert.assertEquals(Kind.ERROR, diagnostic.getKind());
		Assert.assertEquals(6, diagnostic.getLineNumber());
		Assert.assertEquals("EnumTestClass is not a class", getMessage(diagnostic));
	}
	
	@Test
	public void testInterfaceNotAllowed() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.INVALID, "InterfaceTestClass");
		Assert.assertEquals(1, list.size());
		
		Diagnostic<JavaFileObject> diagnostic = list.get(0);
		Assert.assertEquals(Kind.ERROR, diagnostic.getKind());
		Assert.assertEquals(6, diagnostic.getLineNumber());
		Assert.assertEquals("InterfaceTestClass is not a class", getMessage(diagnostic));
	}
	
	@Test
	public void testNoPublicConstructorNotAllowed() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.INVALID, "NoPublicConstructorTestClass");
		Assert.assertEquals(1, list.size());
		
		Diagnostic<JavaFileObject> diagnostic = list.get(0);
		Assert.assertEquals(Kind.ERROR, diagnostic.getKind());
		Assert.assertEquals(6, diagnostic.getLineNumber());
		Assert.assertEquals("NoPublicConstructorTestClass has no public no-args constructor", getMessage(diagnostic));
	}
	
	@Test
	public void testNoNoArgsConstructorNotAllowed() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.INVALID, "NoNoArgsConstructorTestClass");
		Assert.assertEquals(1, list.size());
		
		Diagnostic<JavaFileObject> diagnostic = list.get(0);
		Assert.assertEquals(Kind.ERROR, diagnostic.getKind());
		Assert.assertEquals(6, diagnostic.getLineNumber());
		Assert.assertEquals("NoNoArgsConstructorTestClass has no public no-args constructor", getMessage(diagnostic));
	}
	
	@Test
	public void testAnnotationNotAllowed() throws IOException {
		List<Diagnostic<JavaFileObject>> list = TestDiagnosticListener.compile(FileType.INVALID, "AnnotationTestClass");
		Assert.assertEquals(1, list.size());
		
		Diagnostic<JavaFileObject> diagnostic = list.get(0);
		Assert.assertEquals(Kind.ERROR, diagnostic.getKind());
		Assert.assertEquals(6, diagnostic.getLineNumber());
		Assert.assertEquals("AnnotationTestClass is not a class", getMessage(diagnostic));
	}
	
	private static String getMessage(Diagnostic<JavaFileObject> diagnostic) {
		String msg = diagnostic.getMessage(null);
		int first = msg.indexOf(':');
		int second = msg.indexOf(':', first + 1);
		return msg.substring(second + 1).trim();
	}
}
