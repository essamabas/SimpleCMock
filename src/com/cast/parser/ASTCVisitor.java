package com.cast.parser;

import java.util.logging.Logger;

import org.eclipse.cdt.core.dom.ast.ASTVisitor;
import org.eclipse.cdt.core.dom.ast.ExpansionOverlapsBoundaryException;
import org.eclipse.cdt.core.dom.ast.IASTAttribute;
import org.eclipse.cdt.core.dom.ast.IASTDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTDeclarator;
import org.eclipse.cdt.core.dom.ast.IASTFunctionDefinition;
import org.eclipse.cdt.core.dom.ast.IASTName;
import org.eclipse.cdt.core.dom.ast.IASTNode;
import org.eclipse.cdt.core.dom.ast.IASTParameterDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTSimpleDeclaration;
import org.eclipse.cdt.core.dom.ast.IASTStatement;
import org.eclipse.cdt.core.dom.ast.IASTToken;
import org.eclipse.cdt.core.dom.ast.IASTTypeId;
import org.eclipse.cdt.core.dom.ast.IScope;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTDecltypeSpecifier;
import org.eclipse.cdt.core.dom.ast.cpp.ICPPASTVisibilityLabel;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTFunctionDeclarator;
import org.eclipse.cdt.internal.core.dom.parser.c.CASTName;
import org.eclipse.cdt.internal.core.dom.parser.cpp.CPPASTFunctionDeclarator;

import logger.MyLogger;

/**
 * C language ASTCVisitor - extends Eclipse-dom-ASTVisitor - it is another way to parse c files  
 * @author Essam Abas
 * {@link: https://github.com/ricardojlrufino/eclipse-cdt-standalone-astparser} 
 */

public class ASTCVisitor extends ASTVisitor {

	  // use the classname for the logger, this way you can refactor
	  private final static Logger logger = MyLogger.getLogger();
	  
	  public ASTCVisitor() {
		  // TODO Auto-generated constructor stub
		  
	  };
	     
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
	     
	   public int visit(IASTName name)
	   {
	 	  if ((name.getParent() instanceof CPPASTFunctionDeclarator) || (name.getParent() instanceof CASTFunctionDeclarator)) {    	  
	         logger.info("-- IASTName: " + name.getClass().getSimpleName() + "(" + name.getRawSignature() + ")");
	         logger.info("-- isVisible: " + isVisible(name));
	     }
	     return 3;
	   };

   public int visit(IASTDeclaration declaration)
   {

      if ((declaration instanceof IASTSimpleDeclaration)) {
        
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

}
