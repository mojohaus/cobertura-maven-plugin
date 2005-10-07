package org.apache.maven.plugin.cobertura;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.codehaus.plexus.util.FileUtils;

/**
 * @author <a href="will.gwaltney@sas.com">Will Gwaltney</a>
 * 
 * Utility methods for the Maven Cobertura plugin.
 */
public class Utils {

    /**
     * Return a classpath string that's expanded all entries 
     * with ** and * in them to individual directories 
     * and/or files.
     * 
     * @param cp    the unexpanded classpath.
     * @return  the expanded classpath
     */
    public static final String expandClasspath(String cp) throws IOException {
        // TODO: make the classpath element separator OS-independent
        String pathSeparator = ";";
        StringBuffer retVal = new StringBuffer();
        List expandedElements = null;
        String baseDir = null;
        String relPath = null;
        
        // for each item on the classpath
        StringTokenizer tok = new StringTokenizer(cp, pathSeparator);
        String curElement = null;
        while (tok.hasMoreElements()) {
            // run it through the DirectoryScanner
            curElement = (String) tok.nextToken();
            // use the highest directory in the path
            // as the base directory.
            int index;
            index = curElement.indexOf("/");
            if (index == -1) {
                index = curElement.indexOf("\\");
            }
            if (index == -1){
                // just the file name, no path
                baseDir = ".";
                relPath = curElement;
            } else {
                baseDir = curElement.substring(0, index + 1);
                relPath = curElement.substring(index + 1);
            }
            
            expandedElements = FileUtils.getFileNames(  new File(baseDir),
                                                        relPath,
                                                        null,
                                                        true);

            // concatenate the results onto the classpath
            // to return
            if (retVal.length() != 0) {
                retVal.append(pathSeparator);
            }
            
            for (int i = 0; i < expandedElements.size(); i++) {
                retVal.append(expandedElements.get(i));
                
                if (i < (expandedElements.size() - 1)) {
                    retVal.append(pathSeparator);
                }
            }
         }
 
        return retVal.toString();
    }

}
