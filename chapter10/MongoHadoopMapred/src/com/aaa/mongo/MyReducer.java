package com.aaa.mongo;

import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.hadoop.io.BSONWritable;
import com.mongodb.hadoop.io.MongoUpdateWritable;

public class MyReducer extends Reducer<BSONWritable, DoubleWritable, NullWritable, MongoUpdateWritable> {
	
	@Override
	public void reduce(BSONWritable key, Iterable<DoubleWritable> values, Context context) throws IOException, InterruptedException {
		
		BasicDBObject update = null;
		BasicDBObject query = null;
		int i = 0;
		double sum = 0.0;
		double ave = 0.0;
				
		if(key.getDoc().keySet().contains("country")) {
			// query 객체를 생성한다.
			query = new BasicDBObject("country", key.getDoc().get("country")).append("title", key.getDoc().get("title"))
					.append("ageRange", key.getDoc().get("ageRange")).append("sex", key.getDoc().get("sex")).append("unit", key.getDoc().get("unit"))
					.append("freq", key.getDoc().get("freq")).append("seasonalAdj", key.getDoc().get("seasonalAdj"))
					.append("updateDate", key.getDoc().get("updateDate"));
			
			for(DoubleWritable value : values) {
				i++;
				sum += value.get();
			}
			
			ave = sum/i;
			
			// update 객체를 생성한다.
			BasicDBObjectBuilder tmpObjectBuilder = BasicDBObjectBuilder.start();
			tmpObjectBuilder.append("average", new Double(ave));
			update =  new BasicDBObject("$push", new BasicDBObject("countryData", tmpObjectBuilder.get()));
			
			// MongoDB에 update를 실행한다.
			context.write(null, new MongoUpdateWritable(query, update, true, false));		
		}	
	}
}
