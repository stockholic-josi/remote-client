package com.taxholic.remote;

import java.io.File;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taxholic.remote.util.JSchUtil;
import com.taxholic.remote.util.SysUtil;

public class JSchTest{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	
	@Test
	public void Test_아파티깔아요() {
		
		logger.debug("-------------------------------------------------------------------------------> start");
		
		String resourcePath = ClassLoader.getSystemResource("").getPath() + "../resources";
		SysUtil.setProperty( resourcePath + "/server.properties");
		
		JSchUtil  js = new JSchUtil(
				SysUtil.decrypt(SysUtil.getProperty("host"), "1234")
				,Integer.parseInt(SysUtil.getProperty("port"))
				,SysUtil.decrypt(SysUtil.getProperty("user"), "1234")
				,SysUtil.decrypt(SysUtil.getProperty("password"), "1234")
		);
//
//		try{
//			
//			String shFile = resourcePath+ "/script/install_apache.sh";
//			if(shFile != null){
//				 js.scpTo(new File(shFile), "install_sh/install_apache.sh");
//				 js.exec("bash install_sh/install_apache.sh");
//			}
//		}catch(Exception e){
//			System.out.println("에러 : 실행파일이 존재하지 않습니다.");
//		}
	
		
//		
//		 //파일 전송
//		 File file = new File("D:/99.tmp/javamelody-1.60.0.war");
//		 js.scpTo(file, "/usr/local/source/javamelody-1.60.0.war");
		 
		 
//		//리모트 파일 수신
		File file = new File("D:/nginx.conf");
		js.scpFrom("/usr/local/nginx-1.9.9/conf/nginx.conf",file);
		

		
		logger.debug("-------------------------------------------------------------------------------> end");
	}
	

}
