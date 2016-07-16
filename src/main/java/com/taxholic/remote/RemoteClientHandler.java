package com.taxholic.remote;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.taxholic.remote.util.JSchUtil;
import com.taxholic.remote.util.SysUtil;


public class RemoteClientHandler {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	//서버 인스톨
    @Option(name="-i", usage="서버설치명 arguments { apache | nginx | tomcat | svn | mysql | ftp } 콤마(,)로 구분하여 다중설치")
    private String install;
    
    //암호키
    @Option(name="-k", usage="암호키")
    private String  key;
    
     //셀 명령어
    @Option(name="-c", usage="셀명령")
    private String  cmd;
    
    //암호 문자열
    @Option(name="-e", usage="암호화 문자열")
    private String  encrypt;

     //jenkins deploy
    @Option(name="-d", usage="war 경로")
    private String  deploy;
    
    // receives other command line parameters than options
    @Argument
    private List<String> arguments = new ArrayList<String>();

	 public void cmd(String[] args) throws IOException, URISyntaxException {
		 
		 CmdLineParser parser = new CmdLineParser(this);
	        try {
	            parser.parseArgument(args);
	        } catch( CmdLineException e ) {
	            usage(parser);
	            return;
	        }
	        
	        RemoteClientAction rca = new RemoteClientAction();
	        
	        //key test;
	        if(encrypt ==null && key != null  && rca.keyTest(key) == false) return;
	        
	        //서버설치 
	        if(install != null && key != null){
	        	String[] installs = install.split(",");
	        	 for(String str : installs)
	        		 logger.debug("install : " + str);
	      
	        	logger.debug("key : " + key);
	        	rca.install(installs, key);
	        
	        //셀명령
	        }else if(cmd != null  && key != null){
	        	logger.debug("cmd : " + cmd);
	        	logger.debug("key : " + key);
	        	rca.cmd(cmd, key);
	        //암호화
	        }else if(encrypt != null  && key != null){
	        	logger.debug("encrypt : " + encrypt);
	        	logger.debug("key : " + key);
	        	rca.encrypt(encrypt, key);
	        //deploy
	        }else if(deploy != null  && key != null){
	        	logger.debug("deploy : " + deploy);
	        	logger.debug("key : " + key);
	        	
	        }else{
	        	usage(parser);
	        }
	        
	     // access non-option arguments
	     /*   
	        System.out.println("other arguments are:");
	        for( String s : arguments )
	            System.out.println(s);
	    */	
	}
	 
	
	 public void usage(CmdLineParser parser){
    	 System.out.print("Uage :\n");
    	 System.out.println("java -jar remote-client.jar [options...] arguments...");
    	 parser.printUsage(System.out);
    	 System.out.println();
    	 System.out.println("Example:");
    	 System.out.println("아파치설치\t java -jar remote-client.jar -i nginx,tomcat -k 암호키");
    	 System.out.println("암호화\t\t java -jar remote-client.jar -e 문자열 -k 암호키");
    	 System.out.println("셀명령\t\t java -jar remote-client.jar -c pwd or \"ls -al\"");
    	 System.out.println("deploy\t\t java -jar remote-client.jar -d /usr/local/temp/xxx.war");
    }
	 
	
}
