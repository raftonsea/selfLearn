package com.bigdata.mapreduce.learn.flow;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FlowMR {

	static void init() {
		System.setProperty("hadoop.home.dir", "E:\\DevelopTool\\Platform\\hadoop\\hadoop-2.6.4\\");
	}

	public static void main(String[] args) throws Exception {
		init();

		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", "hdfs://hadoop04:9000/");
		conf.set("mapreduce.framework.name", "yarn");
		conf.set("yarn.resourcemanager.hostname", "hadoop04");
		Job job = Job.getInstance(conf);

		// job.setJarByClass(FlowMR.class);

		job.setJar("E:\\workspace-intellij\\GIT\\selfLearn\\out\\artifacts\\flow\\flow.jar");

		job.setMapperClass(FlowMapper.class);
		job.setReducerClass(FlowReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(FlowBean.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		Path outputDir = new Path(args[1]);
		FileSystem fs = FileSystem.get(conf);
		if (fs.exists(outputDir)) {
			fs.delete(outputDir, true);
		}
		FileOutputFormat.setOutputPath(job, outputDir);
		job.waitForCompletion(true);
	}

	private static class FlowMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String[] datas = value.toString().split("\t");
			String phone = datas[1];
			String upFlow = datas[datas.length - 3];
			String downFlow = datas[datas.length - 2];

			FlowBean fb = new FlowBean(phone, Long.valueOf(upFlow), Long.valueOf(downFlow));
			context.write(new Text(phone), fb);
		}
	}

	private static class FlowReducer extends Reducer<Text, FlowBean, Text, LongWritable> {

		@Override
		protected void reduce(Text key, Iterable<FlowBean> values, Context context)
				throws IOException, InterruptedException {

			long sumsSunFlow = 0;
			String phone = key.toString();
			Iterator itr = values.iterator();
			while (itr.hasNext()) {
				FlowBean flowBean = (FlowBean) itr.next();
				long sumFlow = flowBean.getSumFlow();
				sumsSunFlow = sumsSunFlow + sumFlow;
			}
			context.write(new Text(phone), new LongWritable(sumsSunFlow));

		}
	}

	private static class FlowBean implements Writable {

		public String getTelphone() {
			return telphone;
		}

		public void setTelphone(String telphone) {
			this.telphone = telphone;
		}

		public FlowBean() {
		}

		public long getUpFlow() {
			return upFlow;
		}

		public void setUpFlow(long upFlow) {
			this.upFlow = upFlow;
		}

		public long getDownFlow() {
			return downFlow;
		}

		public void setDownFlow(long downFlow) {
			this.downFlow = downFlow;
		}

		public long getSumFlow() {
			return sumFlow;
		}

		public void setSumFlow(long sumFlow) {
			this.sumFlow = sumFlow;
		}

		private String telphone;
		private long upFlow;
		private long downFlow;
		private long sumFlow;

		public FlowBean(String telphone, long upFlow, long downFlow) {
			this.telphone = telphone;
			this.upFlow = upFlow;
			this.downFlow = downFlow;
			this.sumFlow = this.upFlow + this.downFlow;
		}

		@Override
		public void write(DataOutput out) throws IOException {
			out.writeUTF(this.telphone);
			out.writeLong(this.upFlow);
			out.writeLong(this.downFlow);
			out.writeLong(this.sumFlow);
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			this.telphone = in.readUTF();
			this.upFlow = in.readLong();
			this.downFlow = in.readLong();
			this.sumFlow = in.readLong();
		}
	}

	private class MyPartion extends Partitioner<Text, FlowBean> {
		@Override
		public int getPartition(Text text, FlowBean flowBean, int numPartitions) {
			return 0;
		}
	}
}
