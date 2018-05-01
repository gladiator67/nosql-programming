package com.aaa.mapred;

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

public class MyMapper extends Mapper<LongWritable, Text, Text, Text> {
	
	private Text outputKey = new Text();
  private BufferedReader br = null;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private Date date = null;
	
	// map 작업이 종료되면 BufferedReader stream을 닫는다.
	@Override
  public void cleanup(Context context) throws IOException, InterruptedException{
		br.close();
  }
		
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
			
		if(value.toString().contains("\\") || value.toString().contains("csv")) {

			// Key 값으로 사용될 문자열 배열을 출력한다.
			String[] columns = extractCol(value.toString());
			if(columns != null) {
				
				try {
					date = sdf.parse(columns[8]);
				} catch (ParseException e) {
					e.printStackTrace();
	            }
				
				outputKey.set(columns[4]+","+columns[1]+","+columns[2]+","+columns[3]+","+columns[5]+","+columns[6]+","+columns[7]+","+date);						
						
				// Value 값으로 사용할 문자열을 읽어들여 파일을 출력한다.		
				Path path = new Path("/home/user01/data/INTLCTRY/data/" + columns[0].trim());
				FileSystem fs = FileSystem.get(URI.create("/home/user01/data/INTLCTRY/data" + columns[0].trim()), new Configuration());
				
				if(fs.isFile(path)) {
					br =new BufferedReader(new InputStreamReader(fs.open(path)));
								
					while(br.ready())
			        {            	
						String line = br.readLine();
						if(line.equals("DATE,VALUE")) 
							continue;
        
						context.write(outputKey, new Text(line));
			        }
				} else {
					System.out.println("파일이 존재하지 않습니다.");
				}
			}
		}
	}
	
	// meta 파일로부터 각 문자열을 추출한다.
	private String[] extractCol(String line) {
		
		String[] columns = new String[9]; 
		String[] tmpCol =	line.split(";");
		
		if( tmpCol != null && tmpCol.length > 0) {
			String[] subColumns = tmpCol[1].split(":");
			
			if( subColumns != null && subColumns.length > 0) {
				if(subColumns[0].trim().equals("Active Population") || subColumns[0].trim().equals("Activity Rate") ||
						subColumns[0].trim().equals("Employed Population") || subColumns[0].trim().equals("Employment Rate") || 
						subColumns[0].trim().equals("Employment to Population Rate") || subColumns[0].trim().equals("Inactive Population") ||
						subColumns[0].trim().equals("Inactivity Rate") || subColumns[0].trim().equals("Unemployed Population") ||
						subColumns[0].trim().equals("Unemployment Rate") || subColumns[0].trim().equals("Unemployment to Population Rate") ||
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
