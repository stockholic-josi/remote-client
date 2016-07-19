package com.taxholic.remote.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class SysUtil {
	
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