/*
 * Created on May 27, 2005
 *
 */
package org.apache.maven.plugin.cobertura;


import java.util.*;
import org.codehaus.plexus.util.cli.StreamConsumer;

/**
 * @author wigwal
 *
 */
public class StreamConsumerToList implements StreamConsumer {
    
    // private data
    private List lines = new ArrayList();
    
    /**
     * Store the passed-in line.
     * 
     * @param line  the line to store.
     */
    public synchronized void consumeLine(String line)
    {
        lines.add(line);
    }

    /**
     * Return the list of lines we consumed earlier.
     * 
     * @return  a list with all the lines consumed since
     *          the object was created or the list was
     *          last cleared.
     */
    public List getLines() {
        return lines;
    }
    
    public String getLinesAsString() {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < lines.size(); i++) {
            buf.append(lines.get(i));
        }
        
        return buf.toString();
    }
    
    /**
     * Clear the list of consumed objects by creating an new
     * list.  Note that any old lists returned by the getLines()
     * method will not be changed.
     *
     */
    public void clear() {
        lines = new ArrayList();
    }
}
