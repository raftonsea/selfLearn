package com.learn.strom.wordcount;

import java.util.concurrent.TimeUnit;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.AuthorizationException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class WCRunner {

	public static void main(String[] args)
			throws InterruptedException, InvalidTopologyException, AuthorizationException, AlreadyAliveException {

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("spout", new RandomSpout(), 1);
		builder.setBolt("split", new SplitBolt(), 1).shuffleGrouping("spout");
		builder.setBolt("wordcount", new WordCountBolt(), 1).fieldsGrouping("split", new Fields("split_words"));

		Config conf = new Config();
		conf.setDebug(false);

		if (args != null && args.length > 0) {
			conf.setNumWorkers(3);
			StormSubmitter.submitTopologyWithProgressBar(args[0], conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(3);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology("word-count", conf, builder.createTopology());
		}
		TimeUnit.HOURS.sleep(1);
	}

}
