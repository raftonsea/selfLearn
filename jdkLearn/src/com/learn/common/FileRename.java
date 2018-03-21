package com.learn.common;

import java.io.File;

public class FileRename {

	public static void main(String[] args) {

		File f = new File("E:\\技术视频\\系统架构\\亿级流量电商详情页系统的大型高并发与高可用缓存架构实战第一版附全套资料");

		recusiveFiles(f);
	}

	static void recusiveFiles(File file) {
		if (file.isFile()) {
			String fileName = file.getName();
			fileName = fileName.replace("【W3cjava：www.w3cjava.com】-", "").replace("【W3cjava：www.w3cjava.com】-", "");
			String result = file.getParent() + "\\" + fileName;
			System.err.println(result);
			file.renameTo(new File(result));
		} else {
			File[] files = file.listFiles();
			for (File f : files) {
				recusiveFiles(f);
			}
		}

	}

}
