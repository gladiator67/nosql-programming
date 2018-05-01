package com.aaa.mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class MyDriver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		
		// 첫 번째 Job 객체를 생성한다.
		Job job1 = Job.getInstance(conf, "Data Join");
		
		job1.setOutputKeyClass(Text.class);
		job1.setOutputValueClass(Text.class);
 
		job1.setReducerClass(ReducerJoin.class); 
 
		job1.setInputFormatClass(TextInputFormat.class);
		job1.setOutputFormatClass(TextOutputFormat.class);
		
		// 임시 파일을 생성한다.
		FileOutputFormat.setOutputPath(job1, new Path("/home/user01/outputTmp"));
		 
		job1.setJarByClass(MyDriver.class);
		
		//각 Mapper 클래스에 입력 경로를 설정한다.
		MultipleInputs.addInputPath(job1, new Path("/home/user01/data/INTLCTRY/README_TITLE_SORT.txt"),
				TextInputFormat.class, MapperINTL.class);
		MultipleInputs.addInputPath(job1, new Path("/home/user01/data/OECDTTL/README_TITLE_SORT.txt"),
				TextInputFormat.class, MapperOECD.class);
		
		job1.waitForCompletion(true);
		
		// 두 번째 Job을 생성한다.	
		Job job2 = Job.getInstance(conf, "Data Calculate");
		
		job2.setOutputKeyClass(CountryData.class);
		job2.setOutputValueClass(Text.class);
 
		job2.setMapperClass(MapperCalcu.class);
		job2.setReducerClass(ReducerAvg.class); 
 
		job2.setInputFormatClass(TextInputFormat.class);
		job2.setOutputFormatClass(TextOutputFormat.class);
		
		// 임시 파일로부터 데이터를 읽어온다.
		FileInputFormat.addInputPath(job2, new Path("/home/user01/outputTmp"));
		FileOutputFormat.setOutputPath(job2, new Path("/home/user01/output"));
		 
		job2.setJarByClass(MyDriver.class);
		
		job2.waitForCompletion(true);
		
		// 임시파일을 삭제한다.
		FileSystem hdfs = FileSystem.get(conf);
		Path path = new Path("/home/user01/outputTmp");
       
		if(hdfs.exists(path)) {
			hdfs.delete(path, true);
        }
	}

}
