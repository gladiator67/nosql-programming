package com.aaa.mapred;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MapperCalcu extends Mapper<LongWritable, Text, CountryData, Text> {

	CountryData outputKey = new CountryData();
	Text outputValue = new Text();

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		String[] columns = value.toString().split(",");
		
		outputKey.setCountry(columns[0]);
		outputKey.setTitle(columns[1]);
		outputKey.setAge(columns[2]);
		outputKey.setSex(columns[3]);
		outputKey.setUnits(columns[4]);
		outputKey.setFrequency(columns[5].charAt(0));
		outputKey.setSeasonalAdjust(columns[6]);
		outputKey.setUpdateDate(columns[7]);
		
		outputValue.set(columns[8]+","+columns[9]);
		
		context.write(outputKey, outputValue);
	}
}
