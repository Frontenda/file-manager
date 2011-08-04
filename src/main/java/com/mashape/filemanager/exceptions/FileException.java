package com.mashape.filemanager.exceptions;

public class FileException extends Exception {

	private static final long serialVersionUID = 5266700513468958849L;

	public FileException(Exception e) {
		super(e);
	}

	public FileException(String msg) {
		super(msg);
	}

	public FileException(String msg, Throwable t) {
		super(msg, t);
	}

}
