package com.bigdata.mapreduce.learn.friends;

import java.io.IOException;
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

public class FriendsMR {

	static void init() {
		System.setProperty("hadoop.home.dir", "E:\\DevelopEnvironment\\hadoop\\hadoop-2.6.4");
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		init();
		Path inpath = new Path("C:\\Users\\Administrator\\Desktop\\srcdata\\friends");
		Path outpath = new Path("C:\\Users\\Administrator\\Desktop\\output");

		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		job.setJarByClass(FriendsMR.class);

		job.setMapperClass(FriendsMRMap.class);
		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setReducerClass(FriendMRReduce.class);
		job.setOutputKeyClass(NullWritable.class);
		job.setOutputValueClass(Text.class);

		FileInputFormat.setInputPaths(job, inpath);

		FileSystem fs = FileSystem.get(conf);
		if (fs.exists(outpath))
			fs.delete(outpath, true);
		FileOutputFormat.setOutputPath(job, outpath);

		job.waitForCompletion(true);

	}

	private static class FriendsMRMap extends Mapper<LongWritable, Text, Text, Text> {

		// A:B,C,D,F,E,O
		// B:A,C,E,K
		// C:F,A,D,I
		// D:A,E,F,L
		// E:B,C,D,M,L
		// F:A,B,C,D,E,O,M
		// G:A,C,D,E,F
		// H:A,C,D,E,O
		// I:A,O
		// J:B,O
		// K:A,C,D
		// L:D,E,F
		// M:E,F,G
		// O:A,H,I,J
		Text k = new Text();
		Text v = new Text();

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String[] values = value.toString().split(":");
			String me = values[0];
			String[] friends = values[1].split(",");
			for (String friend : friends) {
				k.set(friend);
				v.set(me);
				context.write(k, v);
			}
		}

	}

	private static class FriendMRReduce extends Reducer<Text, Text, NullWritable, Text> {

		Text k = new Text();
		Text v = new Text();

		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			StringBuffer sb = new StringBuffer();
			Iterator itr = values.iterator();
			sb.append(key.toString() + ":");
			while (itr.hasNext()) {
				Text next = (Text) itr.next();
				sb.append(next.toString()).append(",");
			}
			sb.deleteCharAt(sb.length() - 1);
			v.set(sb.toString());
			context.write(NullWritable.get(), v);
		}
	}

}
