package figlet;

import java.awt.*;
import java.util.*;
import java.net.*;


/**
 * FigletApplet Java
 * @author Benoit Rigaut CERN July 96
 * www.rigaut.com benoit@rigaut.com
 * released with GPL the 13th of november 2000 (my birthday!)
 */
public
class FigletApplet extends java.applet.Applet implements Runnable {
  private TextArea textArea;
  private TextField textField, textFieldCurrentFont, widthField;
  private List list;
  private Button buttonClearText, buttonClear, buttonFont, buttonConvert,
                 buttonWidthMore, buttonWidthLess;
  private Checkbox leftJustifyCB, centerCB, lineWrapCB, wordWrapCB;
  
  private FigletFont figletFontList[];
  private Vector fontList;
  private Thread fontLoading;

  private boolean splitLineAtWord = false;
  private boolean leftJustify = true;
  private int splitLineWidth = 70;
  

  public String getAppletInfo() {
    return "Figlet Java Powered Copyright Benoit Rigaut Juillet 1996";
  }

  public void init() {
    /* font path list */
    fontList = new Vector();
    String path;
    int i = 0;
    while ((path = getParameter("Font" + i)) != null) {
      fontList.addElement(path);
      i++;
    }
    figletFontList = new FigletFont[i];

    /* awt initialisation */
    GridBagLayout gridBagLayout = new GridBagLayout();
    GridBagConstraints gridBagConstraints = new GridBagConstraints();
    setLayout(gridBagLayout);
    setFont(new Font("Courier", Font.PLAIN, 14));
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;

    Panel textPanel = new Panel();
    gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
    gridBagLayout.setConstraints(textPanel, gridBagConstraints);
    add(textPanel);

    Panel fontPanel = new Panel();
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayout.setConstraints(fontPanel, gridBagConstraints);
    add(fontPanel);

    Panel resultPanel = new Panel();
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayout.setConstraints(resultPanel, gridBagConstraints);
    add(resultPanel);

    
    /* Result Panel */

    GridBagLayout gridBagLayoutResult = new GridBagLayout();
    gridBagConstraints = new GridBagConstraints();
    resultPanel.setLayout(gridBagLayoutResult);
    resultPanel.setFont(new Font("Courier", Font.PLAIN, 14));
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;

    textArea = new TextArea(15, 80);
    textArea.setEditable(false);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayoutResult.setConstraints(textArea, gridBagConstraints);
    resultPanel.add(textArea);

    buttonClear = new Button("Clear the output");
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayoutResult.setConstraints(buttonClear, gridBagConstraints);
    resultPanel.add(buttonClear);


    /* Text Panel */

    GridBagLayout gridBagLayoutText = new GridBagLayout();
    gridBagConstraints = new GridBagConstraints();
    textPanel.setLayout(gridBagLayoutText);
    textPanel.setFont(new Font("Courier", Font.PLAIN, 14));
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;

    Label label = new Label("Figlet Java", Label.CENTER);
    label.setFont(new Font("Times", Font.PLAIN, 24));
    gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
    gridBagLayoutText.setConstraints(label, gridBagConstraints);
    textPanel.add(label);

    label = new Label("(� Beno�t Rigaut, Juillet 1996)", Label.CENTER);
    label.setFont(new Font("Times", Font.PLAIN, 10));
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayoutText.setConstraints(label, gridBagConstraints);
    textPanel.add(label);

    label = new Label("The text to be converted:");
    gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
    gridBagLayoutText.setConstraints(label, gridBagConstraints);
    textPanel.add(label);

    buttonClearText = new Button("Clear the input");
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayoutText.setConstraints(buttonClearText, gridBagConstraints);
    textPanel.add(buttonClearText);

    textField = new TextField(30);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayoutText.setConstraints(textField, gridBagConstraints);
    textPanel.add(textField);
    
    label = new Label("Text Folding:");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 2.0;  
    //gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
    gridBagLayoutText.setConstraints(label, gridBagConstraints);
    textPanel.add(label);

    CheckboxGroup styleCBG = new CheckboxGroup();
    
    lineWrapCB = new Checkbox("Line Wrap", styleCBG, true);
    gridBagConstraints.weightx = 1.0;  
    //gridBagConstraints.gridwidth = GridBagConstraints.RELATIVE;
    gridBagLayoutText.setConstraints(lineWrapCB, gridBagConstraints);
    textPanel.add(lineWrapCB);

    wordWrapCB = new Checkbox("Word Wrap", styleCBG, false);
    gridBagConstraints.weightx = 1.0;  
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayoutText.setConstraints(wordWrapCB, gridBagConstraints);
    textPanel.add(wordWrapCB);


    label = new Label("Text Justication:");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = GridBagConstraints.BOTH;  
    gridBagConstraints.weightx = 2.0;  
    gridBagLayoutText.setConstraints(label, gridBagConstraints);
    textPanel.add(label);
    
    CheckboxGroup displayCBG = new CheckboxGroup();
    
    leftJustifyCB = new Checkbox("Left", displayCBG, true);
    gridBagConstraints.weightx = 1.0;  
    gridBagLayoutText.setConstraints(leftJustifyCB, gridBagConstraints);
    textPanel.add(leftJustifyCB);
        
    centerCB = new Checkbox("Center", displayCBG, false);
    gridBagConstraints.weightx = 0.0;  
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayoutText.setConstraints(centerCB, gridBagConstraints);
    textPanel.add(centerCB);
    
    label = new Label("Maximum Line Width:");
    gridBagConstraints = new GridBagConstraints();
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;
    gridBagLayoutText.setConstraints(label, gridBagConstraints);
    textPanel.add(label);
     
    buttonWidthLess = new Button("-");
    gridBagConstraints.weightx = 2.0;  
    gridBagLayoutText.setConstraints(buttonWidthLess, gridBagConstraints);
    textPanel.add(buttonWidthLess);
    
    buttonWidthMore = new Button("+");
    gridBagConstraints.weightx = 2.0;  
    gridBagLayoutText.setConstraints(buttonWidthMore, gridBagConstraints);
    textPanel.add(buttonWidthMore);
    
    widthField = new TextField(new Integer(splitLineWidth).toString(), 2);
    widthField.setEditable(false);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagConstraints.weightx = 1.0;  
    gridBagLayoutText.setConstraints(widthField, gridBagConstraints);
    textPanel.add(widthField);
        
    buttonConvert = new Button("Convert");
    buttonConvert.disable();
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayoutText.setConstraints(buttonConvert, gridBagConstraints);
    textPanel.add(buttonConvert);

    /* Font Panel */

    GridBagLayout gridBagLayoutFont = new GridBagLayout();
    gridBagConstraints = new GridBagConstraints();
    fontPanel.setLayout(gridBagLayoutFont);
    fontPanel.setFont(new Font("Courier", Font.BOLD, 14));
    gridBagConstraints.fill = GridBagConstraints.BOTH;
    gridBagConstraints.weightx = 1.0;

    list = new List(6, false);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayoutFont.setConstraints(list, gridBagConstraints);
    fontPanel.add(list);
    for (int j = 0; j < i; j++) {
      String s = (String) fontList.elementAt(j);
      list.addItem(s.substring(s.lastIndexOf("/")+1, s.lastIndexOf(".flf")));
    }

    textFieldCurrentFont = new TextField("[None]");
    textFieldCurrentFont.setEditable(false);
    gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
    gridBagLayoutFont.setConstraints(textFieldCurrentFont, gridBagConstraints);
    fontPanel.add(textFieldCurrentFont);

  
  }
           


