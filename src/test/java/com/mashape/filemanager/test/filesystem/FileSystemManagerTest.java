package com.mashape.filemanager.test.filesystem;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.mashape.filemanager.FileManager;
import com.mashape.filemanager.exceptions.FileLoadingException;
import com.mashape.filemanager.exceptions.FileSavingException;
import com.mashape.filemanager.filesystem.FileSystemFileManager;

public class FileSystemManagerTest {

	private FileManager manager;

	@Before
	public void setUp() {
		Properties p = new Properties();
		p.put("filemanager.filesystem.dir", ".");
		this.manager = new FileSystemFileManager(p);
	}

	@Test
	public void testReadFile() throws FileLoadingException, IOException {
		InputStream inputStream = manager.loadFile("pom.xml");
		assertNotNull(inputStream);
		assertTrue(inputStream.available() > 0);
	}

	@Test
	public void testSaveFile() throws FileLoadingException, FileSavingException {
		File source = new File("pom.xml");
		manager.saveFile("tempFile", source);
		File f = new File("./tempFile");
		assertTrue(f.exists());
		assertTrue(f.delete());
	}

	@Test(expected = FileSavingException.class)
	public void testSaveFileException() throws FileSavingException {
		manager.saveFile("", null);
	}

	@Test(expected = FileSavingException.class)
	public void testSaveFileException1() throws FileSavingException {
		manager.saveFile("pippo", null);
	}

}
