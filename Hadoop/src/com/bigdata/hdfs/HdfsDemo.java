package com.bigdata.hdfs;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.apache.hadoop.hdfs.DistributedFileSystem;

public class HdfsDemo {


	//idea  2
	//idea   1
	static void downLoadFile() throws IOException {
		Configuration conf = new Configuration();
		conf.set("idea","idea");
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		DistributedFileSystem fs = (DistributedFileSystem) FileSystem.get(conf);
		fs.copyToLocalFile(false, new Path("/upload/demo.log"), new Path("D:\\demo1.log"), true);
		fs.close();
	}

 
	static void uploadFile() throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		fs.copyFromLocalFile(new Path("D:\\demo.log"), new Path("/upload/demo.log"));
		fs.close();
	}

	static void listFiles() throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		RemoteIterator<LocatedFileStatus> iterator = fs.listFiles(new Path("/"), true);
		while (iterator.hasNext()) {
			LocatedFileStatus status = iterator.next();
			String fileName = status.getPath().getName();
			System.err.println(fileName);
		}
	}

	static void getFile() throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		FileStatus fileStatus = fs.getFileStatus(new Path("/"));
		System.err.println(fileStatus.getPath().getName());
	}

	public static void main(String[] args) throws Exception {
		// uploadFile();
		// downLoadFile();
		// mkdirs();
		// ls();
		downLoadFile();
	}

	static void mkdirs() throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		boolean mkdirs = fs.mkdirs(new Path("/testmkdirs"));
		System.err.println(mkdirs);
	}

	static void ls() throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000////");
		FileSystem fs = FileSystem.get(conf);
		FileStatus[] statuses = fs.listStatus(new Path("/"));
		for (FileStatus fileStatus : statuses) {

			String name = fileStatus.getPath().getName();
			short replication = fileStatus.getReplication();
			if (fileStatus.isFile()) {
				System.err.println(name + " is file");
			} else {
				System.err.println(name + " is directory");
			}
		}
	}

	static void cat() {

	}
}
