package org.codehaus.mojo.cobertura.configuration;

import junit.framework.TestCase;

/**
 * Test class for {@link ConfigInstrumentation}.
 * @author Trampas Kirk
 */
public class ConfigInstrumentationTest
        extends TestCase
{
    private ConfigInstrumentation configInstrumentation;

    protected void setUp() throws Exception
    {
        configInstrumentation = new ConfigInstrumentation();
    }

    public void testMaxmem()
    {
        final String expectedValue128m = "128m";

        configInstrumentation.setMaxmem( expectedValue128m );
        
        assertEquals( expectedValue128m, configInstrumentation.getMaxmem() );

    }
}
