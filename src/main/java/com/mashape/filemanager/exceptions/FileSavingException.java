package com.mashape.filemanager.exceptions;

public class FileSavingException extends FileException {

	private static final long serialVersionUID = 9079498815295238146L;

	public FileSavingException(String fileName, Throwable t) {
		super(fileName, t);
	}

	public FileSavingException(Exception t) {
		super(t);
	}

	public FileSavingException(String fileName, String msg) {
		super(String.format(msg, fileName));
	}

}