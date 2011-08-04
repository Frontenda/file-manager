package com.mashape.filemanager;

import java.util.Properties;

public abstract class BaseFileManager implements FileManager {

	protected static final String EXCEPTION_INVALID_FILENAME = "Invalid Filename: %s";
	protected static final String EXCEPTION_EMPTY_STREAM = "Empty Stream: %s";

	public BaseFileManager(Properties properties) {
		// no need to
	}
}
