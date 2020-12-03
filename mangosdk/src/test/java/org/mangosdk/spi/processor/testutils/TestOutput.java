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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicBoolean;

public final class TestOutput {
	
	private static final TestWriter out = new TestWriter("out");
	private static final TestWriter err = new TestWriter("err");
	
	private TestOutput() {
		throw new UnsupportedOperationException();
	}
	
	public static TestWriter out() {
		return out;
	}

	public static TestWriter err() {
		return err;
	}
	
	public static void enableAll() {
		out.setEnabled(true);
		err.setEnabled(true);
	}
	
	public static void disableAll() {
		out.setEnabled(false);
		err.setEnabled(false);
	}
	
	public static final class TestWriter {
		
		private final AtomicBoolean enabled = new AtomicBoolean(true);
		private final AtomicBoolean recording = new AtomicBoolean(false);
		
		private final ByteArrayOutputStream captured = new ByteArrayOutputStream(5000);
		private final PrintStream original;
		
		private TestWriter(String name) {
			if ("out".equals(name)) {
				original = System.out;
				System.setOut(new PrintStream(new TestOutputStream(name, System.out)));
			}
			else {
				original = System.err;
				System.setErr(new PrintStream(new TestOutputStream(name, System.err)));
			}
		}

		public void record() {
			recording.set(true);
		}
		
		public void stop() {
			recording.set(false);
		}
		
		public PrintStream bypass() {
			return original;
		}
		
		public String play() {
			try {
				return captured.toString("UTF-8");
			} 
			catch (UnsupportedEncodingException e) {
				throw new RuntimeException(e);
			}
		}
		
		public String stopPlayAndReset() {
			stop();
			String result = play();
			reset();
			return result;
		}
		
		public void reset() {
			captured.reset();
		}
		
		public boolean isEnabled() {
			return enabled.get();
		}
		
		public void setEnabled(boolean enabled) {
			this.enabled.set(enabled);
		}
		
		private class TestOutputStream extends OutputStream {
			private final OutputStream parent;
			private final String name;
			
			private TestOutputStream(String name, OutputStream parent) {
				this.parent = parent;
				this.name = name;
			}
			
			@Override
			public void write(int b) throws IOException {
				parent.write(b);
				if (!enabled.get()) {
					throw new IllegalStateException("Writing to System." + name + " is not allowed.");
				}
				if (recording.get()) {
					captured.write(b);
				}
			}
		}
	}
}