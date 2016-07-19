package com.taxholic.remote;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taxholic.remote.util.JSchUtil;
import com.taxholic.remote.util.SysUtil;


public class RemoteClientAction {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private String resourcePath;
	
	public void install(String[] installs, String key) throws URISyntaxException {
		for(String str : installs){
			installserver(str,key);
		}
    }
	
	public void installserver(String server, String key) throws URISyntaxException{
		
		String shFile = resourcePath+ "/script/install_" + server + ".sh";
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
			
			setConfig(server, resourcePath, js);
			
		}
	}
	
	public void setConfig(String server, String resourcePath, JSchUtil js){
		
		if("apache".equals(server)){
			String[] frFile =	{resourcePath + "/config/" + server + "/httpd.conf"
									,resourcePath + "/config/" + server + "/workers.properties"};
			String[] toFile =	{"/usr/local/"+ server +"/conf/httpd.conf"
									,"/usr/local/" + server + "/conf/workers.properties"};
			copyRemote(frFile,toFile,js);
			
		}else if("nginx".equals(server)){
			String[] frFile =	{resourcePath + "/config/" + server + "/nginx.conf"
									,resourcePath + "/config/" + server + "/nginxd"};
			String[] toFile =	{"/usr/local/" + server + "/conf/nginx.conf"
									,"/etc/rc.d/init.d/nginxd"};
			copyRemote(frFile,toFile,js);
			 
			String[] cmd = {
				"chmod 755 /etc/rc.d/init.d/nginxd"
			};
			js.exec(cmd);
			 
		}else 	if("tomcat".equals(server)){
			String[] frFile =	{resourcePath + "/config/" + server + "/catalina.sh"
									,resourcePath + "/config/" + server + "/server.xml"
									,resourcePath + "/config/" + server + "/tomcatd"
									,resourcePath + "/sample.war"};
			String[] toFile =	{"/usr/local/" + server + "/bin/catalina.sh"
									,"/usr/local/" + server + "/conf/server.xml"
									,"/usr/local/" + server + "/bin/tomcatd"
									,"/usr/local/www/sample.war"};
			copyRemote(frFile,toFile,js);
			
			String[] cmd = {
				"chmod 755 /usr/local/tomcat/bin/tomcatd"
				,"chmod 755 /usr/local/tomcat/bin/catalina.sh"
				,"cp /usr/local/tomcat/bin/tomcatd /etc/rc.d/init.d/tomcatd"
				,"cd /usr/local/www && jar -xf sample.war"
				,"cd /usr/local/www && rm -rf  META-INF sample.war"
			};
			js.exec(cmd);
			
		}else 	if("mysql".equals(server)){
			 File file= new File(resourcePath + "/config/" + server + "/my.cnf");
			 js.scpTo(file, "/etc/my.cnf");
		}else 	if("php".equals(server)){
			 String[] frFile =	{resourcePath + "/config/" + server + "/www.conf"
									,resourcePath + "/config/" + server + "/Info.php"};
			String[] toFile =	{"/usr/local/php/etc/php-fpm.d/www.conf"
									,"/usr/local/www/php/application/controllers/Info.php"};
			copyRemote(frFile,toFile,js);
		}else 	if("ftp".equals(server)){
			
		}
		
	}
	
	public void copyRemote(String[] frFile, String[] toFile, JSchUtil js){
		for(int i = 0; i < frFile.length; i++){
			File file =  new File(frFile[i]);
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
				SysUtil.decrypt(SysUtil.getProperty("host"), key)
				,Integer.parseInt(SysUtil.getProperty("port"))
				,SysUtil.decrypt(SysUtil.getProperty("user"), key)
				,SysUtil.decrypt(SysUtil.getProperty("password"), key)
			);
		 
		 return js;
	 }
	 
	 public String getResource() throws URISyntaxException{
		 File jarPath = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		 String resourcePath = jarPath.getParent() + "/resources";
		 SysUtil.setProperty( resourcePath + "/server.properties");
		 return resourcePath;
	 }
	 
	 public boolean keyTest(String key) throws URISyntaxException{
		 resourcePath = getResource();
		 return SysUtil.decrypt(SysUtil.getProperty("host"), key)== null ? false : true ;
	 }
}
