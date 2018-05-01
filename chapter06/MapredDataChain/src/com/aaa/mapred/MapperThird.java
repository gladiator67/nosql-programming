package com.aaa.mapred;

import java.io.IOException;

import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class MapperThird extends Mapper<CountryData, FloatWritable, CountryData, FloatWritable> {
	
	// 기준을 설정하여 기준에 맞는 복합키만 추출한다.
	@Override
	public void map(CountryData key, FloatWritable value, Context context) throws IOException, InterruptedException{
		if(key.getTitle().equals("Activity Rate")) {
			if(satisfyCriteria(1, key.getSex(), value.get()))
					context.write(key, value);			
		} else if(key.getTitle().equals("Employment Rate")) {
			if(satisfyCriteria(2, key.getSex(), value.get()))
				context.write(key, value);
		} else if(key.getTitle().equals("Inactivity Rate")) {
			if(satisfyCriteria(3, key.getSex(), value.get()))
				context.write(key, value);
		} else if(key.getTitle().equals("Unemployment Rate")) {
			if(satisfyCriteria(4, key.getSex(), value.get()))
				context.write(key, value);
		}
	}
	
	// 기준은 저자가 임의로 설정하였다.
	private boolean satisfyCriteria(int type, String sex, double d) {
		
		switch (type) {
		case 1: 
			if(sex.equalsIgnoreCase("All Persons") || sex.equalsIgnoreCase("Total")) {
				if(d>50.0)
					return true;
			} else if(sex.equalsIgnoreCase("Males")) {
				if(d>70.0)
					return true;
			} else if(sex.equalsIgnoreCase("Females")) {
				if(d>30.0)
					return true;
			}
			return false;
		case 2:
			if(sex.equalsIgnoreCase("All Persons") || sex.equalsIgnoreCase("Total")) {
				if(d>50.0)
					return true;
			} else if(sex.equalsIgnoreCase("Males")) {
				if(d>70.0)
					return true;
			} else if(sex.equalsIgnoreCase("Females")) {
				if(d>30.0)
					return true;
			}
			return false;
		case 3:
			if(sex.equalsIgnoreCase("All Persons") || sex.equalsIgnoreCase("Total")) {
				if(d>50.0)
					return true;
			} else if(sex.equalsIgnoreCase("Males")) {
				if(d>30.0)
					return true;
			} else if(sex.equalsIgnoreCase("Females")) {
				if(d>70.0)
					return true;
			}
			return false;
		case 4:
			if(sex.equalsIgnoreCase("All Persons") || sex.equalsIgnoreCase("Total")) {
				if(d>50.0)
					return true;
			} else if(sex.equalsIgnoreCase("Males")) {
				if(d>30.0)
					return true;
			} else if(sex.equalsIgnoreCase("Females")) {
				if(d>70.0)
					return true;
			}
			return false;
		}
		return false;
	}
}
