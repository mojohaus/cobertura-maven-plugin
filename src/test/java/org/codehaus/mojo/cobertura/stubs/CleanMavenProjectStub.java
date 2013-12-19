/*
 * #%L
 * Mojo's Maven plugin for Cobertura
 * %%
 * Copyright (C) 2005 - 2013 Codehaus
 * %%
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
 * #L%
 */
package org.codehaus.mojo.cobertura.stubs;

import org.codehaus.plexus.PlexusTestCase;
import org.codehaus.plexus.util.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Edwin Punzalan
 */
public class CleanMavenProjectStub
    extends InstrumentMavenProjectStub
{
    public CleanMavenProjectStub()
        throws IOException
    {
        File targetFile = new File( PlexusTestCase.getBasedir() + "/target/test-harness/clean/cobertura.ser" );

        File serFile = new File( PlexusTestCase.getBasedir() + "/src/test/sources/clean.ser" );

        FileUtils.copyFile( serFile, targetFile );
    }
}
