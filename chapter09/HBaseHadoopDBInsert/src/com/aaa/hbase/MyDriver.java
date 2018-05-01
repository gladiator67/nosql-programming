package com.aaa.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

public class MyDriver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration conf = HBaseConfiguration.create();
		// 테이블 이름을 Configuration에 설정한다. --- ①
		conf.set(TableOutputFormat.OUTPUT_TABLE, "INTLCTRY_TABLE");
		Job job = Job.getInstance(conf, "HBase Data Insert");
		
		HBaseAdmin admin = new HBaseAdmin(conf);
   
		if (admin.tableExists("INTLCTRY_TABLE")) {
			System.out.println("이미 table이 존재합니다!");
		} else {
			HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf("INTLCTRY_TABLE"));
			tableDescriptor.addFamily(new HColumnDescriptor("INTLCTRY_DATA"));
			// 테이블을 생성한다.  --- ②
			admin.createTable(tableDescriptor);

			System.out.println("INTLCTRY_TABLE" + "생성 완료!");
        }
 
		job.setInputFormatClass(TextInputFormat.class);
		// HBase TableOutputFormat으로 지정한다. --- ③
		job.setOutputFormatClass(TableOutputFormat.class);
		
		job.setOutputKeyClass(ImmutableBytesWritable.class);
		job.setOutputValueClass(Put.class);
		
		job.setMapperClass(MyMapper.class);
		job.setNumReduceTasks(0); 

 
		FileInputFormat.setInputPaths(job, new Path("/home/user01/data/INTLCTRY/README_TITLE_SORT.txt"));
		
		job.setJarByClass(MyDriver.class);
 
		job.waitForCompletion(true);
	}

}
