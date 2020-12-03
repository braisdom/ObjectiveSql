package org.mangosdk.spi.processor.testutils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.ToolProvider;
import javax.tools.JavaCompiler.CompilationTask;

import org.mangosdk.spi.processor.SpiProcessor;

public class TestDiagnosticListener implements DiagnosticListener<JavaFileObject> {
	
	private final List<Diagnostic<JavaFileObject>> diagnostics = new ArrayList<Diagnostic<JavaFileObject>>();
	
	@Override
	public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
		@SuppressWarnings("unchecked")
		Diagnostic<JavaFileObject> d = (Diagnostic<JavaFileObject>) diagnostic;
		diagnostics.add(d);
	}
	
	public List<Diagnostic<JavaFileObject>> diagnostics() {
		return diagnostics;
	}
	
	public static List<Diagnostic<JavaFileObject>> compile(FileType type, String... fileNames) throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		TestDiagnosticListener listener = new TestDiagnosticListener();
		CompilationTask task = compiler.getTask(null, null, listener, OutputDir.getOptions(), null, TestJavaFileObject.read(type, fileNames));
		task.setProcessors(Collections.singleton(new SpiProcessor()));
		task.call();
		return listener.diagnostics();
	}	
}
