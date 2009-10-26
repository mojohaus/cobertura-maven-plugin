package org.codehaus.mojo.cobertura.tasks;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import java.io.File;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.plexus.util.StringUtils;

/**
 * The Report Task.
 * 
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 */
public class ReportTask
    extends AbstractTask
{
    private File dataFile;

    private File outputDirectory;

    private String outputFormat;

    private String sourceEncoding;

    private List compileSourceRoots;

    /**
     * Create ReportTask.
     */
    public ReportTask()
    {
        super( "net.sourceforge.cobertura.reporting.Main" );
    }

    public void execute()
        throws MojoExecutionException
    {
        outputDirectory.mkdirs();

        for ( Iterator i = compileSourceRoots.iterator(); i.hasNext(); )
        {
            String directory = (String) i.next();
            cmdLineArgs.addArg( "--source", directory );
        }

        if ( outputDirectory != null )
        {
            cmdLineArgs.addArg( "--destination", outputDirectory.getAbsolutePath() );
        }

        if ( dataFile != null )
        {
            cmdLineArgs.addArg( "--datafile", dataFile.getAbsolutePath() );
        }

        if ( StringUtils.isNotEmpty( outputFormat ) )
        {
            cmdLineArgs.addArg( "--format", outputFormat );
        }

        if ( StringUtils.isNotEmpty( sourceEncoding ) )
        {
            cmdLineArgs.addArg( "--encoding", sourceEncoding );
        }

        int returnCode = executeJava();

        // Check the return code and print a message
        if ( returnCode == 0 )
        {
            getLog().info( "Cobertura Report generation was successful." );
        }
        else
        {
            throw new MojoExecutionException( "Unable to generate Cobertura Report for project." );
        }
    }

    /**
     * @return Returns the dataFile.
     */
    public File getDataFile()
    {
        return dataFile;
    }

    /**
     * @return Returns the outputDirectory.
     */
    public File getOutputDirectory()
    {
        return outputDirectory;
    }

    /**
     * @return Returns the outputFormat.
     */
    public String getOutputFormat()
    {
        return outputFormat;
    }

    /**
     * @return Returns the sourceEncoding.
     */
    public String getSourceEncoding()
    {
        return sourceEncoding;
    }

    /**
     * @param dataFile The dataFile to set.
     */
    public void setDataFile( File dataFile )
    {
        this.dataFile = dataFile;
    }

    /**
     * @param outputDirectory The outputDirectory to set.
     */
    public void setOutputDirectory( File outputDirectory )
    {
        this.outputDirectory = outputDirectory;
    }

    /**
     * @param outputFormat The outputFormat to set.
     */
    public void setOutputFormat( String outputFormat )
    {
        this.outputFormat = outputFormat;
    }

    /**
     * @param sourceEncoding The sourceEncoding to set.
     */
    public void setSourceEncoding( String sourceEncoding )
    {
        this.sourceEncoding = sourceEncoding;
    }

    public void setCompileSourceRoots( List compileSourceRoots )
    {
        this.compileSourceRoots = Collections.unmodifiableList( compileSourceRoots );
    }
}
