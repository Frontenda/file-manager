package com.mashape.filemanager.exceptions;

public class FileReadingException extends FileException {

	private static final long serialVersionUID = 9079498815295238146L;

	public FileReadingException(Exception e) {
		super(e);
	}
}
