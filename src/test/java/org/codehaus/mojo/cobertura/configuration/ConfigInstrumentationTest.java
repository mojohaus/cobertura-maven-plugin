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
package org.codehaus.mojo.cobertura.configuration;

import junit.framework.TestCase;

/**
 * Test class for {@link ConfigInstrumentation}.
 *
 * @author Trampas Kirk
 */
public class ConfigInstrumentationTest
    extends TestCase
{
    private ConfigInstrumentation configInstrumentation;

    protected void setUp()
        throws Exception
    {
        configInstrumentation = new ConfigInstrumentation();
    }

    public void testMaxmem()
    {
        final String expectedValue128m = "128m";

        configInstrumentation.setMaxmem( expectedValue128m );

        assertEquals( expectedValue128m, configInstrumentation.getMaxmem() );

    }
    public void testMaxPermgen()
    {
        final String expectedValue128m = "128m";

        configInstrumentation.setMaxPermgen( expectedValue128m );

        assertEquals( expectedValue128m, configInstrumentation.getMaxPermgen() );

    }
}
