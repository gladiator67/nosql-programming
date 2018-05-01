package com.aaa.mongo;

import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.hadoop.io.BSONWritable;

public class MyReducer extends Reducer<BSONWritable, BSONWritable, NullWritable, BSONWritable> {

	@Override
	public void reduce(BSONWritable key, Iterable<BSONWritable> values, Context context) throws IOException, InterruptedException{
		
		// <Key,Value>에서 Value로 출력할 BasicDBObject를 생성한다.
		BasicDBObjectBuilder outputValueBuilder = BasicDBObjectBuilder.start();
		// reduce 함수의 Iterable<BSONWritable> values 값을 저장할 BasicDBList를 생성한다.
		BasicDBList outputValueDataList = new BasicDBList();
		
		String bCountry = (String)key.getDoc().get("country");
		String bTitle = (String)key.getDoc().get("title");
		String bAgeRange = (String)key.getDoc().get("ageRange");
		String bSex = (String)key.getDoc().get("sex");
		String bUnit = (String)key.getDoc().get("unit");
		char bFreq = ((String)key.getDoc().get("freq")).charAt(0);
		String bSeasonalAdj = (String)key.getDoc().get("seasonalAdj");
		Date bUpdateDate = (Date)key.getDoc().get("updateDate");
		
		outputValueBuilder.append("country", bCountry).append("title", bTitle).append("ageRange", bAgeRange).append("sex", bSex)
				.append("unit", bUnit).append("freq", bFreq).append("seasonalAdj", bSeasonalAdj).append("updateDate", bUpdateDate);
		
		for(BSONWritable bsonVal : values) {
			if(bsonVal.getDoc().keySet().contains("date")) {
				Date bDataDate = (Date)bsonVal.getDoc().get("date");
				String bData = (String)bsonVal.getDoc().get("value");
				
				BasicDBObjectBuilder tmpBuilder = BasicDBObjectBuilder.start();
				if(!bData.equals(".")) {
					tmpBuilder.append("date", bDataDate).append("value", Float.parseFloat(bData));
				
					outputValueDataList.add(tmpBuilder.get());
				}
			}
		}
		
		outputValueBuilder.append("countryData", outputValueDataList);
		// BasicDBObject를 BSONWritable로 출력한다. 이 객체는 Document 타입으로 MongoDB에 저장된다.
		context.write(null, new BSONWritable(outputValueBuilder.get()));
	}
}
