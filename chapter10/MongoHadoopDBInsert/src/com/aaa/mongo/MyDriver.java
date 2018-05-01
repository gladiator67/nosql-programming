package com.aaa.mongo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;

public class MyDriver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		// 출력할 MongoDB의 경로를 지정한다.
		conf.set("mongo.output.uri", "mongodb://127.0.0.1:27017/INTLCTRY.data");
		Job job = Job.getInstance(conf, "Insert to MongoDB");
		
		job.setOutputKeyClass(BSONWritable.class);
		job.setOutputValueClass(BSONWritable.class);
 
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class); 
 
		job.setInputFormatClass(TextInputFormat.class);
		job.setOutputFormatClass(MongoOutputFormat.class);
 
		FileInputFormat.setInputPaths(job, new Path("/home/user01/data/INTLCTRY/README_TITLE_SORT.txt"));
 
		job.setJarByClass(MyDriver.class);
 
		job.waitForCompletion(true);
	}

}
