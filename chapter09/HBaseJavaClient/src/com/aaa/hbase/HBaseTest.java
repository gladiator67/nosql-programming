package com.aaa.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseTest {
	
	private static Configuration conf = null;

    // 생성자로 HBaseConfiguration을 생성한다.
	static {
		conf = HBaseConfiguration.create();
    }
	
	// HBase Table을 생성한다.
		public static void creatTable(String tableName, String family) throws Exception {
			HBaseAdmin admin = new HBaseAdmin(conf);
			if (admin.tableExists(tableName)) {
				System.out.println("이미 table이 존재합니다!");
			} else {
				HTableDescriptor tableDescriptor = new HTableDescriptor(TableName.valueOf(tableName));
				tableDescriptor.addFamily(new HColumnDescriptor(family));
				admin.createTable(tableDescriptor);
				System.out.println(tableName + "테이블 생성 완료!");
	        }
	    }
		
		// HBase Table을 삭제한다.
		public static void deleteTable(String tableName) throws Exception {
			HBaseAdmin admin = new HBaseAdmin(conf);
			admin.disableTable(tableName);
			admin.deleteTable(tableName);
			System.out.println(tableName + "테이블 삭제 완료!");
	    }
		
		// 생성된  HBase Table에 값을 추가한다.
		public static void addRecord(String tableName, String rowKey,
				String family, String qualifier, long ts, String value) throws IOException {
	        	
			HTable table = new HTable(conf, tableName);
			Put put = new Put(Bytes.toBytes(rowKey));
			put.add(Bytes.toBytes(family), Bytes.toBytes(qualifier), ts, Bytes.toBytes(value));
			table.put(put);
			System.out.println("insert recored " + rowKey + " to table " + tableName + " ok.");
			table.close();
	    }
		
		// Get 객체를 사용하여 입력하는 rowKey에 대한 값을 불러온다.
		public static void getOneRecord (String tableName, String rowKey) throws IOException{
	        
			HTable table = new HTable(conf, tableName);
			Get get = new Get(rowKey.getBytes());
			Result rs = table.get(get);
	   
			for(Cell c : rs.listCells()){
				System.out.print(new String(CellUtil.cloneRow(c)) + ":" );
				System.out.print(new String(CellUtil.cloneFamily(c)) + ":" );
				System.out.print(new String(CellUtil.cloneQualifier(c)) + ":" );
				System.out.print(c.getTimestamp() + ":" );
				System.out.println(new String(CellUtil.cloneValue(c)));
	         }
	   
			table.close();
	    }
		
		// 지정된 Table의 모든 값을 불러온다.
		public static void getAllRecord (String tableName) throws IOException {
			HTable table = new HTable(conf, tableName);
			Scan s = new Scan();
			ResultScanner ss = table.getScanner(s);
	   
			for(Result r : ss){
				for(Cell c : r.rawCells()){
					System.out.print(new String(CellUtil.cloneRow(c)) + ":");
					System.out.print(new String(CellUtil.cloneFamily(c)) + ":");
					System.out.print(new String(CellUtil.cloneQualifier(c)) + ":");
					System.out.print(c.getTimestamp() + ":");
					System.out.println(new String(CellUtil.cloneValue(c)));
	            }
	        }
	   
			table.close();
	    }
		
		// 지정하는 rowKey에 값을 삭제한다.
		public static void delRecord(String tableName, String rowKey) throws IOException {
			HTable table = new HTable(conf, tableName);
			Delete del = new Delete(rowKey.getBytes());
			table.delete(del);
			System.out.println("del recored " + rowKey + " ok.");
			table.close();
	    }

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		/*******************************************************************************************
		 * {
     "aaa company" :
         "employee" : {
	               "depart" : "HRD"
	               "under40" : { 
	                     									200810 : "20",
                     										200811 : "16"
               										},
               	"above40" : { 
                     										200810 : "40"
                     										200811 : "34" 
               										},
          				}, 
     "bbb company" :
         "employee" : {
               "depart" : "marketing"
           			"above40" : {
           			                                      200901 : "25"
           			                                      200907 : "17"
           			                    },
          				} 
     			}  */
		 /*********************************************************************************************/
		
		
		String tablename = "companies";
		String family = "employee";
        
		try {
			System.out.println("===========create table========");
			HBaseTest.creatTable(tablename, family);
            
			System.out.println("===========add records========");
			HBaseTest.addRecord(tablename, "aaa company", family, "depart", 0, "HRD");
			HBaseTest.addRecord(tablename, "aaa company", family, "under40", 200810, new Integer(20).toString());
			HBaseTest.addRecord(tablename, "aaa company", family, "under40", 200811, new Integer(16).toString());
			HBaseTest.addRecord(tablename, "aaa company", family, "above40", 200810, new Integer(40).toString());
			HBaseTest.addRecord(tablename, "aaa company", family, "above40", 200811, new Integer(34).toString());
			HBaseTest.addRecord(tablename, "bbb company", family, "depart", 0, "marketing");
			HBaseTest.addRecord(tablename, "bbb company", family, "above40", 200901, new Integer(25).toString());
			HBaseTest.addRecord(tablename, "bbb company", family, "above40", 200907, new Integer(17).toString());
            
			System.out.println("===========get one record========");
			HBaseTest.getOneRecord(tablename, "aaa company");

			System.out.println("===========show all record========");
			HBaseTest.getAllRecord(tablename);
			
			System.out.println("===========del one record========");
			HBaseTest.delRecord(tablename, "bbb company");
			HBaseTest.getAllRecord(tablename);
			
			System.out.println("===========show all record========");
			HBaseTest.getAllRecord(tablename);
			HBaseTest.deleteTable(tablename);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
	}
}

