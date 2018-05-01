package com.aaa.mongo;

import java.io.IOException;
import java.util.Date;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Mapper;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.hadoop.io.BSONWritable;

public class MyMapper extends Mapper<Object, BasicDBObject, BSONWritable, DoubleWritable> {
	
	// inputFormatClass를 MongoInputFormat.class로 설정하면 아래와 같이
  // map함수의 <Key,Value>는 <Object, BasicDBObject>의 형식을 갖는다.
  // Value객체는 Document값을 담고 있다.
	@Override
	public void map(Object key, BasicDBObject value, Context context) throws IOException, InterruptedException{
		
		double outputValue = 0.0;
		BasicDBObjectBuilder outputKeyBuilder = BasicDBObjectBuilder.start();
		
		// 각 Key에 대한 Value 값을 읽어온다.
		String bCountry = (String)value.get("country");
		String bTitle = (String)value.get("title");
		String bAgeRange = (String)value.get("ageRange");
		String bSex = (String)value.get("sex");
		String bUnit = (String)value.get("unit");
		char bFreq = ((String)value.get("freq")).charAt(0);
		String bSeasonalAdj = (String)value.get("seasonalAdj");
		Date bUpdateDate = (Date)value.get("updateDate");
		
		// BasicDBList부분을 제외한 나머지 document value로 Key값을 만든다.
		outputKeyBuilder.append("country", bCountry).append("title", bTitle).append("ageRange", bAgeRange).append("sex", bSex)
					.append("unit", bUnit).append("freq", bFreq).append("seasonalAdj", bSeasonalAdj).append("updateDate", bUpdateDate);
		
		// BasicDBList Value 부분을 읽어와	 Value 값을 만든다.
		BasicDBList valueDataList = (BasicDBList)value.get("countryData");
		
		for(int index=0 ; index<valueDataList.size(); index++) {
			BasicDBObject tmpObj = (BasicDBObject)valueDataList.get(index);
			outputValue = tmpObj.getDouble("value");
	    // <Key,Value>를 출력한다.           
			context.write(new BSONWritable(outputKeyBuilder.get()), new DoubleWritable(outputValue));
		}
	}
}
