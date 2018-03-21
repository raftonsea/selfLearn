package com.learn.share;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 2017/12/7 18:13.
 */
public class ShareDemo {

	void synSugar() {

		try (FileInputStream fi = new FileInputStream("")) {

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
