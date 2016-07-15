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
    @Option(name="-i",handler=BooleanOptionHandler.class, usage="서버설치명 arguments{apache | nginx | tomcat | svn | mysql | ftp}")
    private boolean  install;
    
    //암호화
    @Option(name="-e",handler=BooleanOptionHandler.class, usage="암호화")
    private boolean  encrypt;
    
    //암호키
    @Option(name="-k",handler=BooleanOptionHandler.class, usage="암호키")
    private boolean  key;
    
    //셀 명령어
    @Option(name="-c",handler=BooleanOptionHandler.class, usage="셀명령")
    private boolean  cmd;
    
    //jenkins deploy
    @Option(name="-d",handler=BooleanOptionHandler.class, usage="war 경로")
    private boolean  deploy;
    
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

	        //서버설치
	        if( install  && key) {
	        	logger.debug("-i -k flag is set");
	        	logger.debug(arguments.get(0) );
	        	logger.debug(arguments.get(1) );
	        	
	        	 rca.install(arguments);
	        	
	        //셀명령
	        }else if( cmd && key) {
	        	logger.debug("-c -k flag is set");
	        	logger.debug(arguments.get(0) );
	        	logger.debug(arguments.get(1) );
	        	
	        	 rca.cmd(arguments.get(0), arguments.get(1));
	        	
	        //암호화
	        }else if( encrypt  && key) {
	        	logger.debug("-e -k flag is set");
	        	logger.debug(arguments.get(0));
	        	logger.debug(arguments.get(1));
	        	
	        	rca.encrypt(arguments.get(0), arguments.get(1));
	        	
	       	//deploy
	        }else if( deploy  && key) {
	        	logger.debug("-d flag is set");
	        	logger.debug(arguments.get(0));
	        	logger.debug(arguments.get(1));
	        }else{
	        	usage(parser);
	        }
		 
	}
	 
	
	 public void usage(CmdLineParser parser){
    	 System.out.print("Uage :\n");
    	 System.out.println("java -jar remote-client.jar [options...] arguments...");
    	 parser.printUsage(System.out);
    	 System.out.println();
    	 System.out.println("Example:");
    	 System.out.println("아파치설치\t java -jar remote-client.jar -i apache -k 암호키");
    	 System.out.println("암호화\t java -jar remote-client.jar -e 문자열 -k 암호키");
    	 System.out.println("셀명령\t java -jar remote-client.jar -c pwd or \"ls -al\"");
    	 System.out.println("deploy\t java -jar remote-client.jar -d /usr/local/temp/xxx.war");
    }
	 
	
}
