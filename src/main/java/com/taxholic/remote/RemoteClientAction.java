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
	
	
	public void install(List<String> args) throws URISyntaxException {
		
		String key = args.get(args.size() - 1);
		
		for(int i = 0; i < args.size() -1; i++){
			installserver(args.get(i),key);
		}
		
    }
	
	public void installserver(String server, String key) throws URISyntaxException{
		
		String resourcePath = getResource();
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
			File file1 =  new File(resourcePath + "/config/" + server + "/httpd.conf");
			 js.scpTo(file1, "/usr/local/apache/conf/httpd.conf");
			 
			 File file2 =  new File(resourcePath + "/config/" + server + "/workers.properties");
			 js.scpTo(file2, "/usr/local/apache/conf/workers.properties");

		}else if("nginx".equals(server)){
			 File file1 =  new File(resourcePath + "/config/" + server + "/nginx.conf");
			 js.scpTo(file1, "/usr/local/nginx/conf/nginx.conf");
			 
			 File file2 =  new File(resourcePath + "/config/" + server + "/nginxd");
			 js.scpTo(file2, "/etc/rc.d/init.d/nginxd");
			 
			String[] cmd = {
					"chmod 755 /etc/rc.d/init.d/nginxd"
			};
			js.exec(cmd);
			 
		}else 	if("tomcat".equals(server)){
			File file1 =  new File(resourcePath + "/config/" + server + "/catalina.sh");
			 js.scpTo(file1, "/usr/local/tomcat/bin/catalina.sh");
			 
			 File file2 =  new File(resourcePath + "/config/" + server + "/server.xml");
			 js.scpTo(file2, "/usr/local/tomcat/conf/server.xml");
			 
			 File file3 =  new File(resourcePath + "/config/" + server + "/tomcatd");
			 js.scpTo(file3, "/usr/local/tomcat/bin/tomcatd");

			 File file4 =  new File(resourcePath + "/sample.war");
			 js.scpTo(file4, "/usr/local/www/sample.war");
			 
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
		}else 	if("svn".equals(server)){
			
		}else 	if("ftp".equals(server)){
			
		}
		
	}
	
	
	public void encrypt(String str, String key){
		System.out.println(SysUtil.encrypt(str, key));
	}
	
	public void cmd(String str, String key) throws URISyntaxException{
		getResource();
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
}
