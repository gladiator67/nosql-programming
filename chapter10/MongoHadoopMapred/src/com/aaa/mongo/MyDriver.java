package com.aaa.mongo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Job;

import com.mongodb.hadoop.MongoInputFormat;
import com.mongodb.hadoop.MongoOutputFormat;
import com.mongodb.hadoop.io.BSONWritable;

public class MyDriver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		// MongoDB의 입출력 경로를 Configuration 객체에 지정한다.
		conf.set("mongo.input.uri", "mongodb://master:27017/INTLCTRY.data");
		conf.set("mongo.output.uri", "mongodb://master:27017/INTLCTRY.data");
		Job job = Job.getInstance(conf, "Map Reduce in MongoDB");
		
		job.setOutputKeyClass(BSONWritable.class);
		job.setOutputValueClass(DoubleWritable.class);
 
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class); 
 
 		// 입출력 형식을 MongoInputFormat.class와 MongoOutputFormat.class로
    // 지정한다. map과 reduce 함수는 데이터를 document로 처리한다.
		job.setInputFormatClass(MongoInputFormat.class);
		job.setOutputFormatClass(MongoOutputFormat.class);
 
		job.setJarByClass(MyDriver.class);
 
		job.submit();
	}

}
