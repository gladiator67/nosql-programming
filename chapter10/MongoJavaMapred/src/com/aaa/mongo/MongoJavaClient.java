package com.aaa.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

public class MongoJavaClient {
	
	static String array_names[] = {"이영애","장동건","고소영","소녀시대","정지훈","김연아 ","아이유","김수현","전지연","한가인"};
    
	static String array_address[][] ={
		{"한국", "서울"},
		{"한국", "서울"},
		{"한국", "부산"},
		{"한국", "서울"},
		{"한국", "서울"},
		{"한국", "대전"},
		{"한국", "대전"},
		{"한국", "광주"},
		{"한국", "부산"},
		{"한국", "광주"}
	};
		
	static String array_hobbies[] = {"승마","수영","등산","자전거","헬스","스케이팅 ","노래","검도","연극" ,"고전무용"};
	    
	static int array_age[] = {40, 29, 51, 37, 42, 31, 36, 58, 26, 46};

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MongoClient client = null;
		
		try {
			// MongoDB와 연결한다.
			client = new MongoClient("localhost", 27017);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		DB db = client.getDB("INTLCTRY");
		
		WriteConcern w = new WriteConcern(1,2000);
		db.setWriteConcern(w);
		
		DBCollection collection = db.getCollection("person");

		BasicDBObject document ;
		
		// MongoDB에 데이터를 insert한다.	
		for(int i = 0 ; i < array_names.length ; i++){
			document = new BasicDBObject();
			// value -> String
			document.append("name", array_names[i]);
			// value -> int
			document.append("age", array_age[i]);
			// value -> String
			document.append("hobby", array_hobbies[i]);
			// value --> document
			document.append("address", new BasicDBObject("country",
	                  array_address[i][0]).append("city", array_address[i][1]));
	        
			collection.insert(document);
		}
		
		// map과 reduce 명령을 java script로 작업하고 String 문자열로 저장한다.
		String map = "function() { " +
                "var key; " +
                "if( this.address.city == '서울') " +
                "key = 'Seoul'; " +
                "else if(this.address.city == '부산') " +
                "key = 'Pusan'; " +
                "else if(this.address.city == '광주') " +
                "key = 'Kwangju'; " +     
                "else if(this.address.city == '대전') " +
                "key = 'Daejeon'; " +
                "var value = { " +
                "count : 1, " +
                "name : this.name , " +
                "age : this.age ," +
                "hobby : this.hobby }; " +
                "emit(key,value); }";

		String reduce = "function(key, values) { " +
				          "reduceValues = {count:0, names:Array(values.length), hobbies:Array(values.length), aveAge:0}; " +
				          "var totalAge = 0; " +
				          "for( var index=0; index < values.length ; index++) { " +
				          "reduceValues.count += values[index].count; " +
				          "reduceValues.names[index] = values[index].name; " +
				          "reduceValues.hobbies[index] = values[index].hobby; " +
				          "totalAge += values[index].age; }; " +
				          "reduceValues.aveAge = totalAge/reduceValues.count; " +
				          "return reduceValues; }";

		// Map/Reduce 명령어를 실행한다.
		BasicDBObject query = new BasicDBObject("age", new BasicDBObject("$gt", 20));
		MapReduceCommand cmd = new MapReduceCommand(collection, map, reduce, "personHR", MapReduceCommand.OutputType.REPLACE, query);
		// Map/Reduce 작업을 실행한 결과물을 출력한다.
		MapReduceOutput out = collection.mapReduce(cmd);

		for (DBObject o : out.results()) {
			System.out.println(o.toString());
		}
	}

}
