package com.cast.parser;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.eclipse.cdt.core.dom.ast.IASTPreprocessorIncludeStatement;
import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.cpp.GPPLanguage;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.core.runtime.CoreException;

import treeview.JTreeTable;
import treeview.ast.ASTTreeModel;

/**
 * C language ASTViewer.
 * @author Ricardo JL Rufino
 * {@link: https://github.com/ricardojlrufino/eclipse-cdt-standalone-astparser} 
 */

public class ASTViewer{
    public static void main( String[] args ) throws CoreException{
        String[] includePaths = new String[1];
        includePaths[0]="./docs/sample001/";     	
        new ASTViewer("./docs/DeviceManager.h", includePaths);
    }

    public ASTViewer(String FileLocation,String[] includePaths) throws CoreException {
    	
        JFrame frame = new JFrame("ASTViewer");
        FileContent fileContent = FileContent.createForExternalFileLocation(FileLocation);
        Map definedSymbols = new HashMap();
       
        IScannerInfo info = new ScannerInfo(definedSymbols, includePaths);
        IParserLogService log = new DefaultLogService();

        IncludeFileContentProvider emptyIncludes = IncludeFileContentProvider.getEmptyFilesProvider();

        int opts = 8;
        IASTTranslationUnit translationUnit = GPPLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, opts, log);

        IASTPreprocessorIncludeStatement[] includes = translationUnit.getIncludeDirectives();
        for (IASTPreprocessorIncludeStatement include : includes) {
            System.out.println("include - " + include.getName());
        }

        JTreeTable treeTable = new JTreeTable(new ASTTreeModel(translationUnit));

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing( WindowEvent we ) {
                System.exit(0);
            }
        });
        JScrollPane scrollPane = new JScrollPane(treeTable);
        frame.getContentPane().add(scrollPane);
        frame.pack();
        frame.show();
    }
}
