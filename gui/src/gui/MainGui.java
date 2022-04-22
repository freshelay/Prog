package gui;


import javax.swing.*;
import java.awt.event.*;
import java.util.*;


/**
 * a simple GUI that simulates a slow database for name, email entries. For details see constructor {@link MainGui#MainGui() }.
 * This class provides a simple static {@link #main(String[])} to start/run the GUI window.
 */
public class MainGui {

  private JFrame frame = null;
  private JPanel cp = new JPanel();
  private JPanel topPanel = new JPanel();
  private JPanel middlePanel = new JPanel();
  private JPanel bottomPanel = new JPanel();

  private JTextField tfName = new JTextField("", 20);
  private JTextField tfMail = new JTextField("", 20);

  private JButton btBack = new JButton("<--");
  private JButton btForward = new JButton("-->");
  private JButton btAdd = new JButton("Neu");
  private ArrayList<DBEntry> database = new ArrayList<DBEntry>();

  private int index = 0;

  private JMenuItem menuNew = new JMenuItem("Neu");
  private JMenuItem menuEnd = new JMenuItem("Ende");

  /** inner class representing one entry to DB */
  private class DBEntry {
    String name;
    String email;
  }

  /** inner class to be used for ActionListener back */
  private class BackAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      // always save current entry before loading next
      DBEntry entry = new DBEntry(); // we create a new DBEntry object instead manipulating the
                                     // existing one to simulate real new reference to a new object.
      entry.name = tfName.getText();
      entry.email = tfMail.getText();
      saveFieldsToDB(entry, index);

      if (index >= 1) {
        index--;
        DBEntry entryLoaded = getEntryFromDB(index);
        tfName.setText(entryLoaded.name);
        tfMail.setText(entryLoaded.email);
      }
    }
  }

  /** inner class to be used for ActionListener forward */
  private class ForwardAction implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {

      // always save current entry before loading next
      DBEntry entry = new DBEntry(); // we create a new DBEntry object instead manipulating the
                                     // existing one to simulate real new reference to a new object.
      entry.name = tfName.getText();
      entry.email = tfMail.getText();
      saveFieldsToDB(entry, index);

      if (index < database.size() - 1) {
        index++;
        DBEntry entryLoaded = getEntryFromDB(index);
        tfName.setText(entryLoaded.name);
        tfMail.setText(entryLoaded.email);
      }
    }
  }



  /** inner class to be used for ActionListener exit program */
  private class EndAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      System.exit(0);
    }
  }

  /** inner class to be used for ActionListener add */
  private class AddAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
      // always save current entry before loading next
      DBEntry entry = new DBEntry(); // we create a new DBEntry object instead manipulating the
                                     // existing one to simulate real new reference to a new object.
      entry.name = tfName.getText();
      entry.email = tfMail.getText();
      saveFieldsToDB(entry, index);

      addEmptyEntryToDB();
      index = database.size() - 1;
      DBEntry entryLoaded = getEntryFromDB(index);
      tfName.setText(entryLoaded.name);
      tfMail.setText(entryLoaded.email);
    }
  }



  /** manipulation of DB and simulates slow connection by Thread.wait() */
  private void saveFieldsToDB(DBEntry entry, int index) {
    database.set(index, entry);
    doWait();
  }

  /** add an empty entry at end of db (quickly) */
  private void addEmptyEntryToDB() {
    DBEntry dbEntry = new DBEntry();
    dbEntry.name = "";
    dbEntry.email = "";
    database.add(dbEntry);
  }

  /** reads from DB (quickly) */
  private DBEntry getEntryFromDB(int index) {
    return this.database.get(index);
  }



  /** performing a Thread.wait() for 3 sec */
  private void doWait() {
    try {
      synchronized (database) {
        database.wait(3 * 1000); // simulates "slow" database by waiting 3sec before this Thread
                                 // continues. wait needs monitor of the object, thus synchronized
      }
    } catch (InterruptedException e1) {
      // intentionally blank; wait did not work or was earlier interrupted. No problem for us.
    }
  }


  /**
   * constructor that configures and shows the GUI window. It contains two text inputs for name and
   * email address and three buttons BACK, ADD and FORWARD. For each button own inner classes are
   * defined as {@link ActionListener}: {@link BackAction}, {@link ForwardAction }, {@link AddAction
   * }, which all three call the {@link #doWait()} method, which simulates a slow database. Thus, it
   * will have an effect to move the code of the ActionListeners to extra Threads in order to keep
   * the GUI responsive.
   * 
   * @see Runnable
   * @see Thread
   * @see ActionListener
   * 
   */
  public MainGui() {

    // dummy data to "database"
    DBEntry e1 = new DBEntry();
    e1.name = "Dummy1";
    e1.email = "dummy@test.de";
    DBEntry e2 = new DBEntry();
    e2.name = "Dummy2";
    e2.email = "dummy2@somewhere.com";
    this.database.add(e1);
    this.database.add(e2);

    // setup the GUI
    this.frame = new JFrame("SimpleDB");
    this.frame.setContentPane(this.cp);
    this.cp.setLayout(new BoxLayout(this.cp, BoxLayout.Y_AXIS));

    this.topPanel.add(new JLabel("Name"));
    this.topPanel.add(this.tfName);
    this.middlePanel.add(new JLabel("E-Mail-Adresse"));
    this.middlePanel.add(this.tfMail);
    this.cp.add(this.topPanel);
    this.cp.add(this.middlePanel);

    this.bottomPanel.add(this.btBack);
    this.bottomPanel.add(this.btAdd);
    this.bottomPanel.add(this.btForward);
    this.cp.add(this.bottomPanel);

    // set menu
    JMenuBar jmb = new JMenuBar();
    JMenu menu = new JMenu("Datei");
    this.frame.setJMenuBar(jmb);
    jmb.add(menu);
    menu.add(menuNew);
    menu.addSeparator();
    menu.add(menuEnd);

    // register all ActionListeners to GUI components
    this.btBack.addActionListener(new BackAction());
    this.btForward.addActionListener(new ForwardAction());

    ActionListener actionAdd = new AddAction();
    this.btAdd.addActionListener(actionAdd);
    this.menuNew.addActionListener(actionAdd);

    menuEnd.addActionListener(new EndAction());

    // initially show current db fields (of index 0)
    DBEntry entry = getEntryFromDB(index);
    tfName.setText(entry.name);
    tfMail.setText(entry.email);


    this.frame.setSize(800, 200);
    this.frame.setVisible(true);
  }

  /**
   * a simple main which only creates an instance of {@link MainGui}.
   * 
   * @param args cmd line arguments (ignored)
   */
  public static void main(String[] args) {
    new MainGui();
  }
}

