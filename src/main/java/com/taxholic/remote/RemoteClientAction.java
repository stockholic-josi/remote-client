package com.taxholic.remote;

import java.io.File;
import java.net.URISyntaxException;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taxholic.remote.util.JSchUtil;
import com.taxholic.remote.util.SysUtil;


public class RemoteClientAction {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private String resource;
	private Configuration prop; 
	
	public RemoteClientAction() throws URISyntaxException, ConfigurationException{
		 File jarPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		this.resource = jarPath.getParent() + "/resources";
		this.prop = new PropertiesConfiguration(resource + "/server.properties");
	}
	
	public void install(String[] installs, String key) throws URISyntaxException {
		for(String str : installs){
			installserver(str,key);
		}
    }
	
	public void installserver(String server, String key) throws URISyntaxException{
		
		String shFile = resource+ "/script/install_" + server + ".sh";
		File isFile = new File(shFile);
		if(!isFile.exists()){
			System.out.println("스크립트가 존재하지 않습니다");
			return;
		}
		
		JSchUtil js = login(key);
		
		if(shFile != null){
			
			//설치 스크립트 시랭
			js.scpTo(new File(shFile), "install_"+ server  +".sh");
			String[] installCmd = {
				"chmod 755 install_" + server  + ".sh"
				,"./install_" + server  + ".sh"
			};
			js.exec(installCmd);
			
			//설치 후 작업 server.properties
			copyResource(server,js);
			String[] afterCmd =  prop.getStringArray(server + ".cmd");
			if(afterCmd.length > 0) js.exec(afterCmd);
			
		}
	}
	
	public void copyResource(String server, JSchUtil js){
		
		String[] frFile = prop.getStringArray(server + ".frFile");
		String[] toFile = prop.getStringArray(server + ".toFile");
		
		for(int i = 0; i < frFile.length; i++){
			File file =  new File(resource + frFile[i]);
			js.scpTo(file, toFile[i]);
		}
	}

	
	public void encrypt(String str, String key){
		System.out.println(SysUtil.encrypt(str, key));
	}
	
	public void cmd(String str, String key) throws URISyntaxException{
		JSchUtil js = login(key);
    	js.exec(str);
	}
	
	public void deploy(){
		
	}
	
	 public JSchUtil login(String key) throws URISyntaxException{
		 
		 JSchUtil  js = new JSchUtil(
				SysUtil.decrypt(prop.getString("host"), key)
				,prop.getInt("port")
				,SysUtil.decrypt(prop.getString("user"), key)
				,SysUtil.decrypt(prop.getString("password"), key)
			);
		 
		 return js;
	 }
	 
	 
	 public boolean keyTest(String key) throws URISyntaxException{
		 return SysUtil.decrypt(prop.getString("host"), key)== null ? false : true ;
	 }
}
