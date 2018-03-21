package com.bigdata.hive.udf;

import java.util.HashMap;
import java.util.Map;

import org.apache.hadoop.hive.ql.exec.UDF;

public class ToLower extends UDF {

	static Map phoneMap = new HashMap();

	static {
		phoneMap.put("13211112220", "济南");
		phoneMap.put("13211112221", "德州");
		phoneMap.put("13211112222", "泰安");
		phoneMap.put("13211112223", "滨州");
		phoneMap.put("13211112224", "东营");
	}

	public String evaluate(String s) {
		return s.toLowerCase();
	}

	public String evaluate(Integer phone) {
		String p = String.valueOf(phone);
		String d = (String) phoneMap.get(p);
		return d;
	}
}
