package Option1;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;


// GUI class extends the JFrame class - new GUI() would open a new JFrame window
public class GUI extends JFrame {

    // The panel that contains all components.
    private JPanel mainPanel;

    // The search button for searching for entries.
    JButton searchButton;

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

        addTextFields();

        addSearchButton();

        addResultComponents();

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

    // Method to add components to the GUI that show the results of search queries.
    // Adds a Label for the "Search Results" title, JTable for containing the result,
    // and another label (?) for containing the "No results found" message.
    private void addResultComponents() {

        // Panel for containing the search results label.
        JPanel labelPanel = new JPanel();

        // Set this panel to have a center-aligned layout for its contents.
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Add 10px padding below the panel.
        labelPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        // Create the actual label, set font size to 16, and add to panel.
        JLabel resultLabel = new JLabel("Search Results");
        resultLabel.setFont(new Font(resultLabel.getFont().getFontName(), Font.BOLD, 16));
        labelPanel.add(resultLabel);

        // Add label's panel to mainPanel.
        mainPanel.add(labelPanel);


    }

    // Adds a serach button to the GUI window, and event listeners for it.
    // TODO: Add eventListener to the button to actually send the search request.
    private void addSearchButton() {

        // Create the panel for the button
        JPanel btnPanel = new JPanel();
        // Set the buttonPanel to have a flow layout with
        // center-aligned components.
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Add an empty bottom padding to the panel containing the buttons.
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));

        // Image taken from https://fonts.google.com/icons
        Icon icon = new ImageIcon("src/main/java/Option1/search_icon.png");
        // By default, the icon image cannot be resized - so we make a copy of that
        // icon image with a lower size & set the newly created Image as the new icon.
        Image img = ((ImageIcon) icon).getImage();
        Image shrunkImg = img.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
        ((ImageIcon) icon).setImage(shrunkImg);

        // Create the button, set the icon & other layout/design elements.
        searchButton = new JButton("Search", icon);
        searchButton.setPreferredSize(new Dimension(110, 40));

        // Set the font size of the button to be 16.
        searchButton.setFont(new Font(searchButton.getFont().getFontName(), Font.PLAIN, 16));

        // Add the button to its panel.
        btnPanel.add(searchButton);

        // Add the button's panel to mainPanel.
        mainPanel.add(btnPanel);
    }

    // Add the input fields & labels to the GUI.
    private void addTextFields() {
        // Strings to put in each label
        String[] labels = {"First Name", "Last Name", "Address", "Salary", "Age"};

        for (int i = 0; i < App.NUM_COLUMNS; i++) {

            // Create panel to add the textField and label to.
            JPanel textPanel = new JPanel();

            // Create some padding around the panel and below.
            textPanel.setBorder(BorderFactory.createEmptyBorder(0,40,10,40));

            // Set the panel to allow adding components horizontally with 10px gaps.
            textPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));

            // Create the textField, set the size & font size.
            textFields[i] = new JTextField();
            textFields[i].setPreferredSize(new Dimension(200, 40));
            textFields[i].setFont(new Font(textFields[i].getFont().getFontName(), Font.PLAIN, 14));

            // Create the label, set the size & font size for the label, and assign it to the field.
            JLabel textLabel = new JLabel(labels[i]);
            textLabel.setPreferredSize(new Dimension(100, 40));
            textLabel.setFont(new Font(textLabel.getFont().getFontName(), Font.PLAIN, 14));
            textLabel.setLabelFor(textFields[i]);

            // Add the label and text field to the panel for it.
            textPanel.add(textLabel);
            textPanel.add(textFields[i]);

            // Add this panel to the main panel.
            mainPanel.add(textPanel);
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
        headingLabel.setFont(new Font(headingLabel.getFont().getFontName(), Font.BOLD, 16));

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
