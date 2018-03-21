package com.learn.strom.wordcount;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Tuple;

public class WordCountBolt extends BaseRichBolt {

	HashMap counts = new HashMap();

	OutputCollector outputCollector;

	@Override
	public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
		this.outputCollector = outputCollector;
	}

	@Override
	public void execute(Tuple tuple) {
		String word = tuple.getString(0);
		if (counts.containsKey(word)) {
			int c = (int) counts.get(word);
			counts.put(word, (c + 1));
		} else {
			counts.put(word, 1);
		}

		System.err.println(word + ":" + counts.get(word));

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

	}
}
