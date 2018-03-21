package com.learn.strom.wordcount;

import java.util.Map;
import java.util.Random;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class RandomSpout extends BaseRichSpout {

	SpoutOutputCollector spoutOutputCollector;

	String[] rands = new String[] { "i am ", "tom and ", "i come", "from china", " and i ", "love USA", ", today ",
			"i am learning ", "big data", "by self", "its hard", "but ", "i love", "it ", "ok fight ", "you are ",
			"the ", "best ", "", "", };

	int num = 0;

	@Override
	public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
		this.spoutOutputCollector = spoutOutputCollector;
	}

	@Override
	public void nextTuple() {
		if (num > 5)
			return;
		num = num + 1;
		int rand = new Random().nextInt(rands.length);
		String word = rands[rand];
		spoutOutputCollector.emit(new Values(word));
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
		outputFieldsDeclarer.declare(new Fields("rand_word"));
	}
}
