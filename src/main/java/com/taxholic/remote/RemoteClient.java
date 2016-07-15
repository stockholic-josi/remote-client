package com.taxholic.remote;

import java.io.IOException;
import java.net.URISyntaxException;

public class RemoteClient {
	
	 public static void main(String[] args) throws IOException, URISyntaxException {
		 RemoteClientHandler handler = new RemoteClientHandler();
		 handler.cmd(args);
	 }
         
}
