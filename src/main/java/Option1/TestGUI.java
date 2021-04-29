package Option1;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsEnvironment;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import java.util.Collections;

public class TestGUI {

    JFrame frame;

    // original way to do things
    String data[][] = {{"101", "Amit", "670000"},
            {"102", "Jai", "780000"},
            {"101", "Sachin", "700000"}};
    String column[] = {"ID", "NAME", "SALARY"};
    private int lafIndex = 0;

    TestGUI() {

        // Setting the window to display on default/primary monitor.
        // frame = new JFrame();

        // Setting the window to display on second monitor.
        // https://www.rgagnon.com/javadetails/java-show-jframe-on-a-specific-screen.html
        frame = new JFrame(GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[1].getDefaultConfiguration());

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        // panel.setBackground(Color.CYAN);


        /*
        // Original way to do things.
        // JTable table = new JTable(data, column);

        // Improved way to do things with original constructor:
        // https://www.tutorialspoint.com/how-can-we-disable-the-cell-editing-inside-a-jtable-in-java
        JTable table = new JTable(data, column){
            public boolean editCellAt(int row, int column, java.util.EventObject e) {
                return false;
            }
        };
        table.setRowSelectionAllowed(true);
        */

        // New way using DefaultTableModel. From:
        // https://stackoverflow.com/a/3549341
        JTable table = new JTable(
                new DefaultTableModel(data, column)
        );
        // Adding rows is easy!
        DefaultTableModel tableModel = (DefaultTableModel) table.getModel();
        tableModel.addRow(new Object[]{"Test1_1", "Test1_2", "Test1_3"});
        tableModel.addRow(new Object[]{"Mara", "Test1_2", "Test1_3"});

        // By default (if you pass in data, column), table elements are
        // editable by user. To fix, use a custom table model or something.
        // This link explains how to? :
        // https://docs.oracle.com/javase/tutorial/uiswing/components/table.html#data

        // table.setBounds(30, 40, 300, 50);
        table.setEnabled(false); // completely disable editing and selectng rows/cells

        JPanel tablesPanel = new JPanel();
        tablesPanel.setLayout(new BoxLayout(tablesPanel, BoxLayout.PAGE_AXIS));
        tablesPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        tablesPanel.setPreferredSize(new Dimension(400, 200));
        tablesPanel.add(new JScrollPane(table));
        // tablesPanel.setBackground(Color.RED);

        panel.add(tablesPanel);

        // Create a Button
        JButton button = new JButton("Hola");
        JButton button2 = new JButton("Hola2");

        // Add a click event listener to the button
        button.addActionListener(e -> {

            System.out.println("hola pressed");
            // data[1][0] = "PRESSED";
            // table.repaint();

            /*((DefaultTableModel) table.getModel()).addRow(
                    new Object[]{"NewVal_1", "NewVal_2", "NewVal_3"}
            );*/

            var tableRows = Arrays.asList(tableModel.getDataVector());
            System.out.println(tableRows);
            Collections.reverse(tableRows);
            // System.out.println(tableModel.getDataVector());
            System.out.println(tableRows);
            // ((DefaultTableModel) table.getModel()).setDataVector((Object[][]) data, column);

            /*// Toggling through available LAF's
            try {
                // UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
                UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[lafIndex].getClassName());
                frame.repaint();
            } catch (ClassNotFoundException classNotFoundException) {
                classNotFoundException.printStackTrace();
            } catch (InstantiationException instantiationException) {
                instantiationException.printStackTrace();
            } catch (IllegalAccessException illegalAccessException) {
                illegalAccessException.printStackTrace();
            } catch (UnsupportedLookAndFeelException unsupportedLookAndFeelException) {
                unsupportedLookAndFeelException.printStackTrace();
            }
            System.out.println(UIManager.getInstalledLookAndFeels()[lafIndex].getClassName());
            lafIndex++;
            lafIndex %= UIManager.getInstalledLookAndFeels().length;*/

        });

        // Set the preferred size for a button
        button.setMaximumSize(new Dimension(70, 50));
        // button.setSize(new Dimension(100,100));
        button2.setMaximumSize(new Dimension(70, 50));

        // Adding icons to JButtons (might be useful) using HTML:
        // https://stackoverflow.com/questions/61055090/jbutton-with-both-icon-on-top-and-text-on-bottom-aligned-to
        // -the-left
        // https://docs.oracle.com/javase/tutorial/uiswing/components/html.html

        // To avoid button from filling up space,
        // add the button to a panel, and add the
        // panel to other panel.
        // However, this makes the button layout out of our control?? I think

        // Lot of code from:
        // https://docs.oracle.com/javase/tutorial/uiswing/layout/box.html

        // Panel for adding buttons to
        JPanel btnPanel = new JPanel();

        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.LINE_AXIS));
        // btnPanel.setAlignmentX(Box.LEFT_ALIGNMENT);
        btnPanel.add(Box.createHorizontalGlue());
        btnPanel.add(button);
        btnPanel.add(button2);
        btnPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        // btnPanel.setBackground(Color.YELLOW);
        btnPanel.setPreferredSize(new Dimension(0, 80));
        panel.add(btnPanel);

        frame.add(panel);
        frame.setTitle("Testing Java Swing!");
        // frame.setMinimumSize(new Dimension(400, 300));
        frame.pack();    // packs everything closely - overrides size?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        /*// Removing frame decorations like title
        frame.setUndecorated(true);
        // setting rounded corners - jagged for some reason
        frame.setShape(new RoundRectangle2D.Double(0, 0, frame.getWidth(), frame.getHeight(), 50, 50));*/

        JTextField text = new JTextField();
        text.setToolTipText("Testing tooltip");
        // Making the textfield select all text when it is clicked on/focused.
        // Modified from: https://stackoverflow.com/a/38386682
        FocusListener textSelectListener = new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField focusedField = (JTextField) e.getSource();
                focusedField.selectAll();
            }

            @Override
            public void focusLost(FocusEvent e) {
                JTextField focusedField = (JTextField) e.getSource();
                focusedField.select(0, 0);
            }
        };
        text.addFocusListener(textSelectListener);

        JLabel textLabel = new JLabel("Label for text:");
        textLabel.setPreferredSize(new Dimension(100, 20));
        textLabel.setLabelFor(text);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

        JPanel textPanel2 = new JPanel();
        textPanel2.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));

        textPanel.add(textLabel);
        textPanel.add(text);
        panel.add(textPanel);

        JTextField text2 = new JTextField();
        text2.addFocusListener(textSelectListener);
        JLabel text2Label = new JLabel("Label for text2:");
        text2Label.setPreferredSize(new Dimension(100, 20));
        text2Label.setLabelFor(text2);

        textPanel2.add(text2Label);
        textPanel2.add(text2);
        panel.add(textPanel2);

        frame.setVisible(true);
    }

    public static void main(String[] args) {

        // Trying to set the Dark theme from FlatLAF
        // FlatDarkLaf.install();
        FlatDarculaLaf.install();   // IntelliJ dark theme

        try {
            UIManager.setLookAndFeel(new FlatDarculaLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }

        TestGUI app = new TestGUI();
    }
}

/*
        String data[][] = {{"101", "Amit", "670000"},
                {"102", "Jai", "780000"},
                {"101", "Sachin", "700000"}};
        String column[] = {"ID", "NAME", "SALARY"};

        JTable jt = new JTable(data, column);
        jt.setBounds(30, 40, 200, 100);

        JButton jb = new JButton("Test");
        jb.setBounds(80, 100, 100, 50);
        // jb.setIcon(new ImageIcon("jtp_logo.png"));

        JScrollPane sp = new JScrollPane(null);
        sp.add(jt);
        sp.add(jb);

        ScrollPaneLayout spl = new ScrollPaneLayout();
        sp.setLayout(spl);

        f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(sp);
        f.setSize(300, 300);
        // f.setResizable(false);
        // f.setVisible(true);
        */