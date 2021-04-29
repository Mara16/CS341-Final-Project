package Option1;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;


// GUI class extends the JFrame class - new GUI() would open a new JFrame window
public class GUI extends JFrame {

    // The panel that contains all components.
    private JPanel mainPanel;

    // The JTextField instances for the various search fields.
    private JTextField[] textFields = new JTextField[App.NUM_COLUMNS];

    // Constructor
    public GUI() {

        initializeTheming();

        mainPanel = new JPanel();

        // Set the layout for mainPanel to be a BoxLayout that adds child
        // components vertically downwards.
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));

        addHeading();

        addFields();

        // add the mainPanel to the window.
        add(mainPanel);

        // Sets the title for the GUI window.
        setTitle("Distributed Employee Search System");

        // Closing the GUI stops the program.
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Packs all components closely.
        pack();

        // Launch and show the GUI.
        setVisible(true);
    }

    // Add the input fields & labels to the GUI.
    private void addFields() {
        // Strings to put in each label
        String[] labels = {"First Name", "Last Name", "Address", "Salary", "Age"};

        for (int i = 0; i < App.NUM_COLUMNS; i++) {

            // Create panel to add the textField and label to.
            JPanel textPanel = new JPanel();

            // Set the panel to allow adding horizontally 10 pixels wide components.
            textPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));

            textFields[i] = new JTextField();
            JLabel textLabel = new JLabel(labels[i]);
            textLabel.setPreferredSize(new Dimension(100, 20));
            textLabel.setLabelFor(textFields[i]);

        }
    }

    // Add the heading label to the window.
    private void addHeading() {
        // Create JPanel to contain the label & center it using a FlowLayout.
        JPanel headingPanel = new JPanel();
        headingPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Add some padding (10px) to top and bottom.
        headingPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // Create the Label to its panel.
        JLabel headingLabel = new JLabel("Enter Search Terms");

        // set the font of the label to be the same font with bold
        headingLabel.setFont(new Font(headingLabel.getFont().getFontName(), Font.BOLD, 14));

        // Add the label to its panel.
        headingPanel.add(headingLabel);

        // Add the heading's panel to the mainPanel.
        mainPanel.add(headingPanel);
    }

    // Initialize the theme/Look and Feel for GUI - "FlatDarcula".
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
