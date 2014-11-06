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
package org.codehaus.mojo.cobertura;

/**
 * Check the coverage percentages for unit tests and integration tests from the
 * last instrumentation, and optionally fail the build if the targets are not
 * met. To fail the build you need to set
 * <code>configuration/check/haltOnFailure=true</code> in the plugin's
 * configuration.
 *
 * @author Dennis Lundberg
 * @goal check-integration-test
 * @execute phase="verify" lifecycle="cobertura"
 */
public class CoberturaCheckIntegrationTestMojo extends CoberturaCheckMojo
{
}
