package com.aaa.mapred;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class MyDriver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = new Configuration();
		Job job = Job.getInstance(conf, "Multiple Outputs");
		
		job.setOutputKeyClass(CountryData.class);
		job.setOutputValueClass(Text.class);
 
		job.setMapperClass(MyMapper.class);
		job.setReducerClass(MyReducer.class); 
 
		job.setInputFormatClass(TextInputFormat.class);
 
		FileInputFormat.setInputPaths(job, new Path("/home/user01/data/INTLCTRY/README_TITLE_SORT.txt"));
		FileOutputFormat.setOutputPath(job, new Path("/home/user01/output_INTLCTRY"));

		// 각 나라별로 multi output을 생성한다.		
		MultipleOutputs.addNamedOutput(job, "Australia", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Austria", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Belgium", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Brazil", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Canada", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Chile", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Denmark", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Estonia", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Finland", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "France", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Germany", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Greece", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Hungary", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Iceland", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Indonesia", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Ireland", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Israel", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Italy", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Japan", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Luxembourg", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Mexico", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "NewZealand", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Norway", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Poland", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Portugal", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Slovenia", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "SouthAfrica", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Spain", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Sweden", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Switzerland", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "theCzechRepublic", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "theNetherlands", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "theRepublicofKorea", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "theRussianFederation", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "theSlovakRepublic", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "theUnitedKingdom", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "theUnitedStates", TextOutputFormat.class, CountryData.class, FloatWritable.class);
		MultipleOutputs.addNamedOutput(job, "Turkey", TextOutputFormat.class, CountryData.class, FloatWritable.class);
 
		job.setJarByClass(MyDriver.class);
 
		job.waitForCompletion(true);
	}

}
