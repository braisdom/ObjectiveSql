package org.mangosdk.spi.processor;

public enum LogLocation {
	MESSAGER, 
	LOG_FILE,
	BOTH;

	public boolean toMessager() {
		return this != LOG_FILE;
	}
	
	public boolean toLogFile() {
		return this != MESSAGER;
	}
}
