package com.aaa.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class HiveTest {
	
	private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		// Hive와  jdbc로 연결을 한다. 
				try {
					Class.forName(driverName);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.exit(1);
				}
				Connection con = DriverManager.getConnection("jdbc:hive://localhost:10000/default", "", "");
				Statement stmt = con.createStatement();
						
				// 초기 메타 파일로부터 데이터를 추출하기 위해 첫번째 임시 테이블을 생성한다.
				String tmpTableName_1 = "tmpTable_1";
				stmt.executeQuery("DROP TABLE " + tmpTableName_1);
				String sql = "CREATE TABLE " + tmpTableName_1 
						+ " (filename STRING, title ARRAY<STRING>, unit STRING, freq STRING, seasonalAdj STRING, updateDate STRING) "
						+ "ROW FORMAT DELIMITED "
						+ "FIELDS TERMINATED BY ';' "
						+ "COLLECTION ITEMS TERMINATED BY ':' "
						+ "LINES TERMINATED BY '\n' "
						+ "STORED AS TEXTFILE";
						
				System.out.println("Running: " + sql);
				ResultSet res = stmt.executeQuery(sql);
						
				// 첫번째 임시 테이블의 구조를 나타낸다.
				sql = "DESCRIBE " + tmpTableName_1;
				System.out.println("Running: " + sql);
				res = stmt.executeQuery(sql);
				while (res.next()) {
					System.out.println(res.getString(1) + "\t" + res.getString(2));
				}
						
				// 데이터를 테이블에 삽입한다.
				// 주의 :파일경로는  hive 서버에 로컬에 위치하여야 한다.
				// /home/user01/data/INTLCTRY/README_TITLE_SORT.txt 파일로 부터 첫번째 임시 테이블에 값을 로딩한다..
				String filepath = "/home/user01/data/INTLCTRY/README_TITLE_SORT.txt";
				sql = "LOAD DATA LOCAL INPATH '" + filepath + "' INTO TABLE " + tmpTableName_1;
				System.out.println("Running: " + sql);
				res = stmt.executeQuery(sql);
								
				// 실제 데이터를 로딩할 테이블을 생성한다.
				String realTableName = "dataTable";
				stmt.executeQuery("DROP TABLE " + realTableName);
				sql = " CREATE TABLE " + realTableName + " (country STRING, title STRING, ageRange STRING , sex STRING, unit STRING, "
						+ "freq STRING, seasonalAdj STRING, updateDate STRING, date STRING, value FLOAT) " 
						+ "ROW FORMAT DELIMITED "
						+ "LINES TERMINATED BY '\n' "
						+ "STORED AS TEXTFILE";
				System.out.println("Running : " + sql);
				res = stmt.executeQuery(sql);
						
				// 실제 데이터를 로딩할 테이블의 구조를 나타낸다.
				sql = "DESCRIBE " + realTableName;
				System.out.println("Running: " + sql);
				res = stmt.executeQuery(sql);
				while (res.next()) {
					System.out.println(res.getString(1) + "\t" + res.getString(2));
				}
			
				// 메타 파일로부터 불러온 파일명을 이용하여 값을 저장할 두번째 임시 파일을 생성한다.
				String tmpTableName_2 = "tmpTable_2";
				stmt.executeQuery("DROP TABLE " + tmpTableName_2);
				sql = "CREATE TABLE " + tmpTableName_2 + " (date STRING, value FLOAT) "
					+ "ROW FORMAT DELIMITED "
					+ "FIELDS TERMINATED BY ',' "
					+ "LINES TERMINATED BY '\n' "
					+ "STORED AS TEXTFILE";
			
				System.out.println("Running : "+ sql);
				res = stmt.executeQuery(sql);
			
				// 실제 데이터를 로딩할 테이블의 구조를 나타낸다.
				sql = "DESCRIBE " + tmpTableName_2;
				System.out.println("Running: " + sql);
				res = stmt.executeQuery(sql);
				while (res.next()) {
					System.out.println(res.getString(1) + "\t" + res.getString(2));
				}
				
				//     테이블을 표시한다.
				sql = "SHOW TABLES";
				System.out.println("Running: " + sql);
				res = stmt.executeQuery(sql);
				if (res.next()) {
					// 첫번째 임시 테이블에 대한 정보를 출력한다.
					System.out.println(res.getString(1));
				}
				
				// 첫번째 임시 테이블로부터 조건에 일치하는 파일경로와 파일명을 불러온다.
				sql = "SELECT trim(filename), "
						+ "trim(substr(title[2],instr(title[2],'for')+3)),"
						+ "trim(title[0]), "
						+ "trim(title[1]), "
						+ "trim(substr(title[2],1,instr(title[2],'for')-1)), "
						+ "trim(unit), "
						+ "trim(freq), "
						+ "trim(seasonalAdj), "
						+ "trim(updateDate) FROM " + tmpTableName_1 
						+ " WHERE title[0] = 'Active Population' AND trim(substr(title[2],instr(title[2],'for')+3)) = 'Brazil©'";
					
				res = stmt.executeQuery(sql);
				
				while(res.next()) {
					String path = "/home/user01/data/INTLCTRY/data/" + res.getString(1).replace('\\', '/');

					// 추출한 파일경로로부터 두번째 임시 테이블에 값을 로딩한다.
					sql = "LOAD DATA LOCAL INPATH '" + path + "' OVERWRITE INTO TABLE " + tmpTableName_2;
					System.out.println("Running : " + sql);
					stmt.executeQuery(sql);
					
					// Join문을 사용하여 첫번째 임시 테이블과 두번째 임시 테이블에서 테이터를 추출하여 실제 테이블에 로딩한다.
					sql = "INSERT INTO TABLE " + realTableName 
							+ " SELECT trim(substr(tt_1.title[2],instr(tt_1.title[2],'for')+3)), "
							+ "trim(tt_1.title[0]), "
							+ "trim(tt_1.title[1]), "
							+ "trim(substr(tt_1.title[2],1,instr(tt_1.title[2],'for')-1)), "
							+ "trim(tt_1.unit), "
							+ "trim(tt_1.freq), "
							+ "trim(tt_1.seasonalAdj), "
							+ "trim(tt_1.updateDate), " 
							+ "trim(tt_2.date), "
							+ "tt_2.value "
							+ "FROM " + tmpTableName_1 + " tt_1 JOIN " + tmpTableName_2 + " tt_2"
							+ " WHERE tt_1.title[0] = 'Active Population' AND trim(substr(tt_1.title[2],instr(tt_1.title[2],'for')+3)) = 'Brazil©' "
							+ " AND trim(tt_1.title[0]) = '" + res.getString(3) + "'"
							+ " AND trim(tt_1.title[1]) = '" + res.getString(4) + "'"
							+ " AND trim(substr(tt_1.title[2],1,instr(tt_1.title[2],'for')-1)) = '" + res.getString(5) + "'"
							+ " AND trim(tt_1.unit) = '" + res.getString(6) + "'"
							+ " AND trim(tt_1.freq) = '" + res.getString(7) + "'"
							+ " AND trim(tt_1.seasonalAdj) = '" + res.getString(8) + "'"
							+ " AND tt_2.date != 'DATE'";

					System.out.println("Running : " + sql);
					stmt.executeQuery(sql);
				}
				
				// select * query
				sql = "SELECT country, title, ageRange, sex, unit, freq, seasonalAdj, avg(value) FROM " + realTableName 
						+ " GROUP BY country, title, ageRange, sex, unit, freq, seasonalAdj";
									
				
				System.out.println("Running : " + sql);
				res = stmt.executeQuery(sql);
				
				while (res.next()) {
					System.out.println(res.getString(1) + "," + res.getString(2) + "," +  res.getString(3) + "," + res.getString(4) + "," + 
							res.getString(5) + "," +  res.getString(6) + "," + res.getString(7) +	"," + res.getFloat(8));
				}	

				stmt.executeQuery("DROP TABLE " + tmpTableName_1);
				stmt.executeQuery("DROP TABLE " + tmpTableName_2);
						
				res.close();
				stmt.close();
				con.close();
	}

}
