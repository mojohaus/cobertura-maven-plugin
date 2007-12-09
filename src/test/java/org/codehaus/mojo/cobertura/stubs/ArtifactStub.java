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

import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.artifact.handler.DefaultArtifactHandler;

/**
 * @author Edwin Punzalan
 */
public class ArtifactStub
    extends org.apache.maven.plugin.testing.stubs.ArtifactStub
{
    private File artifactFile;

    private String groupId;

    private String artifactId;

    private String version;

    public void setFile( File file )
    {
        artifactFile = file;
    }

    public void setGroupId( String groupId )
    {
        this.groupId = groupId;
    }

    public String getType()
    {
        return "pom";
    }

    public String getGroupId()
    {
        return groupId;
    }

    public void setArtifactId( String artifactId )
    {
        this.artifactId = artifactId;
    }

    public String getArtifactId()
    {
        return artifactId;
    }

    public ArtifactHandler getArtifactHandler()
    {
        return new DefaultArtifactHandler()
        {
            public String getLanguage()
            {
                return "java";
            }
        };
    }

    public File getFile()
    {
        return artifactFile;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion( String version )
    {
        this.version = version;
    }
}
