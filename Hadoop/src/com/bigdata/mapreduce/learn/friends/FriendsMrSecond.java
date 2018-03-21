package com.bigdata.mapreduce.learn.friends;

import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class FriendsMrSecond {

	static void init() {
		System.setProperty("hadoop.home.dir", "E:\\DevelopEnvironment\\hadoop\\hadoop-2.6.4");
	}

	static class FriendsMrSecondMapper extends Mapper<LongWritable, Text, Text, Text> {

		Text k = new Text();
		Text v = new Text();

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String[] values = value.toString().split(":");
			String friend = values[0];
			String his = values[1];
			String[] fields = his.split(",");
			Arrays.sort(fields);
			for (int i = 0; i < fields.length; i++) {
				for (int j = (i + 1); j < fields.length; j++) {
					String outputkey = fields[i] + fields[j];
					k.set(outputkey);
					v.set(friend);
					context.write(k, v);
				}
			}

		}
	}

	static class FriendsMrSecondReducer extends Reducer<Text, Text, Text, NullWritable> {

		Text k = new Text();
		NullWritable v = NullWritable.get();

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {

			Iterator itr = values.iterator();
			StringBuffer sb = new StringBuffer();
			sb.append(key.toString() + ":");
			while (itr.hasNext()) {
				Text f = (Text) itr.next();
				sb.append(f.toString()).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			k.set(sb.toString());
			context.write(k, v);
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		init();
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		job.setJarByClass(FriendsMrSecond.class);
		job.setMapperClass(FriendsMrSecondMapper.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setReducerClass(FriendsMrSecondReducer.class);
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(NullWritable.class);

		Path in = new Path("C:\\Users\\Administrator\\Desktop\\output");
		Path out = new Path("C:\\Users\\Administrator\\Desktop\\output1");

		FileSystem fs = FileSystem.get(conf);
		if (fs.exists(out))
			fs.delete(out, true);

		FileInputFormat.setInputPaths(job, in);

		FileOutputFormat.setOutputPath(job, out);

		job.waitForCompletion(true);
	}

}
