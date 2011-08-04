package com.mashape.filemanager.exceptions;

public class FileLoadingException extends FileException {

	private static final long serialVersionUID = 9079498815295238146L;

	public FileLoadingException(String fileName, String msg) {
		super(String.format(msg, fileName));
	}

}
