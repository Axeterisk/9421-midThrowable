package midlab1;

import jdk.jshell.execution.Util;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;


/**
 * The GUI class
 */
public class Interface {

    private static final int TEXTAREA_COLUMNS = 20;
    LinkedStack<Token> stack;   // Local stack variable
    // GUI Component Classes
    private JPanel labelPanel = new JPanel();
    private JPanel upperPanel = new JPanel();
    private JScrollPane tabelPanel;
    private JTextArea tableText = new JTextArea(20, 120);
    private JTextField input = new JTextField(TEXTAREA_COLUMNS);
    private String[] conversionType = { "Infix to Postfix Expression", "Evaluate Postfix Expression" };
    private JComboBox<String> expressionType = new JComboBox<>(conversionType);
    private JPanel expressionPanel = new JPanel();
    private JButton submit = new JButton("Submit");
    private JButton clear = new JButton("Clear");

    // Store multiple components into arrays, then add them at once via a for loop
    private JComponent[] upperPanelComponents = { new JLabel("Expression: "), input, submit };
    private JComponent[] expressionPanelComponents = { new JLabel("Expression Type: "),
            expressionType, clear };

    /**
     * Initializes, attaches ActionListeners to JComponents
     * and attaches JComponents to JPanels
     */
    public Interface() {
        redirectSystemStreams();    // redirect printStream to JTextArea

        labelPanel.add(new JLabel("Infix-Postfix Utility"));

        // Add multiple JComponents at once
        for (JComponent component : upperPanelComponents) upperPanel.add(component);
        for (JComponent component : expressionPanelComponents) expressionPanel.add(component);
        expressionType.setSelectedIndex(0);

        // Code for displaying the formatted table
        // for stack operations
        tabelPanel = new JScrollPane(tableText);
        tabelPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        tabelPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        tableText.setEditable(false);

        // Associate code for the submit button
        submit.addActionListener((ActionEvent e) -> {
            try {
                stack = Utility.parseInput(input.getText());
                if (expressionType.getSelectedIndex() == 0) Utility.infixToPostfixTable(stack);
                else Utility.postfixEvaluateTable(stack);
                if (input.getText().equals("")) {
                    JOptionPane.showMessageDialog(null, "Input cannot be empty.");
                    tableText.setText(null);
                }
            } catch (StackException stackException) {
                JOptionPane.showMessageDialog(null,
                        "Invalid expression or wrong expression type selected.");
                tableText.setText(null);
            }
        });
        // Clear the text pane
        clear.addActionListener((ActionEvent e) -> tableText.setText(null));
    }

    /**
     * Helper method for redirectSystemStreams()
     * @param text
     */
    private void updateTextPane(final String text) {
        SwingUtilities.invokeLater(() -> {
            Document doc = tableText.getDocument();
            try {
                doc.insertString(doc.getLength(), text, null);
            } catch (BadLocationException e) {

            }
            tableText.setCaretPosition(doc.getLength() - 1);
        });
    }

    /**
     * Redirects CLI output to the JTextArea tableText
     */
    private void redirectSystemStreams() {
        OutputStream out = new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                updateTextPane(String.valueOf((char) b));
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                updateTextPane(new String(b, off, len));
            }

            @Override
            public void write(byte[] b) throws IOException {
                write(b, 0, b.length);
            }
        };
        System.setOut(new PrintStream(out, true));
        // System.setErr(new PrintStream(out, true));
    }
    /*
        Accessor methods for properties
     */
    public JPanel getLabelPanel() {
        return labelPanel;
    }

    public JPanel getUpperPanel() {
        return upperPanel;
    }

    public JPanel getExpressionPanel() {
        return expressionPanel;
    }

    public JScrollPane getTabelPanel() {
        return tabelPanel;
    }

    /**
     * Uses the concept of composition instead of extending the JFrame class itself
     * This method creates a new JFrame, sets the BoxLayout and attaches
     * JPanels directly to the ContentFrame
     *
     * ALGORITHM:
     *      1.) Create a new instance of the Interface class
     *      2.) Create a new JFrame
     *      3.) Set the content pane to a BoxLayout
     *      4.) For every JPanel, add them to the content pane
     *      5.) Display the Window
     */
    private static void createAndShowGUI() {
        Interface gui = new Interface();
        JFrame frame = new JFrame();
        // Use the Box Layout for the Content Pane
        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Add the panels
        frame.getContentPane().add(gui.getLabelPanel());
        frame.getContentPane().add(gui.getUpperPanel());
        frame.getContentPane().add(gui.getExpressionPanel());
        frame.getContentPane().add(gui.getTabelPanel());

        // Display the window
        frame.pack();
        frame.setTitle("Infix-Postfix Utility");
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Run the program in the Event Dispatch Thread (EDT)
        // For Thread Safety
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Silence the compiler
            }
            createAndShowGUI();
        });

    }



}
