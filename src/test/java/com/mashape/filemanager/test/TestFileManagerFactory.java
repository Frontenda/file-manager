package com.mashape.filemanager.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Test;

import com.mashape.filemanager.FileManager;
import com.mashape.filemanager.FileManagerFactory;
import com.mashape.filemanager.exceptions.FileManagerConfigurationException;
import com.mashape.filemanager.filesystem.FileSystemFileManager;

public class TestFileManagerFactory {

	@Test
	public void testFileSystemFactory() throws FileManagerConfigurationException {
		Properties prop = new Properties();
		prop.setProperty("filemanager", "com.mashape.filemanager.filesystem.FileSystemFileManager");
		FileManagerFactory.init(prop);
		FileManagerFactory fileLoaderFactory = FileManagerFactory
		.getInstance();
		assertNotNull(fileLoaderFactory);

		FileManager fileLoader = fileLoaderFactory.getFileManager();
		assertNotNull(fileLoader);
		assertEquals(FileSystemFileManager.class, fileLoader.getClass());
	}
	
}