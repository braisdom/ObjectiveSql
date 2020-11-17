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

import junit.framework.Assert;

import org.junit.Test;
import org.mangosdk.spi.processor.testutils.NoOutputTestBase;
import org.mangosdk.spi.processor.testutils.TestLogger;

public class ServiceTest extends NoOutputTestBase {
	
	TestLogger logger = new TestLogger();
	
	@Test(expected=NullPointerException.class)
	public void testConstructorLoggerNull() {
		new Service(null, "service1");
	}
	
	@Test(expected=NullPointerException.class)
	public void testConstructorNameNull() {
		new Service(logger, null);
	}
	
	@Test
	public void testConstructorLogs() {
		new Service(logger, "service1");
		Assert.assertEquals(1, logger.records().size());
		Assert.assertEquals("Creating service1\n", logger.getFileContent());
	}
	
	@Test
	public void testGetName() {
		Service service = new Service(logger, "service1");
		Assert.assertEquals("service1", service.getName());
	}
	
	@Test
	public void testAddProvider() {
		Service service = new Service(logger, "service1");
		logger.reset();
		service.addProvider("provider1");
		Assert.assertEquals(1, logger.records().size());
		Assert.assertEquals("Adding provider1 to service1\n", logger.getFileContent());
	}
	
	@Test(expected=NullPointerException.class)
	public void testAddNullProvider() {
		Service service = new Service(logger, "service1");
		service.addProvider(null);
	}
	
	@Test
	public void testRemoveExistingProvider() {
		Service service = new Service(logger, "service1");
		service.addProvider("provider1");
		logger.reset();
		Assert.assertTrue(service.removeProvider("provider1"));
		Assert.assertEquals(1, logger.records().size());
		Assert.assertEquals("Removing provider1 from service1\n", logger.getFileContent());
	}
	
	@Test
	public void testRemoveNonExistingProvider() {
		Service service = new Service(logger, "service1");
		service.addProvider("provider1");
		logger.reset();
		Assert.assertFalse(service.removeProvider("provider2"));
		Assert.assertTrue(logger.records().isEmpty());
	}
	
	@Test
	public void testContainsExisting() {
		Service service = new Service(logger, "service1");
		service.addProvider("provider1");
		Assert.assertTrue(service.contains("provider1"));
	}	
	
	@Test
	public void testContainsNonExisting() {
		Service service = new Service(logger, "service1");
		service.addProvider("provider1");
		Assert.assertFalse(service.contains("provider2"));
	}	
	
	@Test
	public void testContainsRemoved() {
		Service service = new Service(logger, "service1");
		service.addProvider("provider1");
		Assert.assertTrue(service.contains("provider1"));
		service.removeProvider("provider1");
		Assert.assertFalse(service.contains("provider1"));
	}	
	
	@Test
	public void testToProviderNameListNoProviders() {
		Service service = new Service(logger, "service1");
		String result = service.toProviderNamesList();
		Assert.assertEquals("", result);
	}
	
	@Test
	public void testToProviderNameListOneProvider() {
		Service service = new Service(logger, "service1");
		service.addProvider("provider1");
		String result = service.toProviderNamesList();
		Assert.assertEquals("provider1\n", result);
	}
	
	
	@Test
	public void testToProviderNameListMoreProviders() {
		Service service = new Service(logger, "service1");
		service.addProvider("provider1");
		service.addProvider("provider3");
		service.addProvider("provider2");
		service.addProvider("provider17");
		String result = service.toProviderNamesList();
		Assert.assertEquals("provider1\nprovider17\nprovider2\nprovider3\n", result);
	}
	
	@Test(expected=NullPointerException.class)
	public void testFromProviderNameListNull() {
		new Service(logger, "service1").fromProviderNamesList(null);
	}
	
	@Test
	public void testFromProviderNameListEmpty() {
		Service service = new Service(logger, "service1");
		logger.reset();
		service.fromProviderNamesList("");
		Assert.assertTrue(logger.records().isEmpty());
		Assert.assertEquals("", service.toProviderNamesList());
	}
	
	@Test
	public void testFromProviderNameListJustNewLine() {
		Service service = new Service(logger, "service1");
		logger.reset();
		service.fromProviderNamesList("\n");
		Assert.assertTrue(logger.records().isEmpty());
		Assert.assertEquals("", service.toProviderNamesList());
	}
	
	@Test
	public void testFromProviderNameListOnlyComment() {
		Service service = new Service(logger, "service1");
		logger.reset();
		service.fromProviderNamesList("#hello world");
		Assert.assertTrue(logger.records().isEmpty());
		Assert.assertEquals("", service.toProviderNamesList());
	}
	
	@Test
	public void testFromProviderNameListWhiteSpaceAndComments() {
		Service service = new Service(logger, "service1");
		logger.reset();
		service.fromProviderNamesList("\n\n \n\t\n#hello world\n\n\t# comment\tmore comment\n # no comment\n");
		Assert.assertTrue(logger.records().isEmpty());
		Assert.assertEquals("", service.toProviderNamesList());
	}	
	
	@Test
	public void testFromProviderNameListSingleProvider() {
		Service service = new Service(logger, "service1");
		logger.reset();
		service.fromProviderNamesList("provider1");
		Assert.assertEquals(1, logger.records().size());
		Assert.assertEquals("provider1\n", service.toProviderNamesList());
	}	
	
	@Test
	public void testFromProviderNameListProviderAndComment() {
		Service service = new Service(logger, "service1");
		logger.reset();
		service.fromProviderNamesList("provider1# the provider");
		Assert.assertEquals(1, logger.records().size());
		Assert.assertEquals("provider1\n", service.toProviderNamesList());
	}	
	
	@Test
	public void testFromProviderNameListProviderAndWhiteSpace() {
		Service service = new Service(logger, "service1");
		logger.reset();
		service.fromProviderNamesList("#\n \t provider1\t\n");
		Assert.assertEquals(1, logger.records().size());
		Assert.assertEquals("provider1\n", service.toProviderNamesList());
	}	
	
	@Test
	public void testFromProviderNameListMoreProviders() {
		Service service = new Service(logger, "service1");
		logger.reset();
		service.fromProviderNamesList("provider1\nprovider2\nprovider17\nprovider3");
		Assert.assertEquals(4, logger.records().size());
		Assert.assertEquals("provider1\nprovider17\nprovider2\nprovider3\n", service.toProviderNamesList());
	}		
	
	@Test
	public void testToStringEmpty() {
		new Service(logger, "service1").toString();
	}
	
	@Test
	public void testToStringNotEmpty() {
		Service service = new Service(logger, "service1");
		service.addProvider("testprovider");
		service.toString();
	}
}
