package com.mashape.filemanager;

import java.io.File;
import java.io.InputStream;

import com.mashape.filemanager.exceptions.FileLoadingException;
import com.mashape.filemanager.exceptions.FileSavingException;

/**
 * Interface to file system.
 * 
 * @author Mashape Inc
 * 
 */
public interface FileManager {

	/**
	 * Gives the file as InputStream from a specific source depending from the
	 * implementation
	 * 
	 * @param fileName
	 *            the file to be loaded
	 * @return the inputStream of the file, if readable.
	 * @throws FileLoadingException
	 *             if errors are found during the processing of the request
	 */
	InputStream loadFile(String fileName) throws FileLoadingException;

	/**
	 * Request to save a File with the specified name.
	 * 
	 * @param fileName
	 * @param file
	 *            the file to be saved
	 * @param contentType
	 *            the content type of <code>file</code>, if applicable
	 * @throws FileSavingException
	 *             if the FileManager instance is unable to save this file
	 */
	void saveFile(String fileName, File file, String contentType)
			throws FileSavingException;

	void moveFile(String source, String dest);

	void delete(String filename);
}
