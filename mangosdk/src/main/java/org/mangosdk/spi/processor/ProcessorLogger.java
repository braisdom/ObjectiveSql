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

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic.Kind;

public final class ProcessorLogger implements Logger {
	
	private final Messager messager;
	private final Options options;

	private final StringBuffer logContent = new StringBuffer();

	public ProcessorLogger(Messager messager, Options options) {
		if (messager == null) {
			throw new NullPointerException("messager");
		}
		if (options == null) {
			throw new NullPointerException("options");
		}
		
		this.messager = messager;
		this.options = options;
		note(LogLocation.MESSAGER, options.report());
		for (String warning : options.getWarnings()) {
			warning(LogLocation.BOTH, warning);
		}
	}

	@Override
	public void note(LogLocation location, String message) {
		if (location == null) {
			throw new NullPointerException("location");
		}
		if (message == null) {
			throw new NullPointerException("message");
		}
		
		if (options.verbose() && location.toMessager()) {
			messager.printMessage(Kind.NOTE, message);
		}
		
		if (options.logging() && location.toLogFile()) {
			logContent.append(message).append("\n");
		}
	}

	@Override
	public void warning(LogLocation location, String message) {
		if (location == null) {
			throw new NullPointerException("location");
		}
		if (message == null) {
			throw new NullPointerException("message");
		}
		
		if (location.toMessager()) {
			messager.printMessage(Kind.WARNING, message);
		}
		
		if (options.logging() && location.toLogFile()) {
			logContent.append("warning: ").append(message).append("\n");
		}
	}

	@Override
	public String getFileContent() {
		return logContent.toString();
	}
	
	public static String exceptionToString(Exception exception) {
		if (exception == null) {
			throw new NullPointerException("exception");
		}
		
		StringWriter out = new StringWriter();
		exception.printStackTrace(new PrintWriter(out));
		return out.toString();
	}
}
