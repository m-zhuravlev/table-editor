package tableeditor;

import tableeditor.ui.TablePane;

import javax.swing.*;

public class Main {
    private static void createAndShowGUI() {

        //Create and set up the window.
        JFrame frame = new JFrame("TableEditor");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setContentPane(new TablePane());

        //Display the window.
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createAndShowGUI);
    }
}