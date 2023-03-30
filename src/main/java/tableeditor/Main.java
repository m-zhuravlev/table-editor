package tableeditor;

import tableeditor.ui.TablePane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Main {
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("TableEditor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(new TablePane(), BorderLayout.CENTER);
        contentPane.setOpaque(true);
        contentPane.setBorder(new EmptyBorder(1, 1, 1, 1));
        frame.setContentPane(contentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}