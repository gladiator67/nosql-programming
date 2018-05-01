package com.aaa.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;

public class MyDriver {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Configuration config = HBaseConfiguration.create();
		Job job = Job.getInstance(config,"HBase MapReduce Test");
		job.setJarByClass(MyDriver.class);    // Driver 클래스를 지정한다.
		
		Scan scan = new Scan();
		scan.setRaw(true); // 디폴트는 false로 지정되어 있다.  false값에서 최신 버전만 반환된다.
		scan.setCaching(1000);      // 디폴트로 1 로 설정되어 있다.이 값을 충분하게 변경한다.
		scan.setCacheBlocks(false);  // MapReduce작업을 위해 false로 지정한다.
		scan.setMaxVersions();// Scan의 Version값을 최대값으로 설정하거나 범위를 지정한다.
                                                                     											// 그렇지 않으면 최신 버전에 대한 Scan 값만 반영된다.
		

		// Filter나 기타 속성을 지정한다.
		Filter filterTitle = new RowFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("Brazil©,Active Population"));
		scan.setFilter(filterTitle);

		TableMapReduceUtil.initTableMapperJob(
				"INTLCTRY_TABLE",      // input table 이름
				scan,         	        // 앞에서 설정한 Scan 객체를 넘긴다. 이 Scan 값은 Mapper 클래스의 map 함수의 Result 값에 담긴다.
				MyMapper.class,        // mapper class
				Text.class,            // mapper output key
				FloatWritable.class,   // mapper output value
				job );
        
		TableMapReduceUtil.initTableReducerJob(
				"INTLCTRY_TABLE",      // output table 이름
				MyReducer.class,             // reducer class
				job);

		job.setNumReduceTasks(1);

		job.waitForCompletion(true);
	}

}
