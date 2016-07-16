package com.taxholic.remote.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class SysUtil {
	
	private static Properties properties  =  null;
	
	
	/***
	 * 파일읽기
	 * @param fileName : 파일명
	 * @return
	 */
	public static List<String> getFileRead(String fileName) {
		
		List<String> list = new ArrayList<String>();
		BufferedReader in = null;
		try{
			File oTmpFile = new File(fileName);

			in = new java.io.BufferedReader(new FileReader(oTmpFile));
			while(in.ready()) {
				list.add(in.readLine()+ "\n");
			}

		}catch (Exception e){
			
		}finally{
			if(in != null) try{in.close();} catch (IOException e) {}
		}

		return list;
	}
	
	/**
	 * 프로퍼티 생성
	 * @param fileName : 파일명
	 * @return
	 */
	public static void setProperty( String fileName){
		properties = new Properties();
		try {
			properties.load(new FileInputStream(fileName));
		} catch (IOException e) {}
		
	}
	
	/**
	 * 프로퍼티 값
	 * @param value
	 * @return
	 */
	public static String getProperty(String value){
		return  properties.getProperty(value);
	}
	
	
	/**
	 * 암호화
	 * @param str
	 * @return
	 */
	public static String encrypt(String str, String key){
		StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
		standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
		standardPBEStringEncryptor.setPassword(key);
		return standardPBEStringEncryptor.encrypt(str);
	}
	
	/**
	 * 복구화
	 * @param str
	 * @return
	 */
	public static String decrypt(String str, String key){
		String decryptStr = null;
		try{
			StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
			standardPBEStringEncryptor.setAlgorithm("PBEWithMD5AndDES");
			standardPBEStringEncryptor.setPassword(key);
			decryptStr =  standardPBEStringEncryptor.decrypt(str);
		}catch(Exception e){
			System.err.println("암호 복구화 실패");
		}
		return decryptStr;
				
	
	}
	
	
}