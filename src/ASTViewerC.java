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
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.core.runtime.CoreException;

import treeview.JTreeTable;
import treeview.ast.ASTTreeModel;

public class ASTViewerC{
    public static void main( String[] args ) throws CoreException{
        new ASTViewerC(args);
    }

    public ASTViewerC(String[] args ) throws CoreException {
        JFrame frame = new JFrame("ASTViewer");

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
	   	 if((FilePath.contains(".cpp")) || (FilePath.contains(".hpp"))) {
	   	     // Parse cpp/hpp files
	   		 translationUnit = GPPLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, opts, log);
	   	 } else {
	   	     // Parse c/h files
	   	     translationUnit = GCCLanguage.getDefault().getASTTranslationUnit(fileContent, info, emptyIncludes, null, opts, log);
	   	 }
	   	 
	   	 // Exit if file is empty
	   	 if(translationUnit == null) {
	   		 System.err.println(" Error while Parsing File");
	   		 return;
	   	 }        

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
