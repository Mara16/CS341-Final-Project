package Option1;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

// GUI class extends the JFrame class - new GUI() would open a new JFrame window
public class GUI extends JFrame {

    // The panel that contains all components
    private JPanel mainPanel;

    // Constructor
    public GUI() {

        initializeTheming();

        mainPanel = new JPanel();
        // Set the layout for mainPanel to be a BoxLayout that adds child
        // components vertically downwards.
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        // Sets the title for the GUI window.
        setTitle("Distributed Employee Search System");

        // add the mainPanel to the window.
        add(mainPanel);

        // Closing the GUI stops the program.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Packs all components closely.
        pack();

        // Launch and show the GUI.
        setVisible(true);
    }

    // Initialize the theme for GUI
    private void initializeTheming() {
        // Set up the cool dark theme for GUI.
        FlatDarculaLaf.install();

        // Set this as the Look and Feel
        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception ex) {
            System.out.println("LaF setting failed.");
        }
    }
}
