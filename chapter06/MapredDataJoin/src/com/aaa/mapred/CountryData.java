package com.aaa.mapred;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableUtils;

public class CountryData implements WritableComparable<CountryData> {

	private String country;
	private String title;
	private String age;
	private String sex;
	private String units;
	private char frequency;
	private String seasonalAdjust;
	private String updateDate;

	@Override
	public void readFields(DataInput in) throws IOException {
		// TODO Auto-generated method stub
		country = WritableUtils.readString(in);
		title = WritableUtils.readString(in);
		age = WritableUtils.readString(in);
		sex = WritableUtils.readString(in);
		units = WritableUtils.readString(in);
		frequency = in.readChar();
		seasonalAdjust = WritableUtils.readString(in);
		updateDate = WritableUtils.readString(in);
	}

	@Override
	public void write(DataOutput out) throws IOException {
		// TODO Auto-generated method stub
		WritableUtils.writeString(out, country);
		WritableUtils.writeString(out, title);
		WritableUtils.writeString(out, age);
		WritableUtils.writeString(out, sex);
		WritableUtils.writeString(out, units);
		out.writeChar(frequency);
		WritableUtils.writeString(out, seasonalAdjust);
		WritableUtils.writeString(out, updateDate);
	}

	@Override
	public int compareTo(CountryData key) {
		// TODO Auto-generated method stub
		int result = country.compareToIgnoreCase(key.country);
	       
		if(0 == result) {
			result = title.compareTo(key.title);
				
			if(0 == result) {
				result = age.compareTo(key.age);
				
				if(0 == result) {
					result = sex.compareTo(key.sex);
					
					if(0 == result) {
						result = units.compareTo(key.units);
					}
				}
			}
		}
		return result;
	}
	
	@Override
	public String toString() {
		return country+","+title+","+age+","+sex+","+units+","+frequency+","+seasonalAdjust+","+updateDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getUnits() {
		return units;
	}

	public void setUnits(String units) {
		this.units = units;
	}

	public char getFrequency() {
		return frequency;
	}

	public void setFrequency(char frequency) {
		this.frequency = frequency;
	}

	public String getSeasonalAdjust() {
		return seasonalAdjust;
	}

	public void setSeasonalAdjust(String seasonalAdjust) {
		this.seasonalAdjust = seasonalAdjust;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}
}