  /**
   * a thread as to be handled to load the font
   * (because it may be long)
   */ 
  public void run() {
    if (Thread.currentThread() == fontLoading) {
      textFieldCurrentFont.setText("Loading...");
      int s = list.getSelectedIndex();
      String path = (String)fontList.elementAt(s);
      System.out.println("Figlet loading " + path);
      String fontName = path.substring(0, path.indexOf(".flf"));
      fontName = fontName.substring(fontName.lastIndexOf("/")+1);
      URL theURL = null;
      try
        theURL = new URL(getCodeBase(), path);
      catch (MalformedURLException e)
        System.out.println("Bad URL: " + theURL +
                           " ERROR: " + e.getMessage());
      if (theURL != null)
        figletFontList[s] = new FigletFont(theURL);
      //String name = figletFontList[s].fontName;
      textFieldCurrentFont.setText(fontName);
      buttonConvert.enable();
      list.enable();
   }
  }
  /**
   * action() mainly button actions
   */
  public boolean action(Event evt, Object arg) {
    /* handle the buttons */
    if (evt.target instanceof Button) {
      /* clearing the input & output text */
      if ( ((String) arg).equals("Clear the output") )
        textArea.setText("");
      else if ( ((String) arg).equals("Clear the input") )
        textField.setText("");
      /* the + & - buttons for line width */
      else if ( ((String) arg).equals("+") && (splitLineWidth < 120) ) {
        splitLineWidth++;  
        widthField.setText(new Integer(splitLineWidth).toString());
      }
      else if ( ((String) arg).equals("-") && (splitLineWidth > 10) ) {
        splitLineWidth--;
         widthField.setText(new Integer(splitLineWidth).toString());
      }
      /* asking for an output */
      else if ( ((String) arg).equals("Convert") )
        /* if needed must launch a thread to load the font */
        if (figletFontList[list.getSelectedIndex()] == null) {
          buttonConvert.disable();
          list.disable();
          fontLoading = new Thread(this);
          fontLoading.start();
        } else
          textArea.appendText('\n' + FigletASCII.
                              convert(textField.getText(), 
                                      figletFontList[list.getSelectedIndex()],
                                      splitLineAtWord,
                                      leftJustify,
                                      splitLineWidth));
    } 
    /* handle the 2 checkboxes */
    else if (evt.target instanceof Checkbox) {
      splitLineAtWord = wordWrapCB.getState();
      leftJustify = leftJustifyCB.getState();
    }
    /* handle the font list */
    else if (evt.target instanceof List) {
      int s = list.getSelectedIndex();
      if (figletFontList[s] == null) {
        buttonConvert.disable();
        list.disable();
        fontLoading = new Thread(this);
        fontLoading.start();
      } else
        textFieldCurrentFont.setText(figletFontList[s].fontName);
    }
    return true;
  }
}
    




