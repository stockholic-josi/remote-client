package com.taxholic.remote;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taxholic.remote.util.SysUtil;

public class PropertyEncryptTest{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private String host = "10.5.22.106";
	 private String user = "root";
	 private String password = "123123";
	
	@Test
	public void encryptTest() {
		
		logger.debug("-------------------------------------------------------------------------------> start");
		
		logger.debug(SysUtil.encrypt(host,  "1234"));
		logger.debug(SysUtil.encrypt(user, "1234"));
		logger.debug(SysUtil.encrypt(password, "1234"));
		
		
		logger.debug("-------------------------------------------------------------------------------> end");
	}
}
