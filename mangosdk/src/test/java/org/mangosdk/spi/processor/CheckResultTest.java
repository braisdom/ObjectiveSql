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

import org.junit.Test;
import org.mangosdk.spi.processor.testutils.NoOutputTestBase;

import junit.framework.Assert;

public class CheckResultTest extends NoOutputTestBase {
	
	@Test(expected=NullPointerException.class)
	public void testNullMessage() {
		CheckResult.valueOf(null);
	}
	
	@Test
	public void testSomeMessage() {
		CheckResult result = CheckResult.valueOf("Error");
		Assert.assertTrue(result.isError());
		Assert.assertEquals("Error", result.getMessage());
	}
	
	@Test
	public void testOK() {
		CheckResult result = CheckResult.OK;
		Assert.assertFalse(result.isError());
		Assert.assertNull(result.getMessage());
	}
}
