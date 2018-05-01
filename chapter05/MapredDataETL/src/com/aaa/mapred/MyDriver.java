package com.aaa.mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class MyDriver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "Exract Columns");
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
 
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class); 
 
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(TextOutputFormat.class);
 
		FileInputFormat.setInputPaths(job, new Path("/home/user01/data/INTLCTRY/README_TITLE_SORT.txt"));
		FileOutputFormat.setOutputPath(job, new Path("/home/user01/output_INTLCTRY"));
 
		job.setJarByClass(MyDriver.class);
 
		job.waitForCompletion(true);
	}

}
