package com.cast.mocker;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.logging.Logger;

import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTName;
import org.eclipse.core.runtime.CoreException;

import com.cast.parser.ASTCParser;
import logger.MyLogger;

/**
 * C Mock Creater
 * @author essam.abas
 */
public class CMockerCreator {

	// use the classname for the logger, this way you can refactor
	private final static Logger logger = MyLogger.getLogger();
	private static String newline = System.getProperty("line.separator");
	private int WriterOffset = 0;
	
	//Initialse Map
	public HashMap<Integer,Object> MockFunctionList;
	HashMap<String,String> MockFunction;

	public CMockerCreator() {
		// TODO Auto-generated constructor stub
		this.MockFunctionList = new HashMap<Integer, Object>();
		this.MockFunction = new HashMap<String, String>();
		this.WriterOffset = 0;
	}
	
	  /**
	  * insert content to filename
	  * @param	filepath	of the file to be modified
	  * @param	offset	Starting location in the file to insert content
	  * @param	content	Content to be inserted into the file   
	  * @see	http://stackoverflow.com/questions/28913543/java-writing-to-a-file-with-specific-offset
	  * @throws IOException  * 	
	  */  
	  private static void insert(String filepath, long offset, String content) throws IOException {
		  
		  // Extract Bytes from content String
		  byte[] contents = content.getBytes();
		  
		  RandomAccessFile r = new RandomAccessFile(filepath, "rw");
		  RandomAccessFile rtemp = new RandomAccessFile(filepath+"Temp", "rw");
		  long fileSize = r.length(); 
		  FileChannel sourceChannel = r.getChannel();
		  FileChannel targetChannel = rtemp.getChannel();
		  sourceChannel.transferTo(offset, (fileSize - offset), targetChannel);
		  sourceChannel.truncate(offset);
		  r.seek(offset);
		  r.write(contents);
		  long newOffset = r.getFilePointer();
		  targetChannel.position(0L);
		  sourceChannel.transferFrom(targetChannel, newOffset, (fileSize - offset));
		  sourceChannel.close();
		  targetChannel.close();
		  rtemp.close();
		  r.close();
	  }
	  
	  
    /**
    * Create Mock Preprocessor with the the pattern "FunctionName" + _MOCK
    * @return boolean flag as an indication of successful operation
    * @param	filepath	of the file to be modified    
    * @param 	includePaths	include paths to be scanned with the source file
    * @throws CoreException
    */	  
	public boolean CreateMockPrepressor(String FilePath, String[] includePaths) {
		ASTCParser MyCPaser = new ASTCParser();
		
		IASTTranslationUnit translationUnit;
		try {
			translationUnit = MyCPaser.parse(FilePath, includePaths);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	     logger.info("-----------------------------------------------------");
	     logger.info("--------------------getAllPreprocessorStatements--------------------------------");
	     IASTPreprocessorStatement[] preprocessors = translationUnit.getAllPreprocessorStatements();
	     for (IASTPreprocessorStatement preprocessor : preprocessors) {
	    	 //search for Mocks defines
	    	 if(preprocessor.getRawSignature().contains("#ifndef") && preprocessor.getRawSignature().contains("_MOCK")){
	    		 logger.info("preprocessor - " + preprocessor.getRawSignature());
	    	 }
	      }
		 
	     logger.info("-----------------------------------------------------");
	     IASTDeclaration[] declarations = translationUnit.getDeclarations();
	     for (IASTDeclaration declaration : declarations) {
	    	 String FunctionCASTName = new String("");
	    	 
	   	  	 HashMap<String,String> MockFunction = new HashMap();         
	         logger.info("declaration: " + declaration + " ->  " + declaration.getRawSignature());
	         logger.info("-- Parent: " + declaration.getParent().getClass().getSimpleName());
	         logger.info("-- FileLocation: " + declaration.getFileLocation().asFileLocation());
	         logger.info("-- -- Offset: " + declaration.getFileLocation().getNodeOffset());
	         logger.info("-- -- Length: " + declaration.getFileLocation().getNodeLength());
	         logger.info("-- -- StartingLine: " + declaration.getFileLocation().getStartingLineNumber());
	         logger.info("-- -- EndingLine: " + declaration.getFileLocation().getEndingLineNumber());
	         
	         // Push MOCK-Preprocessor
	         for(IASTNode node : declaration.getChildren()) {
	        	 if(node instanceof CASTFunctionDeclarator) {
	        		 for(IASTNode subnode : node.getChildren()) {
	        			 if(subnode instanceof CASTName) {
	        			
	  					   // Push Information to Map
	  					   MockFunction.put("declaration", declaration.getRawSignature());
						   MockFunction.put("File.Location", declaration.getFileLocation().getFileName());
						   MockFunction.put("File.Offset", String.valueOf(declaration.getFileLocation().getNodeOffset()));
						   MockFunction.put("File.Length", String.valueOf(declaration.getFileLocation().getNodeLength()));
						   MockFunction.put("File.StartingLine", String.valueOf(declaration.getFileLocation().getStartingLineNumber()));
						   MockFunction.put("File.EndingLine", String.valueOf(declaration.getFileLocation().getEndingLineNumber()));
	        		         
						   logger.info("-- CASTName: " + subnode.getRawSignature());
						   // Push 
						   FunctionCASTName = subnode.getRawSignature();
						   MockFunction.put("CASTName", subnode.getRawSignature());					   
						   for (IASTPreprocessorStatement preprocessor : preprocessors) {
							   //search for Mocks defines
							   if(preprocessor.getRawSignature().contains("#ifndef " + FunctionCASTName + "_MOCK")){
								   logger.info("preprocessor - " + preprocessor.getRawSignature());
								   MockFunction.put("preprocessor", preprocessor.getRawSignature());
							   }
						   }
						   // if Mock-preprocessor was not found
						   if (!MockFunction.containsKey("preprocessor")) {
							   // Insert End Mock-preprocessor
							   try {
									// Insert Mock-preprocessor
									insert(FilePath,
										   (Integer.valueOf(MockFunction.get("File.Offset")) + WriterOffset), 
										   newline + "#ifndef " + FunctionCASTName + "_MOCK" + newline);
									// Increment WriterOffset								
									WriterOffset += new String(newline + "#ifndef " + FunctionCASTName + "_MOCK" + newline).length();
									// Insert Mock-preprocessor-Ending
									insert(FilePath,(Integer.valueOf(MockFunction.get("File.Offset")) 
											   + Integer.valueOf(MockFunction.get("File.Length")) + WriterOffset) 
											   , newline + "#endif " + newline);
									// Increment WriterOffset
									WriterOffset += new String(newline + "#endif " + newline).length();								

							   } catch (NumberFormatException | IOException e) {
								   // TODO Auto-generated catch block
								   e.printStackTrace();
								   logger.severe("CMockerCreater.CreateMockPrepressor encountered an Runtime Error");
								   return false;
							   }	catch(Exception e){
								   e.printStackTrace();
								   logger.severe("CMockerCreater.CreateMockPrepressor encountered an Runtime Error");								   
							   }

						   }
						   // Push to MockFunctionList
						   MockFunctionList.put(MockFunctionList.size()+1, MockFunction);
					   }
	        		 }
				   }
	        	 }
	         }		
		return true;
	}
	


}
