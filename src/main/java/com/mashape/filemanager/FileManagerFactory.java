package com.mashape.filemanager;

import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mashape.filemanager.exceptions.FileManagerConfigurationException;

public final class FileManagerFactory {

	private static final String DEFAULT_FILEMANAGER_PROPERTIES = "/filemanager.properties";
	private static final Log LOG = LogFactory.getLog(FileManagerFactory.class);

	private static FileManagerFactory instance;
	private Properties properties;
	private FileManager fileManager;

	private FileManagerFactory() {
		// no need to be public
	}

	public static void init(Properties properties)
			throws FileManagerConfigurationException {
		LOG.info("Initializing FileManagerFactory");
		instance = new FileManagerFactory();
		instance.properties = properties;
		instance.initManager();
	}

	@SuppressWarnings("unchecked")
	private void initManager() throws FileManagerConfigurationException {
		try {
			Class<? extends BaseFileManager> fsManagerClass = (Class<? extends BaseFileManager>) Class
					.forName(properties.getProperty("filemanager"));
			this.fileManager = fsManagerClass.getConstructor(Properties.class)
					.newInstance(properties);
		} catch (Exception e) {
			LOG.fatal(properties.getProperty("filemanager"), e);
		}

	}

	public static synchronized FileManagerFactory getInstance() {
		if (instance == null) {
			Properties prop = new Properties();
			try {
				InputStream inputStream = FileManagerFactory.class
						.getResourceAsStream(DEFAULT_FILEMANAGER_PROPERTIES);
				if (inputStream != null) {
					prop.load(inputStream);
					init(prop);
				}
			} catch (Exception e) {
				LOG.error("Error loading default configuration", e);
			}
		}
		return instance;
	}

	public FileManager getFileManager() {
		return fileManager;
	}
}