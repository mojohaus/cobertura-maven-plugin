package org.codehaus.mojo.cobertura.stubs;

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
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Build;
import org.apache.maven.plugin.testing.stubs.MavenProjectStub;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusTestCase;

/**
 * @author Fredrik Vraalsen
 * @author Edwin Punzalan
 */
public class ReportEmptySrcMavenProjectStub
    extends MavenProjectStub
{
    private Build build;

    public ReportEmptySrcMavenProjectStub()
        throws IOException
    {
        File srcDir = new File( PlexusTestCase.getBasedir() + "/target/test-harness/report-empty-src/src/main/java" );

        srcDir.mkdirs();
    }

    public MavenProject getExecutionProject()
    {
        return this;
    }

    public Build getBuild()
    {
        if ( build == null )
        {
            build = new Build();

            build.setDirectory( PlexusTestCase.getBasedir() + "/target/test-harness/report-empty-src" );
            build.setOutputDirectory( PlexusTestCase.getBasedir() + "/target/test-harness/instrument/classes" );
        }

        return build;
    }

    public List getCompileSourceRoots()
    {
        return Collections.singletonList( PlexusTestCase.getBasedir() + "/src/test/sources" );
    }
}
