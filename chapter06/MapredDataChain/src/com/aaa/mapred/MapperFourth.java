package com.aaa.mapred;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

public class MapperFourth extends Mapper<CountryData, FloatWritable, CountryData, FloatWritable> {

	private MultipleOutputs<CountryData, FloatWritable> mos;
	
	@Override
	public void setup(Context context) throws IOException, InterruptedException{
		mos = new MultipleOutputs<CountryData, FloatWritable>(context);
    }
   
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException{
		mos.close();
    }
	
	// MultiOutputs를 이용하여 국가별로 다중 출력한다.
	@Override
	public void map(CountryData key, FloatWritable value, Context context) throws IOException, InterruptedException{
		if(key.getCountry().equals("Australia©")) {
			mos.write("Australia", key, value);
		} else if(key.getCountry().equals("Austria©")) {
			mos.write("Austria", key, value);
		} else if(key.getCountry().equals("Belgium©")) {
			mos.write("Belgium", key, value);
		} else if(key.getCountry().equals("Brazil©")) {
			mos.write("Brazil", key, value);
		} else if(key.getCountry().equals("Canada©")) {
			mos.write("Canada", key, value);
		} else if(key.getCountry().equals("Chile©")) {
			mos.write("Chile", key, value);
		} else if(key.getCountry().equals("Denmark©")) {
			mos.write("Denmark", key, value);
		} else if(key.getCountry().equals("Estonia©")) {
			mos.write("Estonia", key, value);
		} else if(key.getCountry().equals("Finland©")) {
			mos.write("Finland", key, value);
		} else if(key.getCountry().equals("France©")) {
			mos.write("France", key, value);
		} else if(key.getCountry().equals("Germany©")) {
			mos.write("Germany", key, value);
		} else if(key.getCountry().equals("Greece©")) {
			mos.write("Greece", key, value);
		} else if(key.getCountry().equals("Hungary©")) {
			mos.write("Hungary", key, value);
		} else if(key.getCountry().equals("Iceland©")) {
			mos.write("Iceland", key, value);
		} else if(key.getCountry().equals("Indonesia©")) {
			mos.write("Indonesia", key, value);
		} else if(key.getCountry().equals("Ireland©")) {
			mos.write("Ireland", key, value);
		} else if(key.getCountry().equals("Israel©")) {
			mos.write("Israel", key, value);
		} else if(key.getCountry().equals("Italy©")) {
			mos.write("Italy", key, value);
		} else if(key.getCountry().equals("Japan©")) {
			mos.write("Japan", key, value);
		} else if(key.getCountry().equals("Luxembourg©")) {
			mos.write("Luxembourg", key, value);
		} else if(key.getCountry().equals("Mexico©")) {
			mos.write("Mexico", key, value);
		} else if(key.getCountry().equals("New Zealand©")) {
			mos.write("NewZealand", key, value);
		} else if(key.getCountry().equals("Norway©")) {
			mos.write("Norway", key, value);
		} else if(key.getCountry().equals("Poland©")) {
			mos.write("Poland", key, value);
		} else if(key.getCountry().equals("Portugal©")) {
			mos.write("Portugal", key, value);
		} else if(key.getCountry().equals("Slovenia©")) {
			mos.write("Slovenia", key, value);
		} else if(key.getCountry().equals("South Africa©")) {
			mos.write("SouthAfrica", key, value);
		} else if(key.getCountry().equals("Spain©")) {
			mos.write("Spain", key, value);
		} else if(key.getCountry().equals("Sweden©")) {
			mos.write("Sweden", key, value);
		} else if(key.getCountry().equals("Switzerland©")) {
			mos.write("Switzerland", key, value);
		} else if(key.getCountry().equals("the Czech Republic©")) {
			mos.write("theCzechRepublic", key, value);
		} else if(key.getCountry().equals("the Netherlands©")) {
			mos.write("theNetherlands", key, value);
		} else if(key.getCountry().equals("the Republic of Korea©")) {
			mos.write("theRepublicofKorea", key, value);
		} else if(key.getCountry().equals("the Russian Federation©")) {
			mos.write("theRussianFederation", key, value);
		} else if(key.getCountry().equals("the Slovak Republic©")) {
			mos.write("theSlovakRepublic", key, value);
		} else if(key.getCountry().equals("the United Kingdom©")) {
			mos.write("theUnitedKingdom", key, value);
		} else if(key.getCountry().equals("the United States©")) {
			mos.write("theUnitedStates", key, value);
		} else {
			mos.write("Turkey", key, value);
		}
	}
}
