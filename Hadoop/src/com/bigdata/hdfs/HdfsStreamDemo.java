package com.bigdata.hdfs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

public class HdfsStreamDemo {

	//dd
	static void init() {
		System.setProperty("hadoop.home.dir", "E:\\DevelopTool\\Platform\\hadoop\\winutils-master\\hadoop-2.6.4");
	}

	static void create() throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		FSDataOutputStream fsDataOutputStream = fs.create(new Path("/test.txt"), false);
		FileInputStream fis = new FileInputStream("D:\\demo.log");
		IOUtils.copy(fis, fsDataOutputStream);
	}

	static void downLoad() throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		FSDataInputStream fsDataInputStream = fs.open(new Path("/test.txt"));
		FileOutputStream fos = new FileOutputStream("D:\\demo.log1");
		IOUtils.copy(fsDataInputStream, fos);
	}

	static void accessRandom() throws IOException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		FSDataInputStream fsDataInputStream = fs.open(new Path(""));
		fsDataInputStream.seek(12);
		FileOutputStream os = new FileOutputStream("");
		IOUtils.copy(fsDataInputStream, os);
	}

	public static void main(String[] args) throws Exception {
		// downLoad();
		// deleteAll();
		// mkdirs();
		createTheFile();
	}

	static void showBlockInfos() throws IOException {
		init();
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		RemoteIterator<LocatedFileStatus> iterator = fs.listFiles(new Path("/upload"), true);
		while (iterator.hasNext()) {
			LocatedFileStatus fileStatus = iterator.next();
			BlockLocation[] blockLocations = fileStatus.getBlockLocations();
			System.err.println("BLOCK NAME INFOS ------: ");
			for (BlockLocation bl : blockLocations) {
				String[] hosts = bl.getHosts();
				for (String host : hosts) {
					System.err.println(host);
				}
			}
		}
	}

	/**
	 * @author:李小龙
	 * @createTime:2017/9/12 14:50
	 * @discription: 删除全部内容
	 * @modifyTime:
	 * @modifyDiscription:
	 */
	static void deleteAll() throws IOException {
		init();
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		FileStatus[] fileStatuses = fs.listStatus(new Path("/"));
		for (FileStatus fileStatus : fileStatuses) {
			fs.delete(fileStatus.getPath(), true);
		}
	}

	static void deleteTheFile() throws IOException {
		init();
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		fs.delete(new Path(""), true);
	}

	static void mkdirs() throws IOException {
		init();
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		fs.mkdirs(new Path("/learn/mapreduce/flow"));
		fs.close();
	}

	static void createTheFile() throws IOException {
		init();
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		FileSystem fs = FileSystem.get(conf);
		fs.copyFromLocalFile(new Path("C:\\Users\\Administrator\\Desktop\\flow.log"),
				new Path("/learn/mapreduce/flow/flow.log.2"));
	}

}
