package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class HeaderPanel extends JPanel {
    private static final Color RAVENS_PURPLE = new Color(36, 23, 115);
    private static final Color RAVENS_GOLD = new Color(158, 124, 12);
    private static final Color RAVENS_BLACK = new Color(0, 0, 0);
    
    private JTextField searchField;
    private JComboBox<String> filterDropdown;
    
    public HeaderPanel(JFrame parentFrame, 
                      SearchListener searchListener,
                      FilterListener filterListener,
                      ControlButtonListener controlListener) {
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(RAVENS_PURPLE);
        setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

        // Title
        JLabel title = new JLabel("BALTIMORE RAVENS");
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(RAVENS_GOLD);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel("2024-25 Roster & Coaching Staff");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle.setForeground(Color.WHITE);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        subtitle.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        // Search and controls
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        controls.setBackground(RAVENS_PURPLE);
        controls.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Search field
        searchField = new JTextField(25);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(RAVENS_GOLD, 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                searchListener.onSearch(searchField.getText());
            }
        });

        // Control buttons
        RavensButton expandAll = new RavensButton("Expand All");
        expandAll.addActionListener(e -> controlListener.onExpandAll());

        RavensButton collapseAll = new RavensButton("Collapse All");
        collapseAll.addActionListener(e -> controlListener.onCollapseAll());

        RavensButton aboutBtn = new RavensButton("About");
        aboutBtn.addActionListener(e -> DialogHelper.showAboutDialog(parentFrame));

        RavensButton helpBtn = new RavensButton("Help");
        helpBtn.addActionListener(e -> DialogHelper.showHelpDialog(parentFrame));

        // Filter dropdown
        String[] filterOptions = {
            "All Positions",
            "QB - Quarterbacks",
            "RB - Running Backs",
            "WR - Wide Receivers",
            "TE - Tight Ends",
            "OL - Offensive Line",
            "DL - Defensive Line",
            "LB - Linebackers",
            "DB - Defensive Backs",
            "ST - Special Teams",
            "Coaches"
        };
        
        filterDropdown = new JComboBox<>(filterOptions);
        filterDropdown.setFont(new Font("Arial", Font.PLAIN, 13));
        filterDropdown.setBackground(Color.WHITE);
        filterDropdown.setForeground(RAVENS_BLACK);
        filterDropdown.setCursor(new Cursor(Cursor.HAND_CURSOR));
        filterDropdown.setMaximumRowCount(11);
        
        filterDropdown.addActionListener(e -> {
            String selected = (String) filterDropdown.getSelectedItem();
            String filter = "";
            
            if (selected.startsWith("QB")) filter = "QB";
            else if (selected.startsWith("RB")) filter = "RB";
            else if (selected.startsWith("WR")) filter = "WR";
            else if (selected.startsWith("TE")) filter = "TE";
            else if (selected.startsWith("OL")) filter = "OL";
            else if (selected.startsWith("DL")) filter = "DL";
            else if (selected.startsWith("LB")) filter = "LB";
            else if (selected.startsWith("DB")) filter = "DB";
            else if (selected.startsWith("ST")) filter = "ST";
            else if (selected.equals("Coaches")) filter = "Coach";
            
            filterListener.onFilter(filter);
        });

        // Add components to controls panel
        controls.add(new JLabel("Search: ") {{
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.PLAIN, 14));
        }});
        controls.add(searchField);
        controls.add(new JLabel("  Filter: ") {{
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.PLAIN, 14));
        }});
        controls.add(filterDropdown);
        controls.add(expandAll);
        controls.add(collapseAll);
        controls.add(aboutBtn);
        controls.add(helpBtn);

        add(title);
        add(subtitle);
        add(controls);
    }
    
    public void clearSearchField() {
        searchField.setText("");
    }
    
    // Listener interfaces
    public interface SearchListener {
        void onSearch(String query);
    }
    
    public interface FilterListener {
        void onFilter(String category);
    }
    
    public interface ControlButtonListener {
        void onExpandAll();
        void onCollapseAll();
    }
}
