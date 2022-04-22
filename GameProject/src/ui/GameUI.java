package ui;

import java.awt.Dimension;
import java.util.*;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.WindowConstants;
import playground.Playground;

import java.awt.event.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * creates the game UI JFrame containing a canvas (see class {@link GamePanel})) for the levels to
 * paint the games. Has a menu for loading, saving games and an about menu item. two buttons allow
 * restarting the game or exit.
 * 
 *
 */
public class GameUI implements ActionListener {

  private static volatile int newAction = -1;

  /** the JFrame instance used in this window */
  protected JFrame frame = null;
  /** the panel holding all components, uses {@link BoxLayout} Y-Direction */
  protected JPanel panel = null;
  /**
   * the instance of {@link GamePanel} for Playgrounds to paint and refresh their level elements. It
   * is added as first component of {@link #panel}
   */
  private GamePanel canvas = null;
  /**
   * the button panel holding buttons, set on {@link #panel} at last, uses {@link BoxLayout}
   * X-Direction
   */
  protected JPanel buttonPanel = null;

  protected JMenuItem playItem;
  protected JMenuItem loadItem;
  protected JMenuItem saveItem;
  protected JMenuItem quitItem;
  protected JMenuItem aboutItem;
  protected JMenu gameMenu;
  protected JMenu helpMenu;
  protected JButton button;
  protected JButton button2;

  public static final int ACTION_NEW = 1;
  public static final int ACTION_LOAD = 2;
  public static final int ACTION_SAVE = 3;
  public static final int ACTION_RESET = 4;
  public static final int ACTION_QUIT = 5;
  public static final int ACTION_BUTTON = 6;
  public static final int ACTION_PAUSE = 6;
  public static final int ACTION_ABOUT = 7;


  private static Logger logger = LogManager.getLogger(GameUI.class);

  /**
   * as typical for GUI classes this constructor creates all the components and adds them to the
   * frame. It adds this instance as ActionListener for all components. See
   * {@link #actionPerformed(ActionEvent)} for details. It directly sets the frame visible as a
   * final step.
   * 
   * <p>
   * If you want to extend this GUI, create a subclass and add new elements in constructor. It is
   * necessary to call {@link JFrame#revalidate()} on {@link #frame} attribute after changing/adding
   * components to {@link #panel} or {@link #canvas}, because the constructor here already sets
   * visibility to true and renders the IFrame.
   * </p>
   * 
   * @param sizeX pixel dimension wanted in x direction
   * @param sizeY pixel dimension wanted in y direction
   */
  public GameUI(int sizeX, int sizeY) {

    // create a canvas on which the levels (Playgrounds) will be painted later when loaded and
    // started.
    this.canvas = new GamePanel();


    // create contentPane
    this.panel = new JPanel();
    this.panel.setLayout(new BoxLayout(this.panel, BoxLayout.Y_AXIS));


    // panel.setLayout(new FlowLayout());
    // panel.setLayout(new GridBagLayout());
    // panel.setLayout(new SpringLayout());
    this.panel.add(canvas);

    // create main window
    this.frame = new JFrame("Prog2 GameProject!");
    this.frame.setContentPane(panel);

    // Menu
    this.playItem = new JMenuItem("New Game");
    this.loadItem = new JMenuItem("Restore game");
    this.saveItem = new JMenuItem("Save game");
    this.quitItem = new JMenuItem("Exit game");

    this.playItem.addActionListener(this);
    this.loadItem.addActionListener(this);
    this.saveItem.addActionListener(this);
    this.quitItem.addActionListener(this);

    this.gameMenu = new JMenu("Game");
    this.gameMenu.add(playItem);
    this.gameMenu.add(loadItem);
    this.gameMenu.add(saveItem);
    this.gameMenu.addSeparator();
    this.gameMenu.add(quitItem);

    this.helpMenu = new JMenu("Help");
    this.aboutItem = new JMenuItem("About");
    this.helpMenu.add(this.aboutItem);
    // for extending the code change here to your own class/implementation of an ActionListener or
    // extend method this.actionPerformed
    this.aboutItem.addActionListener(this);

    JMenuBar mb = new JMenuBar();
    mb.add(this.gameMenu);
    mb.add(this.helpMenu);

    frame.setJMenuBar(mb);

    this.button = new JButton("(Re)Start");
    this.button.addActionListener(this);

    this.button2 = new JButton("Pause");
    this.button2.addActionListener(this);

    this.buttonPanel = new JPanel();
    this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, BoxLayout.X_AXIS));
    this.buttonPanel.add(this.button);
    this.buttonPanel.add(this.button2);

    this.panel.add(this.buttonPanel);


    // make it visible (render window)

    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    this.canvas.setVisible(true);
    frame.pack();
    frame.setVisible(true);
  }

  public HashMap<Integer, Integer> getCurrentKey() {
    return canvas.getCurrentKey();
  }

  public Stack<KeyEvent> getKeyEvents() {
    return this.canvas.getKeyEvents();
  }

  public Stack<MouseEvent> getMouseEvents() {
    return this.canvas.getMouseEvents();
  }


  public void repaint() {
    canvas.repaint();
  }

  public void setPlayground(Playground pg) {
    this.canvas.setPlayground(pg);
    this.frame.validate();
    this.frame.pack();
  }

  public boolean isPainting() {
    return canvas.stillPainting();
  }

  public void setPainting() {
    canvas.setPainting();
  }

  public void waitWhilePainting() {
    canvas.setPainting();
    canvas.repaint();
    canvas.waitWhilePainting();
  }


  public static int getNewAction() {
    return newAction;
  }


  public static void resetAction() {
    newAction = -1;
  }


  public void grabFocus() {
    canvas.grabFocus();
  }


  /**
   * interface implementation of ActionListener to respond to GUI element actions. It sets the
   * attribute of newAction which is read by GameLoop.runGame to check for GUI actions.
   */
  public void actionPerformed(ActionEvent ae) {
    if (ae.getSource() == this.quitItem) {
      System.exit(0);
    } else if (ae.getSource() == this.playItem) {
      logger.info("new game");
      newAction = ACTION_NEW;
    } else if (ae.getSource() == this.button) {
      logger.info("click");
      newAction = ACTION_NEW;
    } else if (ae.getSource() == this.button2) {
      logger.info("click2");
      newAction = ACTION_PAUSE;
    } else if (ae.getSource() == this.saveItem) {
      logger.info("save");
      newAction = ACTION_SAVE;
    } else if (ae.getSource() == this.loadItem) {
      logger.info("load");
      newAction = ACTION_LOAD;
    } else if (ae.getSource() == this.aboutItem) {
      logger.info("about");
      newAction = ACTION_ABOUT;
      AboutFrame about = new AboutFrame();
      about.show();
    }
  }


}
