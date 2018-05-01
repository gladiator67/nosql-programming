package com.aaa.hbase;

import java.io.IOException;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.FloatWritable;
import org.apache.hadoop.io.Text;

public class MyMapper extends TableMapper<Text, FloatWritable> {
	
	private Text key = new Text();
	private FloatWritable output = new FloatWritable();

	@Override
	public void map(ImmutableBytesWritable row, Result value, Context context) throws InterruptedException, IOException {
		
		key.set(new String(value.getRow()));
		
		for (Cell c : value.rawCells()) {
			
			String qualifier = new String(CellUtil.cloneQualifier(c));
            
			if (qualifier.equals("data")) {
				Float f = Float.parseFloat(new String(CellUtil.cloneValue(c)));
				output.set(f);
				context.write(key, output);
			}
		}
	}
}
