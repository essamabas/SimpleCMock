package com.cast.mocker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFileChooser;

import org.eclipse.core.runtime.CoreException;

import logger.MyLogger;

public class CMockSources {

	private final static Logger logger = MyLogger.getLogger();
	
	public CMockSources() {
		// TODO Auto-generated constructor stub		
	}

    /**
    * Mock Files in Passed Folder
    * @return boolean flag as an indication of successful operation
    * @param	Files	Folder    
    * @param 	includePaths	include paths to be scanned with the source file
    * @throws CoreException
    */	
	public List<String> MockFiles(String folderpath, String[] includePaths) {
		
		// List of mocked-files
		List<String> results = new ArrayList<String>();
		File[] files = new File(folderpath).listFiles();
		
		for (File file : files) {
		    if (file.isFile()) {
		        // Mock only C-Files
		        if(file.getName().endsWith(".c")) {
		        	results.add(file.getName());
		        	// Create Mock Prepressor
		        	CMockerCreator MyCMocker = new CMockerCreator();
		        	MyCMocker.CreateMockPrepressor(file.getAbsolutePath(), includePaths);
		        }
		    }
		}
		return results;
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
		String folderpath = "docs/sample001";
		
		// Set includePaths
		String[] includePaths = new String[1];
		includePaths[0] = "docs/sample001";
		//
		CMockSources cm = new CMockSources();
		List<String> results = cm.MockFiles(folderpath, includePaths);
	}

}
