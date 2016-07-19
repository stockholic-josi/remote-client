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
			js.scpTo(new File(shFile), "install_"+ server  +".sh");
			String[] cmd = {
				"chmod 755 install_" + server  + ".sh"
				,"./install_" + server  + ".sh"
			};
			js.exec(cmd);
			
			setConfig(server, resource, js);
			
		}
	}
	
	public void setConfig(String server, String resource, JSchUtil js){
		
		if("apache".equals(server)){
			String[] frFile =	{resource + "/config/" + server + "/httpd.conf"
									,resource + "/config/" + server + "/workers.properties"};
			String[] toFile =	{"/usr/local/"+ server +"/conf/httpd.conf"
									,"/usr/local/" + server + "/conf/workers.properties"};
			copyRemote(frFile,toFile,js);
			
		}else if("nginx".equals(server)){
			String[] frFile =	{resource + "/config/" + server + "/nginx.conf"
									,resource + "/config/" + server + "/nginxd"};
			String[] toFile =	{"/usr/local/" + server + "/conf/nginx.conf"
									,"/etc/rc.d/init.d/nginxd"};
			copyRemote(frFile,toFile,js);
			 
			String[] cmd = {
				"chmod 755 /etc/rc.d/init.d/nginxd"
			};
			js.exec(cmd);
			 
		}else 	if("tomcat".equals(server)){
			 copyRemote(server,js);
			
			String[] cmd = {
				"chmod 755 /usr/local/tomcat/bin/tomcatd"
				,"chmod 755 /usr/local/tomcat/bin/catalina.sh"
				,"cp /usr/local/tomcat/bin/tomcatd /etc/rc.d/init.d/tomcatd"
				,"cd /usr/local/www && jar -xf sample.war"
				,"cd /usr/local/www && rm -rf  META-INF sample.war"
			};
			js.exec(cmd);
			
		}else 	if("mysql".equals(server)){
			 File file= new File(resource + "/config/" + server + "/my.cnf");
			 js.scpTo(file, "/etc/my.cnf");
		}else 	if("php".equals(server)){
			 String[] frFile =	{resource + "/config/" + server + "/www.conf"
									,resource + "/config/" + server + "/Info.php"};
			String[] toFile =	{"/usr/local/php/etc/php-fpm.d/www.conf"
									,"/usr/local/www/php/application/controllers/Info.php"};
			copyRemote(frFile,toFile,js);
		}else 	if("ftp".equals(server)){
			
		}
		
	}
	
	public void copyRemote(String[] frFile, String[] toFile, JSchUtil js){
		for(int i = 0; i < frFile.length; i++){
			File file =  new File(resource + frFile[i]);
			 js.scpTo(file, toFile[i]);
		}
	}

	public void copyRemote(String server, JSchUtil js){
		
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
