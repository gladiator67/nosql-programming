package com.aaa.mapred;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class MyReducer extends Reducer<CountryData, Text, CountryData, FloatWritable> {

	// 다중 출력에 필요한 MultiOutputs 객체를 필드로 선언한다.
	private MultipleOutputs<CountryData, FloatWritable> mos;
	
	// Reducer가 처음 실행되면서 호출되는 setup 메서드에서 mos 객체를 생성한다.
	@Override
	public void setup(Context context) throws IOException, InterruptedException{
		mos = new MultipleOutputs<CountryData, FloatWritable>(context);
    }
   
  // Reducer가 종료되면서 호출하는 cleanup 메서드를 overriding하여 mos 객체의 output을 종료한다. 
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException{
		mos.close();
    }

	@Override
	public void reduce(CountryData key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		
		float sum = 0;
		int count = 0;
       
		for (Text value : values) {
			String[] columns = value.toString().trim().split(",");
			
			if(columns != null && columns.length == 2 && !columns[1].equals(".")) {
				count++;
				sum += Float.parseFloat(columns[1]);
			}
		}
		
		// 각 나라별로 분리하여 출력을 한다.		
		if(key.getCountry().equals("Australia©")) {
			mos.write("Australia", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Austria©")) {
			mos.write("Austria", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Belgium©")) {
			mos.write("Belgium", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Brazil©")) {
			mos.write("Brazil", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Canada©")) {
			mos.write("Canada", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Chile©")) {
			mos.write("Chile", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Denmark©")) {
			mos.write("Denmark", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Estonia©")) {
			mos.write("Estonia", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Finland©")) {
			mos.write("Finland", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("France©")) {
			mos.write("France", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Germany©")) {
			mos.write("Germany", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Greece©")) {
			mos.write("Greece", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Hungary©")) {
			mos.write("Hungary", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Iceland©")) {
			mos.write("Iceland", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Indonesia©")) {
			mos.write("Indonesia", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Ireland©")) {
			mos.write("Ireland", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Israel©")) {
			mos.write("Israel", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Italy©")) {
			mos.write("Italy", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Japan©")) {
			mos.write("Japan", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Luxembourg©")) {
			mos.write("Luxembourg", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Mexico©")) {
			mos.write("Mexico", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("New Zealand©")) {
			mos.write("NewZealand", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Norway©")) {
			mos.write("Norway", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Poland©")) {
			mos.write("Poland", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Portugal©")) {
			mos.write("Portugal", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Slovenia©")) {
			mos.write("Slovenia", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("South Africa©")) {
			mos.write("SouthAfrica", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Spain©")) {
			mos.write("Spain", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Sweden©")) {
			mos.write("Sweden", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("Switzerland©")) {
			mos.write("Switzerland", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("the Czech Republic©")) {
			mos.write("theCzechRepublic", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("the Netherlands©")) {
			mos.write("theNetherlands", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("the Republic of Korea©")) {
			mos.write("theRepublicofKorea", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("the Russian Federation©")) {
			mos.write("theRussianFederation", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("the Slovak Republic©")) {
			mos.write("theSlovakRepublic", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("the United Kingdom©")) {
			mos.write("theUnitedKingdom", key, new FloatWritable(sum/count));
		} else if(key.getCountry().equals("the United States©")) {
			mos.write("theUnitedStates", key, new FloatWritable(sum/count));
		} else {
			mos.write("Turkey", key, new FloatWritable(sum/count));
		}
	}
}
