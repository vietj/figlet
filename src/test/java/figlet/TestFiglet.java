package figlet;

import org.junit.Test;

import java.net.URL;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 */
public class TestFiglet {

  @Test
  public void test() throws Exception {

    URL url = FigletApplet.class.getResource("3-d.flf");
    FigletFont font = new FigletFont(url);
    String s = font.convert("HELLO", false, true, 10);
    System.out.println(s);

  }

}
