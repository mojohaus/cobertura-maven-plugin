/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

f1 = new File(basedir, 'target/org.codehaus.mojo.cobertura.its.Test1-pid');
assert f1.exists();
f2 = new File(basedir, 'target/org.codehaus.mojo.cobertura.its.Test2-pid');
assert f2.exists();
f3 = new File(basedir, 'target/org.codehaus.mojo.cobertura.its.Test3-pid');
assert f3.exists();
assert f1.getText() != f2.getText();
assert f1.getText() != f3.getText();
assert f2.getText() != f3.getText();



