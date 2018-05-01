package com.aaa.mongo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.hadoop.io.BSONWritable;

public class MyMapper extends Mapper<LongWritable, Text, BSONWritable, BSONWritable> {
	
	private BufferedReader br = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Date date = null;
	
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException{
		br.close();
	}

	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		
		BasicDBObjectBuilder outputKeyBuilder = BasicDBObjectBuilder.start();
		BasicDBObjectBuilder outputValueBuilder = BasicDBObjectBuilder.start();
		
		if(value.toString().contains("\\") || value.toString().contains("csv")) {

			String[] columns = extractColumns(value.toString());
						
			if(columns != null) {
				// 데이터를 간략화하기 위해 조건을 부여한다.
				if( columns[4].equals("Brazil©") && columns[1].equals("Active Population")) {
					
					try {
						date = sdf.parse(columns[8]);
					} catch (ParseException e) {
						e.printStackTrace();
		            }
					// Key 값이 될 BasicDBObject를 생성한다.
					outputKeyBuilder.append("country", columns[4]).append("title", columns[1]).append("ageRange", columns[2]).append("sex", columns[3])
							.append("unit", columns[5]).append("freq", columns[6].charAt(0)).append("seasonalAdj", columns[7])
							.append("updateDate", date);
															
					Path path = new Path("/home/user01/data/INTLCTRY/data/" + columns[0].trim());
					FileSystem fs = FileSystem.get(URI.create("/home/user01/data/INTLCTRY/data" + columns[0].trim()), new Configuration());
					
					if(fs.isFile(path)) {
						br =new BufferedReader(new InputStreamReader(fs.open(path)));
									
						while(br.ready()) {            	
							String line = br.readLine();
							if(line.equals("DATE,VALUE")) 
								continue;
							
							String[] data = line.split(",");
							try {
								date = sdf.parse(data[0]);
							} catch (ParseException e) {
								e.printStackTrace();
				            }
							// Value가 될 BasicDBObject를 생성한다.
							outputValueBuilder.append("date", date).append("value", data[1]);
							// <KEY BasicDBObject, VALUE BasicDBObject>를 출력한다.					
							context.write(new BSONWritable(outputKeyBuilder.get()), new BSONWritable(outputValueBuilder.get()));
				        }
					} else {
						System.out.println("파일이 존재하지 않습니다");
					}
				}
			}
		}
	}
	
	private String[] extractColumns(String line) {
		
		String[] columns = new String[9]; 
		String[] tmpCol =	line.split(";");
		
		if( tmpCol != null && tmpCol.length > 0) {
			String[] subColumns = tmpCol[1].split(":");
			
			if( subColumns != null && subColumns.length > 0) {
				if(subColumns[0].trim().equals("Active Population") || subColumns[0].trim().equals("Activity Rate") ||
						subColumns[0].trim().equals("Employed Population") || subColumns[0].trim().equals("Employment Rate") || 
						subColumns[0].trim().equals("Inactive Population") || subColumns[0].trim().equals("Inactivity Rate") ||
						subColumns[0].trim().equals("Unemployed Population") || subColumns[0].trim().equals("Unemployment Rate") || 
						subColumns[0].trim().equals("Working Age Population")) {
					columns[0] = tmpCol[0].trim().replace('\\','/');
					columns[1] = subColumns[0].trim();
					columns[2] = subColumns[1].trim();
					columns[3] = subColumns[2].substring(0, subColumns[2].indexOf("for")).trim();
					columns[4] =	subColumns[2].substring(subColumns[2].indexOf("for")+3).trim();

					columns[5] = tmpCol[2].trim();
					columns[6] = tmpCol[3].trim();
					columns[7] = tmpCol[4].trim();
					columns[8] = tmpCol[5].trim();
						
					return columns;
				}
			}
		}
		return null;
	}
}
