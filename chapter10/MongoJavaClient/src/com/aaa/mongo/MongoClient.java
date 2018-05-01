package com.aaa.mongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBAddress;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.Mongo;
import com.mongodb.WriteConcern;

public class MongoClient {
	
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
		DB db = null;
		DBAddress addr = null;
	        
		try {
			// MongoDB와 연결한다.  DB 객체를 반환한다.
			// database가 존재하지 않으면 MongoDB는 database를 생성한다.
		 addr = new DBAddress("master:27017/INTLCTRY");
		 db = Mongo.connect(addr);
		} catch(Exception e) {
			e.getStackTrace();
		}

		// WriteConcern을 지정한다. 생성자의 첫번째 변수는 복제하려는 갯수이고 두번째 변수는 복제 인터벌 시간이다.
		WriteConcern w = new WriteConcern(1,2000);
		db.setWriteConcern(w);
	          
		// collection을 반환한다.
		// collection이 존재하지 않으면 MongoDB는 collection을 생성한다.
		DBCollection collection = db.getCollection("person");
	          
		/**** Insert ****/
		// key와 value를 이용하여 document를 생성하고 collection에 삽입한다.
		BasicDBObject document ;
		for(int i = 0 ; i < array_names.length ; i++){
			document = new BasicDBObject();
			// value -> String
			document.append("name", array_names[i]);
			// value -> int
			document.append("age", array_age[i]);
			// value -> String
			document.append("hobby", array_hobbies[i]);
			// value --> document
			document.append("address", new BasicDBObject("country", array_address[i][0]).append("city", array_address[i][1]));
	        
			collection.insert(document);
	        
		}
	             
	             
		// get count를 반환한다.
		System.out.println("모든 사람들 인원수 : "+collection.getCount());
		//------------------------------------
		// 모든 document를 반환한다.
		DBCursor cursor = collection.find();
		try {
			while(cursor.hasNext()) {
				System.out.println(cursor.next());
	         }
		} finally {
			cursor.close();
		}
		//------------------------------------
	        
		// query를 만들어 document를 검색한다.
		BasicDBObject query = new BasicDBObject("age", new BasicDBObject("$gt", 30));
	        
		cursor = collection.find(query);
		System.out.println("age > 30 인 사람 인원수  --> "+cursor.count());
	             
	          
		/**** Update ****/
		// 앞에서 생성한 age > 30의 질의를 이용하여 반환되는 document를 
		// "age = 20"의 값을 갖는 새로운 document로 대체한다.
		BasicDBObject newDocument = new BasicDBObject();
		newDocument.put("age", 20);
	          
		BasicDBObject updateObj = new BasicDBObject();
		updateObj.put("$set", newDocument);
	          
		// 마지막 매개변수를 true로 설정하여 모든 document에 적용한다.
		collection.update(query, updateObj, false, true); 
	          
		/**** Find and display ****/
		cursor = collection.find(query);
		System.out.println("update 후에 age > 30 인 사람 인원수  --> "+cursor.count());
	             
	             
		// 다시 모든 document를 구한다.
		cursor = collection.find();
		try {
			while(cursor.hasNext()) {
				System.out.println(cursor.next());
			}
		} finally {
			cursor.close();
		}
	        
		/**** Database Remove ****/
		db.dropDatabase();
	          
		/**** Done ****/
		System.out.println("Done");
	}
}
