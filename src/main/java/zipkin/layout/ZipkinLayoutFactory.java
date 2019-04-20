/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package zipkin.layout;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.springframework.boot.loader.tools.CustomLoaderLayout;
import org.springframework.boot.loader.tools.Layout;
import org.springframework.boot.loader.tools.LayoutFactory;
import org.springframework.boot.loader.tools.LibraryScope;
import org.springframework.boot.loader.tools.LoaderClassesWriter;

public class ZipkinLayoutFactory implements LayoutFactory, CustomLoaderLayout {

  private static final Set<LibraryScope> LIB_DESTINATION_SCOPES = new HashSet<LibraryScope>(
      Arrays.asList(
          LibraryScope.CUSTOM)
  );
  // Name of the layout and the same has to be specified at the client side where the layout is used
  private String name = "zipkin";

  public ZipkinLayoutFactory() {
  }

  public ZipkinLayoutFactory(String name) {
    this.name = name;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public Layout getLayout(File file) {
    return new Layout() {

      // Since the layout is currently used only for modules, there is no launcher provided at the moment
      @Override
      public String getLauncherClassName() {
        return null;
      }

      // If the scope of the library is CUSTOM, then the libs will be repackaged to "libs/" directory
      @Override
      public String getLibraryDestination(String libraryName, LibraryScope scope) {
          return "lib/";
      }

      @Override
      public String getClassesLocation() {
        return null;
      }

      // Marking the jar as non executable
      @Override
      public boolean isExecutable() {
        return false;
      }
    };
  }

  @Override
  public void writeLoadedClasses(LoaderClassesWriter writer) throws IOException {
    writer.writeEntry(this.name, new ByteArrayInputStream(new byte[0]));
  }
}
