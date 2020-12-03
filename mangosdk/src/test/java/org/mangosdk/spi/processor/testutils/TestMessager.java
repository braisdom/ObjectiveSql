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
import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public class TestMessager implements Messager {
	
	private final List<Message> messages = new ArrayList<Message>();
	
	@Override
	public void printMessage(Kind kind, CharSequence msg) {
		printMessage(kind, msg, null);
	}

	@Override
	public void printMessage(Kind kind, CharSequence msg, Element e) {
		printMessage(kind, msg, e, null);
	}

	@Override
	public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a) {
		printMessage(kind, msg, e, a, null);
	}

	@Override
	public void printMessage(Kind kind, CharSequence msg, Element e, AnnotationMirror a, AnnotationValue v) {
		messages.add(new Message(kind, msg, e, a, v));
	}
	
	public void reset() {
		messages.clear();
	}
	
	public List<Message> messages() {
		return messages;
	}
	
	public static final class Message {
		public final Kind kind;
		public final CharSequence msg;
		public final Element element;
		public final AnnotationMirror annotationMirror;
		public final AnnotationValue annotationValue;

		public Message(Kind kind, CharSequence msg, Element e, AnnotationMirror a, AnnotationValue v) {
			this.kind = kind;
			this.msg = msg;
			this.element = e;
			this.annotationMirror = a;
			this.annotationValue = v;
		}
	}
}
