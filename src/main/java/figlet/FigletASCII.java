package figlet;

import java.util.*;

/**
 * FigletASCII Java
 * @author Benoit Rigaut CERN July 96
 * www.rigaut.com benoit@rigaut.com
 * released with GPL the 13th of november 2000 (my birthday!)
 *
 */
class FigletASCII {
  /**
   * move a banner to the right (for centering)
   */
  private static String scroll(String message, int offset) {
    String result = "";
    String shift = "";
    for (int i = 0; i < offset; i++)
      shift += ' ';
    StringTokenizer st = new StringTokenizer(message, "\n");
    while (st.hasMoreElements())
      result += shift + st.nextToken() + '\n';
    return result;
  }
  /**
   * append a new banner line (center if needed)  
   */
  private static String addLine(String text, String line,
                              boolean leftJustify, int splitWidth) {
    String result = text;
    if (leftJustify) 
      result += line; 
    else
      result += scroll(line, (int) (splitWidth/2 - width(line)/2));
    return result;
  }
  /**
   * the main part, converts from ASCII to a banner,
   * eventually centering each line,
   * folding after each word, or when the width limit is reached
   */
  public static String convert(String message, FigletFont figletFont,
                               boolean splitAtWord, boolean leftJustify,
                               int splitWidth) {
    String result = "";
    StringTokenizer st = new StringTokenizer(message, " ");
    if (splitAtWord)
      while (st.hasMoreElements()) 
        result = addLine(result, convertOneLine(st.nextToken(), figletFont),
                         leftJustify, splitWidth);
    else { 
      String line = "";
      while (st.hasMoreElements()) {
        String w = st.nextToken(), word;
        if (line == "")
          word = w;
        else
          word = ' ' + w;
        String newLine = append(line, word, figletFont);
        //System.out.println("word:" + word + "line:" + 
        //                  line + "new line:" + newLine);
        if ((width(newLine) > splitWidth) && (line != "")) {
          result = addLine(result, line + '\n',
                           leftJustify, splitWidth);
          line = append("", w, figletFont);
        } else
          line = newLine;
      }
      if (line != "")
        result = addLine(result, line + '\n',
                         leftJustify, splitWidth);
    }
    return result;
  }
  /**
   * Gimme the maximum width of a converted text
   */
  public static int width(String message) {
    int w = 0;
    StringTokenizer st = new StringTokenizer(message, "\n");
    while (st.hasMoreElements()) 
      w = Math.max(w, st.nextToken().length());
    return w;
  }
  /**
   * create a banner from a text
   */
  private static String convertOneLine(String message, FigletFont figletFont) {
    String result = "";
    for (int l = 0; l < figletFont.height; l++) { // for each line
      for (int c = 0; c < message.length(); c++) // for each char
        result += figletFont.getCharLineString((int) message.charAt(c), l);
      result += '\n';
    }
    return result;
  }       
  /**
   *  append a word to a banner
   */
  private static String append(String message, String end,
                                      FigletFont figletFont) {
    String result = "";
    int h = 0;
    if (message == "")
      for (int i = 0; i < figletFont.height; i++)
        message += " \n";
    StringTokenizer st = new StringTokenizer(message, "\n");
    while (st.hasMoreElements()) {
      result += st.nextToken();
      for (int c = 0; c < end.length(); c++) // for each char
        result += figletFont.getCharLineString((int) end.charAt(c), h);
      result += '\n';
      h++;
    }
    return result;
  }
}
    





