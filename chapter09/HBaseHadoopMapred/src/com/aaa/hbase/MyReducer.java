package com.aaa.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;

public class MyReducer extends TableReducer<Text, FloatWritable, ImmutableBytesWritable> {

	// Column Family를 선언한다.
	private final byte[] COLUMN_FAMILY = "INTLCTRY_DATA".getBytes();

	@Override
	public void reduce(Text key, Iterable<FloatWritable> values, Context context) throws IOException, InterruptedException {
		
		float sum = 0;
		int count = 0;
		
		Float avg = null;
				 
		for (FloatWritable val : values) {
			sum += val.get();
			count++;
		}
		
		// HBase에 입력하기 위해 Put 객체를 생성한다. rowKey는 country로 지정한다.
		Put put = new Put(Bytes.toBytes(key.toString()));
		avg = new Float(sum/count);
		
		// 평균값을 average란 qualifier로 저장한다.
		put.add(COLUMN_FAMILY, Bytes.toBytes("average"), Bytes.toBytes(avg.toString()));

		// HBase에 Put 객체를 추가한다.
		context.write(null, put);
	}
}
