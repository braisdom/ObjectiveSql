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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class Collector {

	private final Map<String, Service> services = new HashMap<String, Service>();
	
	private final List<String> removed = new ArrayList<String>();
	private final Initializer initializer;
	private final Logger logger;
	
	Collector(Initializer initializer, Logger logger) {
		this.initializer = initializer;
		this.logger = logger;
	}
	
	Service getService(String service) {
		if (service == null) {
			throw new NullPointerException("service");
		}
		if (!services.containsKey(service)) {
			Service newService = new Service(logger, service);
			CharSequence initialData = initializer.initialData(service);
			if (initialData != null) {
				newService.fromProviderNamesList(initialData.toString());
				for (String provider : removed) {
					newService.removeProvider(provider);
				}
			}
			services.put(service, newService);
		}
		return services.get(service);
	}
	
	Collection<Service> services() {
		return Collections.unmodifiableMap(services).values();
	}
	
	void removeProvider(String provider) {
		if (provider == null) {
			throw new NullPointerException("provider");
		}
		logger.note(LogLocation.LOG_FILE, "Removing " + provider);
		removed.add(provider);
		for (Service service : services.values()) {
			service.removeProvider(provider);
		}
	}
	
	@Override
	public String toString() {
		return services.values().toString();
	}
}
