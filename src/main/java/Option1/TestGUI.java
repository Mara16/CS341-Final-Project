package Option1;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class TestGUI {

    JFrame frame;

    // original way to do things
    String data[][] = {{"101", "Amit", "670000"},
            {"102", "Jai", "780000"},
            {"101", "Sachin", "700000"}};
    String column[] = {"ID", "NAME", "SALARY"};

    TestGUI() {

        frame = new JFrame();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));


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

        table.setBounds(30, 40, 200, 500);
        // table.setEnabled(false); // completely disable editing and selectng rows/cells
        panel.add(new JScrollPane(table));

        // Create a Button
        JButton button = new JButton("Hola");

        // Add a click event listener to the button
        button.addActionListener(e -> {

            // System.out.println("hello pressed");
            System.out.println(e.getClass());
            // data[1][0] = "PRESSED";
            // table.repaint();

            ((DefaultTableModel) table.getModel()).addRow(
                    new Object[]{"NewVal_1", "NewVal_2", "NewVal_3"}
            );
        });

        // Set the preferred size for a button
        button.setPreferredSize(new Dimension(80, 80));

        // Adding icons to JButtons (might be useful) using HTML:
        // https://stackoverflow.com/questions/61055090/jbutton-with-both-icon-on-top-and-text-on-bottom-aligned-to
        // -the-left
        // https://docs.oracle.com/javase/tutorial/uiswing/components/html.html

        // To avoid button from filling up space,
        // add the button to a panel, and add the
        // panel to other panel.
        // However, this makes the button layout out of our control?? I think
        JPanel btnPanel = new JPanel();
        btnPanel.add(button);
        panel.add(btnPanel);

        frame.add(panel);
        frame.setTitle("Testing Java Swing!");
        // frame.pack();    // packs everything closely - overrides size?
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);

    }

    public static void main(String[] args) {
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