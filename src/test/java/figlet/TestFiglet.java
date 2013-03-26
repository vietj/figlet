package figlet;

import org.junit.Test;

import java.net.URL;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 */
public class TestFiglet {

  @Test
  public void test() throws Exception {

    URL url = FigletApplet.class.getResource("Banner.flf");
    FigletFont font = new FigletFont(url);
    String s = font.convert("A B C D E F G H", false, false, 40);
    System.out.println(s);
  }
}
