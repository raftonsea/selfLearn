package com.bigdata.mapreduce.learn.maxorder;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Partitioner;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class MaxOrderRunner {

	static class OrderBean implements WritableComparable<OrderBean> {
		// Order_0000003,Pdt_01,322.8
		String orderId;
		String pId;
		double price;

		public OrderBean() {
		}

		public OrderBean(String orderId, String pId, double price) {
			this.orderId = orderId;
			this.pId = pId;
			this.price = price;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public String getpId() {
			return pId;
		}

		public void setpId(String pId) {
			this.pId = pId;
		}

		public double getPrice() {
			return price;
		}

		public void setPrice(double price) {
			this.price = price;
		}

		@Override
		public int compareTo(OrderBean o) {
			if (o.getOrderId().equals(this.getOrderId())) {
				return (o.getPrice() > this.getPrice()) ? 1 : -1;
			}
			return o.getOrderId().compareTo(this.getOrderId());

		}

		@Override
		public void write(DataOutput out) throws IOException {
			out.writeUTF(this.getOrderId());
			out.writeUTF(this.getpId());
			out.writeDouble(this.getPrice());
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			this.setOrderId(in.readUTF());
			this.setpId(in.readUTF());
			this.setPrice(in.readDouble());
		}

		@Override
		public String toString() {
			return " {" + "orderId ： '" + orderId + '\'' + ", pId ： '" + pId + '\'' + ", price ： " + price + '}';
		}
	}

	static class MaxOrderMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {
		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			String[] fields = value.toString().split(",");
			OrderBean bean = new OrderBean(fields[0], fields[1], Double.valueOf(fields[2]));
			context.write(bean, NullWritable.get());
		}
	}

	static class MaxOrderReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {

		@Override
		protected void reduce(OrderBean key, Iterable<NullWritable> values, Context context)
				throws IOException, InterruptedException {
			context.write(key, NullWritable.get());
		}
	}

	static void init() {
		System.setProperty("hadoop.home.dir", "E:\\DevelopEnvironment\\hadoop\\hadoop-2.6.4");
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		init();
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		job.setJarByClass(MaxOrderRunner.class);

		job.setMapperClass(MaxOrderMapper.class);
		job.setMapOutputKeyClass(OrderBean.class);
		job.setMapOutputValueClass(NullWritable.class);

		job.setReducerClass(MaxOrderReducer.class);
		job.setOutputKeyClass(OrderBean.class);
		job.setOutputValueClass(NullWritable.class);

		job.setNumReduceTasks(5);
		job.setPartitionerClass(OrderPatition.class);
		job.setGroupingComparatorClass(OrderBeanGroupingComparator.class);

		Path in = new Path("C:\\Users\\Administrator\\Desktop\\srcdata\\gpinput");
		Path out = new Path("C:\\Users\\Administrator\\Desktop\\output");

		FileSystem fs = FileSystem.get(conf);
		if (fs.exists(out))
			fs.delete(out, true);

		FileInputFormat.setInputPaths(job, in);
		FileOutputFormat.setOutputPath(job, out);

		job.waitForCompletion(true);
	}
}

class OrderPatition extends Partitioner<MaxOrderRunner.OrderBean, NullWritable> {
	@Override
	public int getPartition(MaxOrderRunner.OrderBean orderBean, NullWritable nullWritable, int numPartitions) {
		return (orderBean.getOrderId().hashCode() & Integer.MAX_VALUE) % numPartitions;
	}
}

class OrderBeanGroupingComparator extends WritableComparator {

	public OrderBeanGroupingComparator() {
		super(MaxOrderRunner.OrderBean.class, true);
	}

	@Override
	public int compare(WritableComparable a, WritableComparable b) {
		MaxOrderRunner.OrderBean bean1 = (MaxOrderRunner.OrderBean) a;
		MaxOrderRunner.OrderBean bean2 = (MaxOrderRunner.OrderBean) b;
		return bean1.getOrderId().compareTo(bean2.getOrderId());
	}
}