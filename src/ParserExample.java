
/* Location:           /media/Dados/Codigos/C_Plus/Projetos/eclipse-cdt-standalone-astparser/bin/
 * Qualified Name:     ParserExample
 * JD-Core Version:    0.6.0
 */

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.DOMException;
import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.core.dom.ast.IASTAttribute;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFileLocation;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit.IDependencyTree;
import org.eclipse.cdt.core.dom.ast.IASTTypeId;
import org.eclipse.cdt.core.dom.ast.IScope;
import org.eclipse.cdt.core.dom.ast.*;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTDecltypeSpecifier;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTFunctionDeclarator;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTVisibilityLabel;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.cdt.core.parser.ParserLanguage;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDefinition;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTTranslationUnit;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTFunctionDefinition;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTName;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;

import java.util.logging.Logger;
import logger.MyLogger;

public class ParserExample
{
	
  // use the classname for the logger, this way you can refactor
  private final static Logger logger = MyLogger.getLogger();
	  
  public static void main(String[] args)
    throws Exception
  {
	 // Initialse Map
	  HashMap<Integer,Object> MockFunctionList = new HashMap();
	  
	 //Initialize Logger 
	 MyLogger.setup();	  
     //FileContent fileContent = FileContent.createForExternalFileLocation("/media/Dados/Codigos/Java/Projetos/OpenDevice/opendevice-hardware-libraries/arduino/OpenDevice/DeviceConnection.h");
	  String FilePath;
	  // Check that args is passed
	  if(args.length > 0) {
		  FilePath = args[0];
	  } else {
		  //FilePath = "./docs/SquareRoot.c";
		  FilePath = "./docs/sample001/l2s_cop_lib.c";
	  }
	  FileContent fileContent = FileContent.createForExternalFileLocation(FilePath);
	  
	  
     Map definedSymbols = new HashMap();
     String[] includePaths = new String[1];
     includePaths[0]="./docs/sample001/";
     IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
     IParserLogService log = new DefaultLogService();

     IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();

     int opts = 8;
     IASTTranslationUnit translationUnit = null;
     try {
    	 if((FilePath.contains(".cpp")) || (FilePath.contains(".hpp"))) {
    	     // Parse cpp/hpp files
    		 translationUnit = GPPLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, opts, log);
    	 } else {
    	     // Parse c/h files
    	     translationUnit = GCCLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, opts, log);
    	 }
     } catch (CoreException e) {
		 logger.severe(e.toString());
		 return;
     }
     


	 // Exit if file is empty
	 if(translationUnit == null) {
		 logger.warning(" Error while Parsing File");
		 return;
	 }

	 /**
     logger.info("-----------------------------------------------------");
     logger.info("--------------------getIncludeDirectives--------------------------------");
     IASTPreprocessorIncludeStatement[] includes = translationUnit.getIncludeDirectives();
     for (IASTPreprocessorIncludeStatement include : includes) {
       logger.info("include - " + include.getName());
    }
    
     logger.info("-----------------------------------------------------");
     logger.info("--------------------getDependencyTree--------------------------------");     
     IDependencyTree dependencies = translationUnit.getDependencyTree();
     logger.info("dependencies - " + dependencies.toString());    

     **/
	 
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
         logger.info("declaration - " + declaration.getRawSignature());
         // Push Information to Map
         MockFunction.put("declaration", declaration.getRawSignature());
         MockFunction.put("FileLocation", declaration.getFileLocation().getFileName());
         MockFunction.put("Offset", String.valueOf(declaration.getFileLocation().getNodeOffset()));
         MockFunction.put("Length", String.valueOf(declaration.getFileLocation().getNodeLength()));
         MockFunction.put("StartingLine", String.valueOf(declaration.getFileLocation().getStartingLineNumber()));
         MockFunction.put("EndingLine", String.valueOf(declaration.getFileLocation().getEndingLineNumber()));
         
	   logger.info("declaration: " + declaration + " ->  " + declaration.getRawSignature());
	   logger.info("-- Parent: " + declaration.getParent().getClass().getSimpleName());
	   logger.info("-- FileLocation: " + declaration.getFileLocation().asFileLocation());
	   logger.info("-- -- Offset: " + declaration.getFileLocation().getNodeOffset());
	   logger.info("-- -- Length: " + declaration.getFileLocation().getNodeLength());
	   logger.info("-- -- StartingLine: " + declaration.getFileLocation().getStartingLineNumber());
	   logger.info("-- -- EndingLine: " + declaration.getFileLocation().getEndingLineNumber());
	   
	   for(IASTNode node : declaration.getChildren()) {
		   if(node instanceof CASTFunctionDeclarator) {
			   for(IASTNode subnode : node.getChildren()) {
				   if(subnode instanceof CASTName) {
					   logger.info("-- CASTName: " + subnode.getRawSignature());
		   // Push 
		   FunctionCASTName = subnode.getRawSignature();
		   MockFunction.put("CASTName", subnode.getRawSignature());        				   
				   }
			   }
		   }
	   }

            }           
      }     
     
     //printTree(translationUnit, 1);

	 
     logger.info("-----------------------------------------------------");
     logger.info("-----------------------------------------------------");
     logger.info("-----------------------------------------------------");

     ASTVisitor visitor = new ASTVisitor()
    {
      public int visit(IASTName name)
      {
    	  if ((name.getParent() instanceof CPPASTFunctionDeclarator) || (name.getParent() instanceof CASTFunctionDeclarator)) {    	  
            logger.info("-- IASTName: " + name.getClass().getSimpleName() + "(" + name.getRawSignature() + ")");
            logger.info("-- isVisible: " + ParserExample.isVisible(name));
        }
        return 3;
      }

      public int visit(IASTDeclaration declaration)
      {

         if ((declaration instanceof IASTSimpleDeclaration)) {
           /**
           IASTSimpleDeclaration ast = (IASTSimpleDeclaration)declaration;
          try
          {
             logger.info("--- type: " + ast.getSyntax() + " (childs: " + ast.getChildren().length + ")");
             IASTNode typedef = ast.getChildren().length == 1 ? ast.getChildren()[0] : ast.getChildren()[1];
             logger.info("------- typedef: " + typedef);
             IASTNode[] children = typedef.getChildren();
             if ((children != null) && (children.length > 0))
               logger.info("------- typedef-name: " + children[0].getRawSignature());
          }
          catch (ExpansionOverlapsBoundaryException e)
          {
             e.printStackTrace();
             logger.severe(e.toString());
          }

           IASTDeclarator[] declarators = ast.getDeclarators();
           for (IASTDeclarator iastDeclarator : declarators) {
             logger.info("iastDeclarator > " + iastDeclarator.getName());
          }

           IASTAttribute[] attributes = ast.getAttributes();
           for (IASTAttribute iastAttribute : attributes) {
             logger.info("iastAttribute > " + iastAttribute);
          }
          **/
        }

         if ((declaration instanceof IASTFunctionDefinition)) {
		   logger.info("-----------------------------------------------------");
		   logger.info("-----------------------------------------------------");
		   logger.info("-----------------------------------------------------");    	  
	       logger.info("declaration: " + declaration + " ->  " + declaration.getRawSignature());
           logger.info("-- Parent: " + declaration.getParent().getClass().getSimpleName());
           logger.info("-- FileLocation: " + declaration.getFileLocation());
           logger.info("-- -- Offset: " + declaration.getFileLocation().getNodeOffset());
           logger.info("-- -- Length: " + declaration.getFileLocation().getNodeLength());
           logger.info("-- -- StartingLine: " + declaration.getFileLocation().getStartingLineNumber());
           logger.info("-- -- EndingLine: " + declaration.getFileLocation().getEndingLineNumber());
           
           for(IASTNode node : declaration.getChildren()) {
        	   if(node instanceof CASTFunctionDeclarator) {
        		   for(IASTNode subnode : node.getChildren()) {
        			   if(subnode instanceof CASTName) {
        				   logger.info("-- CASTName: " + subnode.getRawSignature());        				   
        			   }
        		   }
        	   }
           }
           IASTFunctionDefinition ast = (IASTFunctionDefinition)declaration;
           IScope scope = ast.getScope();
          /**
          try
          {
             logger.info("### function() - Parent = " + scope.getParent().getScopeName());
             logger.info("### function() - Syntax = " + ast.getSyntax());
          }
          catch (DOMException e) {
             e.printStackTrace();
          } catch (ExpansionOverlapsBoundaryException e) {
             e.printStackTrace();
          }
           //ICPPASTFunctionDeclarator typedef = (ICPPASTFunctionDeclarator)ast.getDeclarator();
           logger.info("------- typedef: " + ast.getDeclarator().getName());
           **/
        }

         return 3;
      }

      public int visit(IASTTypeId typeId)
      {
         logger.info("--typeId: " + typeId.getRawSignature());
         return 3;
      }

      public int visit(IASTStatement statement)
      {
         logger.info("--statement: " + statement.getRawSignature());
         return 3;
      }

      public int visit(IASTAttribute attribute)
      {
    	 logger.info("--attribute: " + attribute.getRawSignature());
         return 3;
      }
  	
      public int visit(ICPPASTDecltypeSpecifier decltypeSpecifier)
      {
    	 logger.info("--decltypeSpecifier: " + decltypeSpecifier.getRawSignature());
         return 3;
      }
      
      public int visit(IASTDeclarator declarator) 
      {
		  if(declarator instanceof CASTFunctionDeclarator) {
			  logger.info("--declarator: " + declarator.getRawSignature());
		  }
         return 3;    	  
      }
      
      public int visit(IASTToken token)
      {
    	 logger.info("--token: " + token.getRawSignature());
         return 3;
      }

      public int visit(IASTParameterDeclaration parameterDeclaration)
      {
    	 logger.info("--parameterDeclaration: " + parameterDeclaration.getRawSignature());
         return 3;
      }      
                    
      
    };
     visitor.shouldVisitNames = true;
     
     visitor.shouldVisitDeclarations = true;
     visitor.shouldVisitDeclarators = false;
     visitor.shouldVisitDeclSpecifiers = false;
     visitor.shouldVisitTokens = false;
     
     visitor.shouldVisitAttributes = false;
     visitor.shouldVisitStatements = false;
     visitor.shouldVisitTypeIds = false;
     visitor.shouldVisitParameterDeclarations = false;

     translationUnit.accept(visitor);
  }

  
  private static void printTree(IASTNode node, int index) {
     IASTNode[] children = node.getChildren();

     boolean printContents = true;

     if ((node instanceof CPPASTTranslationUnit)) {
       printContents = false;
    }
     if ((node instanceof CASTFunctionDeclarator)) {
         printContents = false;
      }     
     

     String offset = "";
    try {
       offset = node.getSyntax() != null ? " (offset: " + node.getFileLocation().getNodeOffset() + "," + node.getFileLocation().getNodeLength() + ")" : "";
       printContents = node.getFileLocation().getNodeLength() < 30;
    } catch (ExpansionOverlapsBoundaryException e) {
       e.printStackTrace();
    } catch (UnsupportedOperationException e) {
       offset = "UnsupportedOperationException";
       logger.severe(offset + "  " + e.toString());
    }

     logger.info(String.format(new StringBuilder("%1$").append(index * 2).append("s").toString(), new Object[] { "-" }) + node.getClass().getSimpleName() + offset + " -> " + (printContents ? node.getRawSignature().replaceAll("\n", " \\ ") : node.getRawSignature().subSequence(0, 5)));

     for (IASTNode iastNode : children)
       printTree(iastNode, index + 1);
  }

  public static boolean isVisible(IASTNode current)
  {
     IASTNode declator = current.getParent().getParent();
     IASTNode[] children = declator.getChildren();

     for (IASTNode iastNode : children) {
       if ((iastNode instanceof ICPPASTVisibilityLabel)) {
         return 1 == ((ICPPASTVisibilityLabel)iastNode).getVisibility();
      }
    }

     return false;
  }
}

