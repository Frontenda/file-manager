package com.mashape.filemanager.filesystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mashape.filemanager.BaseFileManager;
import com.mashape.filemanager.exceptions.FileLoadingException;
import com.mashape.filemanager.exceptions.FileSavingException;

public class FileSystemFileManager extends BaseFileManager {

	private static final String FILEMANAGER_FILESYSTEM_DIR = "filemanager.filesystem.dir";
	private static final String BASE_DIR = "/tmp/fs";
	private String baseDirPath = BASE_DIR;
	private static final Log LOG = LogFactory
			.getLog(FileSystemFileManager.class);

	public FileSystemFileManager(Properties p) {
		super(p);
		if (p != null && p.containsKey(FILEMANAGER_FILESYSTEM_DIR)) {
			baseDirPath = p.getProperty(FILEMANAGER_FILESYSTEM_DIR);
		}
		LOG.trace("Base directory is: " + baseDirPath);
		File baseDir = new File(baseDirPath);
		if (!baseDir.exists()) {
			LOG.trace("Creating baseDir");
			baseDir.mkdirs();
		}
	}

	@Override
	public InputStream loadFile(String fileName) throws FileLoadingException {
		if (isValidFilename(fileName)) {
			InputStream fileStream = null;
			File f = new File(getAbsolutePath(fileName));
			if (f.exists()) {
				try {
					fileStream = new FileInputStream(f);
				} catch (FileNotFoundException e) {
					throw new FileLoadingException(fileName,
							EXCEPTION_EMPTY_STREAM);
				}
			} else {
				throw new FileLoadingException(fileName, EXCEPTION_EMPTY_STREAM);
			}
			return fileStream;
		}
		throw new FileLoadingException(fileName, EXCEPTION_INVALID_FILENAME);
	}

	private boolean isValidFilename(String fileName) {
		return StringUtils.isNotBlank(fileName);
	}

	@Override
	public void saveFile(String fileName, File source)
			throws FileSavingException {
		InputStream dataStream;
		try {
			dataStream = new FileInputStream(source);
		} catch (Exception e1) {
			throw new FileSavingException(e1);
		}
		if (isValidFilename(fileName)) {

			File f = new File(getAbsolutePath(fileName));
			if (!f.getParentFile().exists())
				f.getParentFile().mkdirs();
			OutputStream output;
			try {
				output = new FileOutputStream(f);
				IOUtils.copy(dataStream, output);
			} catch (FileNotFoundException e) {
				throw new FileSavingException(fileName, e);
			} catch (IOException e) {
				throw new FileSavingException(fileName, e);
			}
		} else {
			throw new FileSavingException(fileName, EXCEPTION_INVALID_FILENAME);
		}
	}

	private String getAbsolutePath(String fileName) {
		if (baseDirPath.endsWith(File.separator)
				|| fileName.startsWith(File.separator))
			return baseDirPath + fileName;
		return baseDirPath + File.separator + fileName;
	}

	@Override
	public void moveFile(String source, String dest) {
		File srcPath = new File(getAbsolutePath(source));
		File dstPath = new File(getAbsolutePath(dest));
		if (!dstPath.getParentFile().exists()) {
			dstPath.getParentFile().mkdirs();
		}

		srcPath.renameTo(dstPath);
	}

	@Override
	public void delete(String s) {
		File file = new File(getAbsolutePath(s));
		if (file.exists())
			file.delete();
	}
}