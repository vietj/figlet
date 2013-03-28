/*
 * Copyright (C) 2012 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package figlet;

import junit.framework.AssertionFailedError;
import org.junit.Test;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;

public class TestFiglet {

  /** . */
  static final FigletFont font;

  static {
    try {
      URL url = FigletFont.class.getResource("3x5.flf");
      font = new FigletFont(url);
    }
    catch (IOException e) {
      AssertionFailedError afe = new AssertionFailedError();
      afe.initCause(e);
      throw afe;
    }
  }

  @Test
  public void testSimple() {
    String s = font.convert("A", false, true, 50);
    System.out.println("s = " + s);
  }

/*
  @Test
  public void test() throws Exception {
    URL url = FigletApplet.class.getResource("univers.flf");
    FigletFont font = new FigletFont(url);
    String s = font.convert("CRaSH is back at Devoxx!!!! Wedenesday 17:40 room Miles Davis C....", false, true, 1000);
    File f = new File("text.txt");
    FileWriter writer = new FileWriter(f);
    writer.append(s);
    writer.close();
  }
*/

  public void test() throws Exception {
    print("roman.flf");
    print("C64-fonts/4x4_offr.flf");
    print("bdffonts/5x7.flf");
  }

  private void print(String name) throws Exception {
    URL url = FigletFont.class.getResource(name);
    File f = new File(url.toURI());
    File folder = f.getParentFile();
    File[] files = folder.listFiles(new FilenameFilter() {
      public boolean accept(File dir, String name) {
        return name.endsWith(".flf");
      }
    });
    for (File file : files) {
      try {
        FigletFont font = new FigletFont(file.toURI().toURL());
        System.out.println("------------------------------------------------------------");
        System.out.println(file.getName());
        System.out.println(file.getAbsolutePath());
        System.out.println();
        String s = font.convert("DEVOXX", false, false, 40);
        System.out.println(s);
      }
      catch (Exception e) {
        // Skip
      }
    }
  }
}
