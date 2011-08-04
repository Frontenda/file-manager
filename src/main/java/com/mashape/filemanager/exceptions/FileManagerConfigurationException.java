package com.mashape.filemanager.exceptions;

public class FileManagerConfigurationException extends Exception {

	private static final long serialVersionUID = -7861188583450994305L;

	public FileManagerConfigurationException(Exception e) {
		super(e);
	}

	public FileManagerConfigurationException(String message) {
		super(message);
	}

}