package com.cast.parser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.cdt.core.index.IIndex;

import logger.MyLogger;

public class ASTCParser {

	// use the classname for the logger, this way you can refactor
	private final static Logger logger = MyLogger.getLogger();
	  
	public ASTCParser() {
		// TODO Auto-generated constructor stub
	}	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
    /**
    * Parses the source code, return the AST.
    * @return IASTTranslationUnit Object of the parsed File
    * @param	filepath	of the file to be modified    
    * @param 	includePaths	include paths to be scanned with the source file
    * @throws CoreException
    */
    public IASTTranslationUnit parse(String FilePath, String[] includePaths) throws CoreException {
    	
      logger.finest("########################### Parser File: "+ FilePath + "########################");
      // Create FileParser
  	  FileContent fileContent = FileContent.createForExternalFileLocation(FilePath);
  	  //FileContent reader = FileContent.create(fileInfo.getPath(), sourceCode.toCharArray());
  	  
      Map definedSymbols = new HashMap();
      // create Scanner Object with IncludePaths
      IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
      IParserLogService log = new DefaultLogService();
      IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();
      
      ILanguage language;
 	  if((FilePath.contains(".cpp")) || (FilePath.contains(".hpp"))) {
 	     // Parse cpp/hpp files
 		 language = GPPLanguage.getDefault();
 	  } else {
 	     // Parse c/h files
 		 language = GCCLanguage.getDefault();
 	  }
 	  
 	  // Start Parsing
 	  logger.finest("  getting ast translation unit");
      int options = ILanguage.OPTION_IS_SOURCE_UNIT;
      IIndex index = null;
      IASTTranslationUnit translationUnit = language
              .getASTTranslationUnit(fileContent, info, emptyIncludes,
                      index, options, log); 	  
 	  return translationUnit;
    }
    
    /**
    * Parses the source code, return the AST.
    * Call the parse () internally.
    * If you fail to Perth return a null.
    * @return Of the entire source code AST or null,.
    */
    public IASTTranslationUnit parseOrNull(String FilePath, String[] includePaths) {
       try {
           IASTTranslationUnit tu = parse(FilePath, includePaths);
           return tu;
       } catch (CoreException e) {
			e.printStackTrace();    	   
           return null;
       }
   }    

}
