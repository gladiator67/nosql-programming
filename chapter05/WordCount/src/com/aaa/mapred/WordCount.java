package com.aaa.mapred;

import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class WordCount {
	
	public static class MyMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
		 
		 private final static IntWritable outputValue = new IntWritable(1);
		 private Text outputKey = new Text();
	  
	   @Override
		 public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
			 String line = value.toString();
			 StringTokenizer tokenizer = new StringTokenizer(line);
	  
			 while (tokenizer.hasMoreTokens()) {
				 outputKey.set(tokenizer.nextToken());
				 context.write(outputKey, outputValue );
			 }
		 }
	 } 
	  
	public static class MyReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
	 
	  @Override
		public void reduce(Text key, Iterable<IntWritable> values, Context context)	throws IOException, InterruptedException {
			int sum = 0;
			for (IntWritable value : values) {
	         sum += value.get();
			}
			context.write(key, new IntWritable(sum));
		}
	}

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		  
		Job job = Job.getInstance(conf, "wordcount");
	     
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);
	        
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class);
		
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
	  
		FileInputFormat.addInputPath(job, new Path("/home/user01/input"));
		FileOutputFormat.setOutputPath(job, new Path("/home/user01/output"));
	      
		job.submit();
	}

}
