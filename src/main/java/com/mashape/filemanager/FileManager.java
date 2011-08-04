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
	 * @return the inputStrem of the file, if readable.
	 * @throws FileLoadingException
	 *             if errors are found during the processing of the request
	 */
	InputStream loadFile(String fileName) throws FileLoadingException;

	/**
	 * Request to save a File with the specified name.
	 * 
	 * @param fileName
	 * @param input
	 * @throws FileSavingException
	 */
	void saveFile(String fileName, File file)
			throws FileSavingException;

	void moveFile(String source, String dest);

	void delete(String filename);
}
