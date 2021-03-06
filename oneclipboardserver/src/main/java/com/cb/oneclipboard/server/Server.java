package com.cb.oneclipboard.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.cb.oneclipboard.lib.ApplicationProperties;
import com.cb.oneclipboard.lib.DefaultPropertyLoader;
import com.cb.oneclipboard.server.admin.AdminServer;
import com.cb.oneclipboard.server.logging.LevelBasedFileHandler;

public class Server {
	private final static Logger LOGGER = Logger.getLogger(Server.class.getName());

	public static final String[] PROP_LIST = { "config.properties" };
	private static int serverPort;
	private static ServerSocket serverSocket = null;

	public static void main(String[] args) throws Exception {
		init(args);
		start();
	}
	
	public static void start(){
		Thread startupThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					serverSocket = new ServerSocket(serverPort);
					LOGGER.info("Server started on port " + serverPort);
				} catch (IOException e) {
					LOGGER.log(Level.SEVERE, "Could not listen on port: " + serverPort);
					System.exit(-1);
				}

				while (true) {
					ServerThread serverThread = null;
					try {
						serverThread = new ServerThread(serverSocket.accept());
					}catch(SocketException e){
						break; // exit loop
					}catch (Exception e) {
						try {
							serverThread.close();
						} catch (Exception ex) {
							LOGGER.log(Level.WARNING, "Unable to close server thread properly " + ex.getMessage());
						}
						LOGGER.log(Level.WARNING, "Connection lost: " + e.getMessage());
					}
				}
				
			}
		}, "Startup Thread");
		startupThread.start();
	}
	
	public static boolean restart(){
		try{
			LOGGER.info("Restarting server...");
			serverSocket.close();
			start();
			return true;
		}catch(Exception e){
			LOGGER.log(Level.SEVERE, "Restart failed!", e);
			return false;
		}
	}

	private static void init(String[] args) throws SecurityException, IOException {
		// set up loggers
		Logger.getLogger("").addHandler(new LevelBasedFileHandler("oneclipboardserver.log", Arrays.asList(Level.INFO, Level.WARNING)));
		Logger.getLogger("").addHandler(new LevelBasedFileHandler("oneclipboardserver.err", Arrays.asList(Level.SEVERE)));

		// Load properties
		ApplicationProperties.loadProperties(PROP_LIST, new DefaultPropertyLoader());
		serverPort = ApplicationProperties.getIntProperty("server_port");

		if (args.length > 0) {
			try {
				serverPort = Integer.parseInt(args[0]);
			} catch (Exception e) {
			}
		}

		// Start admin server
		try {
			AdminServer.start(args);
			ServerThreadCleaner.start(args);
		} catch (Exception e) {
			LOGGER.log(Level.SEVERE, "Error starting admin server", e);
		}
	}

}