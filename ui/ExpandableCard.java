package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ExpandableCard extends JPanel {
    private static final Color RAVENS_PURPLE = new Color(36, 23, 115);
    private static final Color RAVENS_GOLD = new Color(158, 124, 12);
    private static final Color RAVENS_BLACK = new Color(0, 0, 0);
    private static final Color CARD_BG = Color.WHITE;
    private static final Color DETAIL_BG = new Color(248, 248, 250);
    private static final Color HOVER_COLOR = new Color(245, 243, 255);
    
    private JPanel detailPanel;
    private JLabel arrowLabel;
    private JPanel headerPanel;
    private boolean expanded = false;
    private int width = 500;
    public String searchText;

    public ExpandableCard(String name, String subtitle, String details, boolean isPlayer) {
        this.searchText = name + " " + subtitle + " " + details;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(CARD_BG);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 235), 1),
            BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        setAlignmentX(Component.LEFT_ALIGNMENT);
        setMaximumSize(new Dimension(width, 100));
        setPreferredSize(new Dimension(width, 70));

        // Header (clickable)
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        headerPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        headerPanel.setPreferredSize(new Dimension(width, 70));
        headerPanel.setMinimumSize(new Dimension(0, 70));

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(CARD_BG);

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 15));
        nameLabel.setForeground(RAVENS_BLACK);
        
        JLabel subtitleLabel = new JLabel(subtitle);
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitleLabel.setForeground(new Color(100, 100, 120));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

        leftPanel.add(nameLabel);
        if (!subtitle.isEmpty()) {
            leftPanel.add(subtitleLabel);
        }

        arrowLabel = new JLabel("▼");
        arrowLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        arrowLabel.setForeground(RAVENS_GOLD);

        headerPanel.add(leftPanel, BorderLayout.CENTER);
        headerPanel.add(arrowLabel, BorderLayout.EAST);

        // Detail panel
        detailPanel = new JPanel(new BorderLayout());
        detailPanel.setBackground(DETAIL_BG);
        detailPanel.setBorder(BorderFactory.createEmptyBorder(15, 18, 18, 18));
        detailPanel.setVisible(false);
        detailPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea detailText = new JTextArea(details);
        detailText.setEditable(false);
        detailText.setLineWrap(true);
        detailText.setWrapStyleWord(true);
        detailText.setBackground(DETAIL_BG);
        detailText.setFont(new Font("Arial", Font.PLAIN, 13));
        detailText.setForeground(new Color(60, 60, 70));

        detailPanel.add(detailText, BorderLayout.CENTER);

        // Click to expand/collapse
        headerPanel.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!expanded) headerPanel.setBackground(HOVER_COLOR);
                leftPanel.setBackground(HOVER_COLOR);
            }
            public void mouseExited(MouseEvent e) {
                if (!expanded) {
                    headerPanel.setBackground(CARD_BG);
                    leftPanel.setBackground(CARD_BG);
                }
            }
            public void mouseClicked(MouseEvent e) {
                toggleExpand();
            }
        });

        add(headerPanel);
        add(detailPanel);
    }

    private void toggleExpand() {
        setExpanded(!expanded);
    }

    public void setExpanded(boolean expand) {
        this.expanded = expand;
        detailPanel.setVisible(expand);
        arrowLabel.setText(expand ? "▲" : "▼");
        
        if (expand) {
            setMaximumSize(new Dimension(width, Integer.MAX_VALUE));
            setPreferredSize(null);
        } else {
            setMaximumSize(new Dimension(width, 100));
            setPreferredSize(new Dimension(width, 70));
        }
        
        revalidate();
        repaint();
    }

    private void showQuickInfo(String name, String subtitle, String details) {
        String title = subtitle.isEmpty() ? name : name + " - " + subtitle;
        JOptionPane.showMessageDialog(null,
            details,
            title,
            JOptionPane.INFORMATION_MESSAGE);
    }
}
