/* Copyright 2003-2005 : Project cobertura-maven-plugin */
package org.codehaus.mojo.cobertura.configuration;

import net.sourceforge.cobertura.ant.Regex;

import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.util.TypeFormat;

public class ConfigConverter
    extends AbstractConfigurationConverter
{
    public static final String DEFAULT_BRANCH_RATE = "0";

    public static final String DEFAULT_LINE_RATE = "0";

    public boolean canConvert( Class type )
    {
        return ConfigCheck.class.isAssignableFrom( type ) || ConfigInstrumentation.class.isAssignableFrom( type );
    }

    public Object fromConfiguration( ConverterLookup converterLookup, PlexusConfiguration configuration, Class type,
                                    Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator,
                                    ConfigurationListener listener )
        throws ComponentConfigurationException
    {
        Object retValue = fromExpression( configuration, expressionEvaluator, type );
        if ( retValue != null )
        {
            return retValue;
        }

        Class implementation = getClassForImplementationHint( type, configuration, classLoader );

        retValue = instantiateObject( implementation );

        if ( retValue instanceof ConfigCheck )
        {
            processCheck( (ConfigCheck) retValue, configuration, expressionEvaluator );
        }
        else if ( retValue instanceof ConfigInstrumentation )
        {
            processInstrumentation( (ConfigInstrumentation) retValue, configuration, expressionEvaluator );
        }

        return retValue;
    }

    private void processCheck( ConfigCheck check, PlexusConfiguration configuration,
                              ExpressionEvaluator expressionEvaluator )
    {
        PlexusConfiguration children[] = configuration.getChildren();

        check.setBranchRate( configuration.getAttribute( "branchrate", DEFAULT_BRANCH_RATE ) );
        check.setLineRate( configuration.getAttribute( "linerate", DEFAULT_LINE_RATE ) );
        check.setHaltOnFailure( TypeFormat.parseBoolean( configuration.getAttribute( "haltonerror", "true" ).trim() ) );
        check.setTotalBranchRate( configuration.getAttribute( "totalbranchrate", DEFAULT_BRANCH_RATE ) );
        check.setTotalLineRate( configuration.getAttribute( "totallinerate", DEFAULT_LINE_RATE ) );

        if ( ( children != null ) && ( children.length > 0 ) )
        {
            for ( int i = 0; i < children.length; i++ )
            {
                Regex regex = check.createRegex();
                regex.setBranchRate( children[i].getAttribute( "branchrate", DEFAULT_BRANCH_RATE ) );
                regex.setLineRate( children[i].getAttribute( "linerate", DEFAULT_LINE_RATE ) );
                regex.setPattern( children[i].getAttribute( "pattern", "*" ) );
            }
        }
    }

    private void processInstrumentation( ConfigInstrumentation instrumentation, PlexusConfiguration configuration,
                                        ExpressionEvaluator expressionEvaluator )
        throws ComponentConfigurationException
    {
        PlexusConfiguration children[] = configuration.getChildren();

        if ( ( children != null ) && ( children.length > 0 ) )
        {
            for ( int i = 0; i < children.length; i++ )
            {
                String name = children[i].getName();
                
                if ( "include".equals( name ) )
                {
                    try
                    {
                        instrumentation.addInclude( children[i].getValue().toString() );
                    }
                    catch ( PlexusConfigurationException e )
                    {
                        throw new ComponentConfigurationException( "Unable to add include.", e );
                    }
                } else if ( "exclude".equals( name ) )
                {
                    try
                    {
                        instrumentation.addExclude( children[i].getValue().toString() );
                    }
                    catch ( PlexusConfigurationException e )
                    {
                        throw new ComponentConfigurationException( "Unable to add exclude.", e );
                    }
                } else if ( "ignore".equals( name ) )
                {
                    try
                    {
                        instrumentation.addIgnore( children[i].getValue().toString() );
                    }
                    catch ( PlexusConfigurationException e )
                    {
                        throw new ComponentConfigurationException( "Unable to add ignore.", e );
                    }
                }
            }
        }
    }
}
