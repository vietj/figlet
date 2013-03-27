package figlet;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 */
public class TestFiglet {

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

  @Test
  public void test() throws Exception {
    print("roman.flf");
    print("C64-fonts/4x4_offr.flf");
    print("bdffonts/5x7.flf");
  }

  private void print(String name) throws Exception {
    URL url = FigletApplet.class.getResource(name);
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
