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

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;

public class GitHubIssueTest {

	public static class TestBoolean {
		@SerializeAsNumber
		boolean boss;

		Boolean no;

		Integer in;

		public boolean isBoss() {
			return boss;
		}

		public void setBoss(boolean boss) {
			this.boss = boss;
		}

		public Boolean getNo() {
			return no;
		}

		public void setNo(Boolean no) {
			this.no = no;
		}

		public Integer getIn() {
			return in;
		}

		public void setIn(Integer in) {
			this.in = in;
		}

	}
	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void test() {
		GithubIssues test = new GithubIssues();
		for (Field field : TestBoolean.class.getDeclaredFields()) {
			if ("boss".equals(field.getName())) {
				assertEquals(true, test.createBoundField(field));
			} else {
				assertEquals(false, test.createBoundField(field));
			}
		}
	}

}
