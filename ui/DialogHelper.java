package ui;

import javax.swing.*;

public class DialogHelper {
    
    public static void showWelcomeDialog(JFrame parent) {
        JOptionPane.showMessageDialog(parent,
            "Welcome to the Baltimore Ravens Roster App!\n\n" +
            "Browse the complete 2024-25 roster including:\n" +
            "• Players by position\n" +
            "• Coaching staff\n" +
            "• Front office\n" +
            "• Medical & performance staff\n\n" +
            "Use the search feature to find specific people quickly!",
            "Welcome to Ravens Roster",
            JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showAboutDialog(JFrame parent) {
        String message = "Baltimore Ravens Roster Application\n" +
                        "Version 2.0\n\n" +
                        "Complete roster and staff directory for the 2024-25 season.\n\n" +
                        "Features:\n" +
                        "• CSV-based data loading\n" +
                        "• Searchable player and staff database\n" +
                        "• Expandable detail cards\n" +
                        "• Position-based organization\n" +
                        "• Team colors and branding\n\n" +
                        "Go Ravens!";
        
        JOptionPane.showMessageDialog(parent, message, "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showHelpDialog(JFrame parent) {
        String[] options = {"Search Help", "Navigation Help", "Close"};
        int choice = JOptionPane.showOptionDialog(parent,
            "What do you need help with?",
            "Help Menu",
            JOptionPane.YES_NO_CANCEL_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);

        if (choice == 0) {
            JOptionPane.showMessageDialog(parent,
                "SEARCH HELP\n\n" +
                "• Type in the search box to filter results\n" +
                "• Search by name, position, or role\n" +
                "• Examples: 'Lamar', 'QB', 'Coach', 'Medical'\n" +
                "• Clear the search box to show all entries",
                "Search Help",
                JOptionPane.INFORMATION_MESSAGE);
        } else if (choice == 1) {
            JOptionPane.showMessageDialog(parent,
                "NAVIGATION HELP\n\n" +
                "• Click any card to expand and view details\n" +
                "• Click again to collapse\n" +
                "• Double-click for quick info popup\n" +
                "• Use 'Expand All' to open all visible cards\n" +
                "• Use 'Collapse All' to close all cards\n" +
                "• Scroll to browse all sections",
                "Navigation Help",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
