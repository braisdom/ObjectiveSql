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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

final class Service {
	
	private final Logger logger;
	private final String serviceName;
	private final Set<String> providers = new HashSet<String>();

	Service(Logger logger, String name) {
		if (logger == null) {
			throw new NullPointerException("logger");
		}
		if (name == null) {
			throw new NullPointerException("name");
		}
		this.logger = logger;
		logger.note(LogLocation.LOG_FILE, "Creating " + name);
		this.serviceName = name;
	}

	void addProvider(String provider) {
		if (provider == null) {
			throw new NullPointerException("provider");
		}
		logger.note(LogLocation.LOG_FILE, "Adding " + provider + " to " + serviceName);
		providers.add(provider);
	}
	
	boolean contains(String provider) {
		return providers.contains(provider);
	}
	
	boolean removeProvider(String provider) {
		if (providers.remove(provider)) {
			logger.note(LogLocation.LOG_FILE, "Removing " + provider + " from " + serviceName);
			return true;
		}
		return false;
	}
	
	String getName() {
		return serviceName;
	}
	
	String toProviderNamesList() {
		StringBuilder sb = new StringBuilder();
		List<String> names = new ArrayList<String>(providers);
		Collections.sort(names);
		for (String provider : names) {
			sb.append(provider).append("\n");
		}
		return sb.toString();
	}
	
	void fromProviderNamesList(String input) {
		if (input == null) {
			throw new NullPointerException("input");
		}
		String[] lines = input.split("\\n");
		for (String line : lines) {
			String[] content = line.split("#");
			if (content.length > 0) {
				String trimmed = content[0].trim();
				if (trimmed.length() > 0) {
					addProvider(trimmed);
				}
			}
		}
	}
	
	@Override
	public String toString() {
		return serviceName + "=" + providers;
	}
}