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
import org.codehaus.plexus.PlexusTestCase;

/**
 * @author Edwin Punzalan
 */
public class CoberturaCleanMojoTest
    extends AbstractCoberturaTestCase
{
    public void testClean()
        throws Exception
    {
        Mojo mojo =
            lookupMojo( "clean", PlexusTestCase.getBasedir() + "/src/test/plugin-configs/clean-plugin-config.xml" );

        File dataFile = (File) getVariableValueFromObject( mojo, "dataFile" );

        assertTrue( "Test if the ser file has been prepared", dataFile.exists() );

        mojo.execute();

        assertFalse( "ser file must have been deleted", dataFile.exists() );
    }
}
