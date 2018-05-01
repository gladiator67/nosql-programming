package com.aaa.hdfs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class FileTest {
	
	private static final String srcDir = "/home/user01/" ;

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
String fileName = "HadoopFileTest.txt";
		
		// Path 객체와 FileSystem객체를 생성한다.
		Path path = new Path(srcDir + fileName);
		FileSystem fs = FileSystem.get(URI.create(srcDir + fileName), new Configuration());
		
		if(fs.exists(path)) { // 파일이 존재하는지 여부를 확인한다.
			// 입력 스트림을 연다.
			BufferedReader br =new BufferedReader(new InputStreamReader(fs.open(path)));
			
			while(br.ready()){ 
				// 파일 내용을 읽어 온다.           	
				String line = br.readLine();
				System.out.println(line);
	        }
			
			// FileStatus 객체를 불러온다.
			FileStatus fStatus = fs.getFileStatus(path);
			
			if(fStatus.isFile()) {
				System.out.println("");
				System.out.println("===========================================");
				// 파일의 속성을 불러와 콘솔에 표시한다.
				System.out.println("File Block Size : " + fStatus.getBlockSize());
				System.out.println("Group of File   : " + fStatus.getGroup());
				System.out.println("Owner of File   : " + fStatus.getOwner());
				System.out.println("File Length     : " + fStatus.getLen());
			} else {
				System.out.println("파일이 아닙니다.");
			}
		} else {
			System.out.println("파일을 찾을 수 없습니다.");
		}
	}

}
