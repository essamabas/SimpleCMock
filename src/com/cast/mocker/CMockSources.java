package com.cast.mocker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import logger.MyLogger;

public class CMockSources {

	private final static Logger logger = MyLogger.getLogger();
	
	public CMockSources() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		
		// Initialise Logger
		try {
			MyLogger.setup();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// get list of Files in Passed Folder
		
		// Set includePaths
		String[] includePaths = new String[1];
		includePaths[0] = "docs/sample001";
		
		//
		List<String> results = new ArrayList<String>();
		File[] files = new File("docs/sample001").listFiles();
		//If this pathname does not denote a directory, then listFiles() returns null. 
		for (File file : files) {
		    if (file.isFile()) {
		        file.getAbsolutePath();
		        // Mock only C-Files
		        if(file.getName().endsWith(".c")) {
		        	results.add(file.getName());
		        	// Create Mock Prepressor
		        	CMockerCreator MyCMocker = new CMockerCreator();
		        	MyCMocker.CreateMockPrepressor(file.getAbsolutePath(), includePaths);
		        }
		        
		    }
		}
	}

}
