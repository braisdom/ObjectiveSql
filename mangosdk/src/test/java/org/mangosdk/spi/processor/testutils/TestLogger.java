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

package org.mangosdk.spi.processor.testutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.tools.Diagnostic.Kind;

import org.mangosdk.spi.processor.LogLocation;
import org.mangosdk.spi.processor.Logger;

public class TestLogger implements Logger {

	private final List<Record> records = new ArrayList<Record>();
	
	@Override
	public String getFileContent() {
		StringBuilder result = new StringBuilder();
		for (Record r : records) {
			if (r.location.toLogFile()) {
				if (r.kind == Kind.WARNING) {
					result.append("warning: ");
				}
				result.append(r.message).append("\n");
			}
		}
		return result.toString();
	}

	@Override
	public void note(LogLocation location, String message) {
		records.add(new Record(Kind.NOTE, location, message));
	}

	@Override
	public void warning(LogLocation location, String message) {
		records.add(new Record(Kind.WARNING, location, message));
	}
	
	public List<Record> records() {
		return Collections.unmodifiableList(records);
	}
	
	public void reset() {
		records.clear();
	}
	
	public static final class Record {
		final Kind kind;
		final LogLocation location;
		final String message;
		
		public Record(Kind kind, LogLocation location, String message) {
			this.kind = kind;
			this.location = location;
			this.message = message;
		}
	}
}