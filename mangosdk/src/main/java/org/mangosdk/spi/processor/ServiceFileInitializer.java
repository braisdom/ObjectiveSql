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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;


final class ServiceFileInitializer implements Initializer {

	private final Filer filer;
	private final String path;
	private final Logger logger;

	ServiceFileInitializer(Filer filer, String path, Logger logger) {
		this.filer = filer;
		this.path = path;
		this.logger = logger;
	}

	@Override
	public CharSequence initialData(String serviceName) {
		try {
			FileObject resource = filer.getResource(StandardLocation.CLASS_OUTPUT, "", path + serviceName);
			
			CharSequence result;
			try {
				// Eclipse can't handle the getCharContent
				// See https://bugs.eclipse.org/bugs/show_bug.cgi?id=246089
				// 2008-09-12 RoelS: I've posted a patch file
				result = tryWithReader(resource);
			} 
			catch (FileNotFoundException e) {
				// Could happen
				return null;
			}
			catch (IOException e) {
				logger.note(LogLocation.MESSAGER, "Eclipse gave an IOException: " + e.getMessage());
				return null;
			}
			catch (Exception other) {
				try {
					// Javac can't handle the openReader
					// Filed as a bug at bugs.sun.com and recieved a review ID: 1339738
					result = resource.getCharContent(true);
				}
				catch (FileNotFoundException e) {
					// Could happen
					return null;
				}
				catch (IOException e) {
					logger.note(LogLocation.MESSAGER, "Javac gave an IOException: " + e.getMessage());
					return null;
				}
			}
			return result;
		} 
		catch (IOException e) {
			logger.note(LogLocation.MESSAGER, "getResource gave an IOException: " + e.getMessage());
		} 
		return null;
	}

	private CharSequence tryWithReader(FileObject resource) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader reader = new BufferedReader(resource.openReader(true));
		try {
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
		}
		finally {
			reader.close();
		}
		return sb;
	}
}