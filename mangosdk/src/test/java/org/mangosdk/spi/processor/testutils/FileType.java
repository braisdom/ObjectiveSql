package org.mangosdk.spi.processor.testutils;

public enum FileType {
	VALID, INVALID;
	
	public String getLocation() {
		return name().toLowerCase();
	}
}
