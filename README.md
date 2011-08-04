Mashape File-Manager library
============================

This library provides an interface ( FileManager.java ) and two different implementations (file system and S3) for standard file operations:
save, move, delete.

Using FileManagerFactory.init(Properties properties) you can load the implementation needed in a transparent way to the rest of the code that uses just the interface.

FileSystemFileManager
---------------------
The first implementation uses the filesystem. The configuration file needs this key: `filemanager.filesystem.dir`. This is the first version we used on mashape.com and we still use in development.

S3FileManager
-------------
The second implementation (the one we use in production) is an abstract way to store and manage file in the Amazon Simple Storage Service ( http://aws.amazon.com/s3 )
It relies on JetS3t ( http://www.jets3t.org/ )
The configuration file needs these keys:
* `s3.accesskey` 
* `s3.secretkey`
* `s3.bucket` 
Where the last one is the bucket name on S3


Copyright (C) 2011 Mashape, Inc.
