package ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * 
 * Creates a JFrame with a short text about the creators of the game code.
 *
 */
class AboutFrame extends Object {
  private JFrame frame = new JFrame();

  AboutFrame() {

    JPanel aboutPanel = new JPanel();

    JLabel text = new JLabel("About", JLabel.CENTER);
    JTextArea field = new JTextArea(
        "This game was developed by Prof. Gepperth and Prof. Konert and the students of module Programming 2. It contains several levels of SpaceInvadersLevel style and BreakoutLevel style. The framework below allows quick creation of different 2d game types.");
    field.setBounds(25, 25, 80, 80);
    field.setLineWrap(true);
    field.setWrapStyleWord(true);
    field.setBackground(aboutPanel.getBackground());
    field.setEditable(false);

    JButton ok = new JButton("OK");
    ok.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        frame.setVisible(false);
      }
    });

    aboutPanel.setLayout(new BoxLayout(aboutPanel, BoxLayout.Y_AXIS));
    aboutPanel.add(text);
    aboutPanel.add(field);
    aboutPanel.add(ok);

    frame.add(aboutPanel);
    frame.setSize(400, 400);
  }

  protected void show() {
    frame.setVisible(true);
  }

}
