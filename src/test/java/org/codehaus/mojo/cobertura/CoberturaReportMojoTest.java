package org.codehaus.mojo.cobertura;

/*
 * Copyright 2001-2006 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

import java.io.File;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.reporting.MavenReport;
import org.codehaus.plexus.PlexusTestCase;

/**
 * @author Edwin Punzalan
 */
public class CoberturaReportMojoTest
    extends AbstractCoberturaTestCase
{
    public void testReport()
        throws Exception
    {
        Mojo mojo =
            lookupMojo( "cobertura", PlexusTestCase.getBasedir() + "/src/test/plugin-configs/report-plugin-config.xml" );

        setMojoPluginClasspath( mojo );

        MavenReport reportMojo = (MavenReport) mojo;

        assertTrue( "Should be able to generate a report", reportMojo.canGenerateReport() );

        assertTrue( "Should be an externale report", reportMojo.isExternalReport() );

        mojo.execute();

        File outputHtml = new File( reportMojo.getReportOutputDirectory(), reportMojo.getOutputName() + ".html" );

        assertTrue( "Test for generated html file", outputHtml.exists() );
    }

    public void testReportEmptySourceDir()
        throws Exception
    {
        Mojo mojo =
            lookupMojo( "cobertura", PlexusTestCase.getBasedir() +
                "/src/test/plugin-configs/report-empty-src-plugin-config.xml" );

        setMojoPluginClasspath( mojo );

        MavenReport reportMojo = (MavenReport) mojo;

        assertFalse( "Should not be able to generate a report", reportMojo.canGenerateReport() );
    }

    private void setMojoPluginClasspath( Mojo mojo )
        throws Exception
    {
        setVariableValueToObject( mojo, "pluginClasspathList", getPluginClasspath() );
    }
}
