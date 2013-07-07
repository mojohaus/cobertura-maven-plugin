/**
 * Copyright 2013
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.cobertura;

import java.beans.Transient;
import java.lang.reflect.Field;

public class GithubIssues {
	boolean test;

	public boolean createBoundField(final Field field) {
		Class<?> primitive = boolean.class;
		if (((primitive).equals(field.getType()) || (Boolean.class).equals(field.getType())) && field.getAnnotation(SerializeAsNumber.class) != null) {
			return true;
		}
		return false;
	}

	@Transient
	public void noTestAnnoation() {
		if (test) {
			System.out.print("test");
		} else {
			System.out.print("test2");
		}
	}

	public boolean isTest() {
		return test;
	}

	public void setTest(boolean test) {
		this.test = test;
	}
}
