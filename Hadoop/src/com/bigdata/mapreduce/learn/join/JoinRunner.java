package com.bigdata.mapreduce.learn.join;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class JoinRunner {

	static void init() {
		System.setProperty("hadoop.home.dir", "E:\\DevelopEnvironment\\hadoop\\hadoop-2.6.4");
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException,
			InvocationTargetException, IllegalAccessException {

		init();
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf);

		job.setMapperClass(JoinRunnerMap.class);
		job.setReducerClass(JoinRunnerReducer.class);

		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(JoinBean.class);
		job.setOutputKeyClass(JoinBean.class);
		job.setOutputValueClass(NullWritable.class);

		FileInputFormat.setInputPaths(job, new Path("C:\\Users\\Administrator\\Desktop\\source"));
		Path outputDir = new Path("C:\\Users\\Administrator\\Desktop\\target");
		FileSystem fs = FileSystem.get(conf);
		if (fs.exists(outputDir)) {
			fs.delete(outputDir, true);
		}
		job.setNumReduceTasks(5);
		job.setOutputFormatClass(TextOutputFormat.class);
		FileOutputFormat.setOutputPath(job, outputDir);
		job.waitForCompletion(true);
	}

	static class JoinBean implements Writable {

		@Override
		public void write(DataOutput out) throws IOException {
			out.writeUTF(this.orderId);
			out.writeUTF(this.cdate);
			out.writeUTF(this.pId);
			out.writeUTF(this.pName);
			out.writeUTF(this.price);
			out.writeUTF(this.flag);
		}

		public JoinBean() {
		}

		public JoinBean(String orderId, String cdate, String pId, String pName, String price, String flag) {
			this.orderId = orderId;
			this.cdate = cdate;
			this.pId = pId;
			this.pName = pName;
			this.price = price;
			this.flag = flag;
		}

		public void setJoinBean(String orderId, String cdate, String pId, String pName, String price, String flag) {
			this.orderId = orderId;
			this.cdate = cdate;
			this.pId = pId;
			this.pName = pName;
			this.price = price;
			this.flag = flag;
		}

		@Override
		public void readFields(DataInput in) throws IOException {
			this.orderId = in.readUTF();
			this.cdate = in.readUTF();
			this.pId = in.readUTF();
			this.pName = in.readUTF();
			this.price = in.readUTF();
			this.flag = in.readUTF();

		}

		private String orderId;
		private String cdate;
		private String pId;
		private String pName;
		private String price;
		private String flag;

		public String getFlag() {
			return flag;
		}

		public void setFlag(String flag) {
			this.flag = flag;
		}

		public String getOrderId() {
			return orderId;
		}

		public void setOrderId(String orderId) {
			this.orderId = orderId;
		}

		public String getCdate() {
			return cdate;
		}

		public void setCdate(String cdate) {
			this.cdate = cdate;
		}

		public String getpId() {
			return pId;
		}

		public void setpId(String pId) {
			this.pId = pId;
		}

		public String getpName() {
			return pName;
		}

		public void setpName(String pName) {
			this.pName = pName;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		@Override
		public String toString() {
			return "orderId : '" + orderId + ", cdate : " + cdate + ", pId : " + pId + ", pName : " + pName
					+ ", price: " + price;
		}
	}

	static class JoinRunnerMap extends Mapper<LongWritable, Text, Text, JoinBean> {

		String fileName = "";

		JoinBean joinBean = new JoinBean();

		Text rk = new Text();

		@Override
		protected void setup(Context context) throws IOException, InterruptedException {
			FileSplit inputSplit = (FileSplit) context.getInputSplit();
			fileName = inputSplit.getPath().getName();
		}

		@Override
		protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

			String line = value.toString();
			String[] lines = line.split("\t");
			String pId = "";
			if (fileName.contains("order")) {
				pId = lines[2];
				String orderId = lines[0];
				String cdate = lines[1];
				joinBean.setJoinBean(orderId, cdate, pId, "", "", "0");
			}

			if (fileName.contains("product")) {
				pId = lines[0];
				String pName = lines[1];
				String price = lines[2];
				joinBean.setJoinBean("", "", pId, pName, price, "1");
			}

			rk.set(pId);
			context.write(rk, joinBean);
		}
	}

	static class JoinRunnerReducer extends Reducer<Text, JoinBean, JoinBean, NullWritable> {

		@Override
		protected void reduce(Text key, Iterable<JoinBean> values, Context context)
				throws IOException, InterruptedException {
			Iterator iterator = values.iterator();

			List list = new ArrayList();

			JoinBean pjb = new JoinBean();
			while (iterator.hasNext()) {
				JoinBean jb = (JoinBean) iterator.next();
				if ("1".equals(jb.getFlag())) {
					try {
						pjb.setpName(jb.getpName());
						pjb.setPrice(jb.getPrice());
						pjb.setpId(jb.getpId());
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					try {
						JoinBean ojb = new JoinBean();
						ojb.setOrderId(jb.getOrderId());
						ojb.setCdate(jb.getCdate());
						ojb.setpId(jb.getpId());
						list.add(ojb);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			for (int i = 0; i < list.size(); i++) {
				JoinBean bean = (JoinBean) list.get(i);
				bean.setpName(pjb.getpName());
				bean.setPrice(pjb.getPrice());
				context.write(bean, NullWritable.get());
			}

		}
	}

}
