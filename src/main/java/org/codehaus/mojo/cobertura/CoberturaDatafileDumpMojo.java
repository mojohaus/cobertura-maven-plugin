package org.codehaus.mojo.cobertura;

/*
 * Copyright 2001-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import net.sourceforge.cobertura.coveragedata.CoverageDataFileHandler;
import net.sourceforge.cobertura.coveragedata.PackageData;
import net.sourceforge.cobertura.coveragedata.ProjectData;
import net.sourceforge.cobertura.util.Header;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Iterator;

/**
 * Cobertura Datafile Dump Mojo 
 *
 * @author <a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>
 * @version $Id$
 * @goal dump-datafile
 */
public class CoberturaDatafileDumpMojo
    extends AbstractCoberturaMojo
{

    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if ( ( dataFile == null ) || !dataFile.exists() )
        {
            throw new MojoExecutionException( "Unable to dump non-existance dataFile [" + dataFile + "]" );
        }

        ProjectData projectData = CoverageDataFileHandler.loadCoverageData( dataFile );
        NumberFormat percentage = NumberFormat.getPercentInstance();

        println( "<?xml version=\"1.0\"?>" );

        println( "<coverage line-rate=\"" + percentage.format(projectData.getLineCoverageRate()) 
                 + "\" branch-rate=\"" + percentage.format(projectData.getBranchCoverageRate()) 
                 + "\" version=\"" + Header.version() 
                 + "\" timestamp=\"" + new Date().getTime() + "\">" );

        Iterator it = projectData.getPackages().iterator();
        while ( it.hasNext() )
        {
            PackageData packageData = (PackageData) it.next();
            println( "<package name=\"" + packageData.getName() 
                     + "\" line-rate=\"" + percentage.format(packageData.getLineCoverageRate())
                     + "\" branch-rate=\"" + percentage.format(packageData.getBranchCoverageRate()) + "\" />" );
        }

        println( "</coverage>" );
    }

    private void println( String msg )
    {
        System.out.println( msg );
    }
}
