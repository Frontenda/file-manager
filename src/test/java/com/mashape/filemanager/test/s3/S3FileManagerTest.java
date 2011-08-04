package com.mashape.filemanager.test.s3;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.jets3t.service.acl.AccessControlList;
import org.jets3t.service.acl.GroupGrantee;
import org.jets3t.service.acl.Permission;
import org.jets3t.service.impl.rest.httpclient.RestS3Service;
import org.jets3t.service.model.S3Bucket;
import org.jets3t.service.model.S3Object;
import org.jets3t.service.security.AWSCredentials;
import org.junit.Before;
import org.junit.Test;

import com.mashape.filemanager.s3.S3FileManager;

public class S3FileManagerTest {

	private S3FileManager fileManager;
	private Properties properties;

	@Before
	public void setUp() throws IOException {
		properties = new Properties();
		properties.load(getClass().getResourceAsStream(
				"/filemanager.s3.properties"));
		this.fileManager = new S3FileManager(properties);
	}

	@Test
	public void testInstance() {
		assertNotNull(this.fileManager);
	}

	@Test
	public void testConfiguration() {
		assertTrue(properties.containsKey("s3.accesskey"));
		assertTrue(properties.containsKey("s3.secretkey"));
		assertTrue(properties.containsKey("s3.bucket"));
	}

	@Test
	public void testFlow() throws Exception {
		String awsAccessKey = properties.getProperty("s3.accesskey");
		String awsSecretKey = properties.getProperty("s3.secretkey");

		AWSCredentials awsCredentials = new AWSCredentials(awsAccessKey,
				awsSecretKey);
		RestS3Service s3Service = new RestS3Service(awsCredentials);
		S3Bucket bucket = s3Service.getBucket(properties
				.getProperty("s3.bucket"));

		assertNotNull(bucket);
		S3Object object = new S3Object(new File("src/test/resources/testFile.txt"));
		object.setKey("test/testFile.txt");
		AccessControlList accessControlList = new AccessControlList();
		accessControlList.setOwner(bucket.getOwner());
		accessControlList.grantPermission(GroupGrantee.ALL_USERS, Permission.PERMISSION_READ);
		
		object.setAcl(accessControlList);
		object = s3Service.putObject(bucket, object);
		s3Service.deleteObject(bucket, object.getKey());
	}
	
	@Test
	public void testFileManagerFlow() throws Exception {
		fileManager.saveFile("test/testFile.txt", new File("src/test/resources/testFile.txt"));
		fileManager.moveFile("test/testFile.txt", "test/testFile2.txt");
		fileManager.delete("test/testFile2.txt");
	}
	
}
