package com.aaa.hbase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class MyMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable , Put> {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private BufferedReader br = null;
	private Date date = null;
	
	private HTable table = null;
	// Column Family를 선언한다.
	private final byte[] COLUMN_FAMILY = "INTLCTRY_DATA".getBytes();
  
  // Mapper가 시작할 때 table 객체를 생성하고 속성을 설정한다.
	@Override
	public void setup(Context context) throws IOException, InterruptedException {
		table = new HTable(context.getConfiguration(),"INTLCTRY_TABLE");
		table.setAutoFlush(false, true);
	}
	
	// Mapper가 종료할 때 스트림을 닫고 table을 flush한 다음 종료한다.
	@Override
	public void cleanup(Context context) throws IOException, InterruptedException{
		br.close();
		table.flushCommits();
		table.close();
	}
	
	@Override
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{
		if(value.toString().contains("\\") || value.toString().contains("csv")) {

			String[] columns = extractCol(value.toString());
			if(columns != null) {
				
				// 데이터를 간략화하기 위해 조건을 부여한다.
				if(columns[4].equals("Brazil©") || columns[4].equals("Indonesia©") || columns[4].equals("Luxembourg©")) {
				
					try {
						date = sdf.parse(columns[8]);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					// row Key를 생성한다.	--- ①
					String rowKey = new String(columns[4]+","+columns[1]+","+columns[2]+","+columns[3]+","+columns[5]+
							","+columns[6].charAt(0)+","+columns[7]+","+date.toString());
					
					// 생성된 Put 객체를 담을 List 객체를 생성한다. ---②
					List<Put> puts = new ArrayList<Put>();
					
					// ①의 rowKey에 대한 Put 객체를 생성한다. --- ③
					Put put = new Put(Bytes.toBytes(rowKey));
					
					Path path = new Path("/home/user01/data/INTLCTRY/data/" + columns[0].trim());
					FileSystem fs = FileSystem.get(URI.create("/home/user01/data/INTLCTRY/data" + columns[0].trim()), context.getConfiguration());
					
					if(fs.isFile(path)) {
						br =new BufferedReader(new InputStreamReader(fs.open(path)));
								
						while(br.ready()) {            	
							String line = br.readLine();
							if(line.equals("DATE,VALUE")) 
								continue;
							String[] valueColumns = line.toString().trim().split(",");
							
							if(valueColumns != null && valueColumns.length == 2 && !valueColumns[1].equals(".")) {
								
								String time = valueColumns[0].replaceAll("-", "");
								long ts = Long.parseLong(time);
								// 출발 지연을 Column Family에 time stamp와 같이 추가한다. qualifier는 data로 지정한다.  --- ④
								put.add(COLUMN_FAMILY, Bytes.toBytes("data"), ts, Bytes.toBytes(valueColumns[1]));
								puts.add(put);
							}
						}
						// List<Put> 객체를 HBase 테이블에 Put한다. --- ⑤
						table.put(puts);
					} else {
						System.out.println("파일이 존재하지 않습니다.");
					}
				}
			}
		}
	}
	
private String[] extractCol(String line) {
		
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
