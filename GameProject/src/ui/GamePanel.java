package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Stack;
import javax.swing.JPanel;
import playground.Playground;
import java.awt.event.*;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * UI element that holds the graphic visualization of current {@link Playground}. Registers key and
 * mouse events.
 *
 */
class GamePanel extends JPanel implements KeyListener, MouseListener {
  private static final long serialVersionUID = 1L;
  protected volatile boolean painting = false;
  private Playground playground = null;
  private HashMap<Integer, Integer> keys = new HashMap<Integer, Integer>();
  Stack<KeyEvent> keyEvents = new Stack<KeyEvent>();
  Stack<MouseEvent> mouseEvents = new Stack<MouseEvent>();

  private static Logger logger = LogManager.getLogger(GamePanel.class);

  GamePanel() {
    super();
    setPreferredSize(new Dimension(100, 100));
    addKeyListener(this);
  }

  public void setSize(int sizeX, int sizeY) {
    setPreferredSize(new Dimension(sizeX, sizeY));
  }

  @Override
  public void repaint() {
    super.repaint();
  }

  public void setPainting() {
    painting = true;
  }


  public void waitWhilePainting() {
    while (painting) {
    }
  }


  Stack<KeyEvent> getKeyEvents() {
    return keyEvents;
  }


  Stack<MouseEvent> getMouseEvents() {
    return mouseEvents;
  }


  public void setPlayground(Playground pg) {
    this.playground = pg;
    if (this.playground != null) {
      setPreferredSize(new Dimension(this.playground.getSizeX(), this.playground.getSizeX()));
      setSize(getPreferredSize());  
      this.invalidate();
    }
  }

  public boolean stillPainting() {
    return painting;
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    logger.trace("my Playground: " + playground);
    if (playground != null) {
      playground.redraw((Graphics2D) g);
    }
    painting = false;

  }


  public HashMap<Integer, Integer> getCurrentKey() {
    return keys;
  }


  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    logger.debug("pressed keyCode " + e.getKeyCode());
    logger.trace(e.paramString());
    this.keyEvents.push(e);
  }

  @Override
  public void keyReleased(KeyEvent e) {

    logger.debug("released keyCode " + e.getKeyCode());
    this.keyEvents.push(e);
    logger.trace(e.paramString());
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    this.mouseEvents.push(e);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    // this.mouseEvents.push(e) ;
  }


  @Override
  public void mousePressed(MouseEvent e) {
    // this.mouseEvents.push(e) ;
  }



  @Override
  public void mouseEntered(MouseEvent e) {
    // this.mouseEvents.push(e) ;
  }

  @Override
  public void mouseExited(MouseEvent e) {
    // this.mouseEvents.push(e) ;
  }
}
