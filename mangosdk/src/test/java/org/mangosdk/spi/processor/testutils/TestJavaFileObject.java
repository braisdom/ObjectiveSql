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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;

public final class TestJavaFileObject extends SimpleJavaFileObject {
	
	public static final JavaFileObject HELLO_WORLD = create("HelloWorld", "public class HelloWorld {\n\tpublic static final void main(String[] args) {\n\t\tSystem.out.println(\"Hello World!\");\n\t}\n}");
	public static final Iterable<JavaFileObject> ONLY_HELLO_WORLD = Collections.singleton(HELLO_WORLD);
	
	private final CharSequence programText;

	private TestJavaFileObject(String className, CharSequence programText) {
		super(uri(className), Kind.SOURCE);
		this.programText = programText;
	}
	
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
        return programText;
    }
    
    private static URI uri(String className) {
    	try {
			return new URI( className + ".java" );
		} 
    	catch (URISyntaxException e) {
    		throw new RuntimeException(e);
		}     	
    }
    
    public static JavaFileObject create(String className, String content) {
    	return new TestJavaFileObject(className, content);
    }
    
    public static Iterable<? extends JavaFileObject> read(FileType type, String... names) throws IOException {
    	List<JavaFileObject> result = new ArrayList<JavaFileObject>();
    	for (String name : names) {
    		result.add(new TestJavaFileObject(name, readFile(type, name)));
    	}
    	return result;
    }

	private static CharSequence readFile(FileType type, String name) throws FileNotFoundException, IOException {
		BufferedReader reader = new BufferedReader(
			new InputStreamReader(TestJavaFileObject.class.getClassLoader().getResourceAsStream(
				type.getLocation() + "/"+ name + ".java")));
    	StringBuilder result = new StringBuilder();
    	try {
	    	String line;
	    	while ((line = reader.readLine()) != null) {
	    		result.append(line).append("\n");
	    	}
    	}
    	finally {
    		reader.close();
    	}
		return result;
	}
}
