package com.taxholic.remote;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.commons.configuration.ConfigurationException;

public class RemoteClient {
	
	 public static void main(String[] args) throws IOException, URISyntaxException, ConfigurationException {
		 RemoteClientHandler handler = new RemoteClientHandler();
		 handler.cmd(args);
	 }
         
}
