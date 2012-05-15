package com.mashape.filemanager.s3;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jets3t.service.S3Service;
import org.jets3t.service.S3ServiceException;
import org.jets3t.service.ServiceException;
import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.GroupGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;

import com.mashape.filemanager.BaseFileManager;
import com.mashape.filemanager.FileManagerFactory;
import com.mashape.filemanager.exceptions.FileLoadingException;
import com.mashape.filemanager.exceptions.FileSavingException;

public class S3FileManager extends BaseFileManager {

	private static final String AWS_PROPERTIES = "/aws.properties";

	private AWSCredentials awsCredentials;
	private S3Service s3Service;
	private S3Bucket bucket;
	private static final Log LOG = LogFactory.getLog(S3FileManager.class);

	public S3FileManager(Properties properties) {
		super(properties);
		String awsAccessKey = properties.getProperty("s3.accesskey");
		String awsSecretKey = properties.getProperty("s3.secretkey");
		
		// fallback
		if(awsAccessKey == null) {
			Properties awsProperties = new Properties();
			try {
				InputStream inputStream = FileManagerFactory.class
						.getResourceAsStream(AWS_PROPERTIES);
				if (inputStream != null) {
					awsProperties.load(inputStream);
					awsAccessKey = properties.getProperty("aws.accesskey");
					awsSecretKey = properties.getProperty("aws.secretkey");
				}
			} catch (Exception e) {
				LOG.error("Error loading AWS configuration", e);
			}
		}
			
		awsCredentials = new AWSCredentials(awsAccessKey, awsSecretKey);
		try {
			s3Service = new RestS3Service(awsCredentials);
			bucket = s3Service.getBucket(properties.getProperty("s3.bucket"));
		} catch (S3ServiceException e) {
			LOG.fatal("error connecting to S3", e);
		}
	}

	@Override
	public InputStream loadFile(String fileName) throws FileLoadingException {
		S3Object obj;
		try {
			obj = s3Service.getObject(bucket.getName(), getFsName(fileName));
			return obj.getDataInputStream();
		} catch (S3ServiceException e) {
			throw new FileLoadingException(fileName, e.getMessage());
		} catch (ServiceException e) {
			throw new FileLoadingException(fileName, e.getMessage());
		}
	}

	@Override
	public void saveFile(String fileName, File file, String contentType) throws FileSavingException {

		S3Object object;
		try {
			object = new S3Object(file);
		} catch (Exception e) {
			throw new FileSavingException(e);
		}

		object.setKey(getFsName(fileName));
		object.setContentType(contentType);
		AccessControlList accessControlList = new AccessControlList();
		accessControlList.setOwner(bucket.getOwner());
		accessControlList.grantPermission(GroupGrantee.ALL_USERS,
				Permission.PERMISSION_READ);
		
		object.setAcl(accessControlList);
		try {
			object = s3Service.putObject(bucket, object);
		} catch (S3ServiceException e) {
			throw new FileSavingException(e);
		}
	}

	private String getFsName(String fileName) {
		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1);
		}
		return fileName;
	}

	@Override
	public void moveFile(String source, String dest) {
		try {
			S3Object object = new S3Object(getFsName(dest));
			AccessControlList accessControlList = new AccessControlList();
			accessControlList.setOwner(bucket.getOwner());
			accessControlList.grantPermission(GroupGrantee.ALL_USERS,
					Permission.PERMISSION_READ);
			object.setAcl(accessControlList);

			s3Service.renameObject(bucket.getName(), getFsName(source),
					object);
			
		} catch (ServiceException e) {
			LOG.error("error renaming file " + source + " to " + dest, e);
		}
	}

	@Override
	public void delete(String filename) {
		try {
			s3Service.deleteObject(bucket, getFsName(filename));
		} catch (S3ServiceException e) {
			LOG.error("error deleting file " + filename, e);
		}
	}

}
