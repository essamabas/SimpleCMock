
import java.util.HashMap;

import java.util.logging.Logger;
import java.util.Map;

import org.eclipse.cdt.core.dom.ast.IASTTranslationUnit;
import org.eclipse.cdt.core.dom.ast.gnu.c.GCCLanguage;
import org.eclipse.cdt.core.index.IIndex;
import org.eclipse.cdt.core.model.ILanguage;
import org.eclipse.cdt.core.parser.DefaultLogService;
import org.eclipse.cdt.core.parser.FileContent;
import org.eclipse.cdt.core.parser.IParserLogService;
import org.eclipse.cdt.core.parser.IScannerInfo;
import org.eclipse.cdt.core.parser.IncludeFileContentProvider;
import org.eclipse.cdt.core.parser.ScannerInfo;
import org.eclipse.core.runtime.CoreException;


/**
 * C language parser.
 * @author uchan
 */
public class Parser {
	
    private FileInfo fileInfo;
    private String sourceCode;

    /**
    * To generate the C language parser.
    * If you want to parse the string on the memory rather than the file, you may be given the appropriate string in the file path.
    * @param FilePath path to the file that contains the source code
    * @param SourceCode source code
    */
    public Parser(FileInfo fileInfo, String sourceCode) {
        this.fileInfo = fileInfo;
        this.sourceCode = sourceCode;
    }

    /**
    * Parses the source code, return the AST.
    * @return Of the entire source code AST
    * @throws CoreException
    */
    public IASTTranslationUnit parse() throws CoreException {
        logger.finest("Parser#parse()");

        ILanguage language = GCCLanguage.getDefault();

        FileContent reader = FileContent.create(fileInfo.getPath(), sourceCode.toCharArray());

        Map<String, String> macroDefinitions = new HashMap<String, String>();
        macroDefinitions.put("__STDC__", "100");

        String stdheaderDirPath = "";

        logger.finest("  setting include search path: " + stdheaderDirPath);

        /*
        * Path name obtained by adding a header file name in the directory specified in includeSearchPath is
        It is passed to the * MyFileContentProvider # getContentForInclusion
        */
        String[] includeSearchPath = new String[] { stdheaderDirPath };
        IScannerInfo scanInfo = new ScannerInfo(macroDefinitions, includeSearchPath);

        logger.finest("  creating include file content provider");

        IncludeFileContentProvider fileCreator =
                //IncludeFileContentProvider.getSavedFilesProvider();
                //IncludeFileContentProvider.getEmptyFilesProvider();
                //new MyFileContentProvider(stdheaderDirPath);
                new MyFileContentProvider("stdheaders", fileInfo);
        IIndex index = null;
        int options = ILanguage.OPTION_IS_SOURCE_UNIT;
        IParserLogService log = new DefaultLogService();

        logger.finest("  getting ast translation unit");

        IASTTranslationUnit translationUnit = language
                .getASTTranslationUnit(reader, scanInfo, fileCreator,
                        index, options, log);
        return translationUnit;
   }

    /**
    * Parses the source code, return the AST.
    * Call the parse () internally.
    * If you fail to Perth return a null.
    * @return Of the entire source code AST or null,.
    */
    public IASTTranslationUnit parseOrNull() {
       try {
           IASTTranslationUnit tu = parse();
           return tu;
       } catch (CoreException e) {
           return null;
       }
   }
}
