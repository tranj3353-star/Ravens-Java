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
    public String searchText;

    private Timer animationTimer;
    private int animationHeight = 0;
    private int targetHeight = 0;
    private int fullDetailHeight = 0;

    private static final int ANIMATION_SPEED = 20;

    public ExpandableCard(String name, String subtitle, String details, boolean isPlayer) {

        this.searchText = name + " " + subtitle + " " + details;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(CARD_BG);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(230, 230, 235), 1),
                BorderFactory.createEmptyBorder(0, 0, 0, 0)
        ));
        setAlignmentX(Component.LEFT_ALIGNMENT);

        // ===== HEADER =====
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(18, 18, 18, 18));
        headerPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

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

        // ===== DETAIL PANEL =====
        detailPanel = new JPanel(new GridLayout(1, 1));
        detailPanel.setBackground(DETAIL_BG);
        detailPanel.setBorder(BorderFactory.createEmptyBorder(15, 18, 18, 18));
        detailPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea detailText = new JTextArea(details);
        detailText.setEditable(false);
        detailText.setLineWrap(true);
        detailText.setWrapStyleWord(true);
        detailText.setBackground(DETAIL_BG);
        detailText.setFont(new Font("Arial", Font.PLAIN, 13));
        detailText.setForeground(new Color(60, 60, 70));

        detailPanel.add(detailText);

        // Calculate natural height ONCE
        detailPanel.setPreferredSize(null);
        fullDetailHeight = detailPanel.getPreferredSize().height;

        // Collapse initially
        detailPanel.setPreferredSize(new Dimension(0, 0));
        detailPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 0));
        detailPanel.setMinimumSize(new Dimension(0, 0));

        // ===== CLICK LISTENER =====
        headerPanel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                if (!expanded) {
                    headerPanel.setBackground(HOVER_COLOR);
                    leftPanel.setBackground(HOVER_COLOR);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!expanded) {
                    headerPanel.setBackground(CARD_BG);
                    leftPanel.setBackground(CARD_BG);
                }
            }

            @Override
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

        if (animationTimer != null && animationTimer.isRunning()) {
            animationTimer.stop();
        }

        this.expanded = expand;
        arrowLabel.setText(expand ? "▲" : "▼");

        targetHeight = expand ? fullDetailHeight : 0;
        animationHeight = detailPanel.getHeight();

        animationTimer = new Timer(10, e -> {

            if (animationHeight < targetHeight) {
                animationHeight = Math.min(animationHeight + ANIMATION_SPEED, targetHeight);
            } else if (animationHeight > targetHeight) {
                animationHeight = Math.max(animationHeight - ANIMATION_SPEED, targetHeight);
            }

                
            detailPanel.setPreferredSize(
                    new Dimension(detailPanel.getPreferredSize().width, animationHeight)
            );
            detailPanel.setMaximumSize(
                    new Dimension(Integer.MAX_VALUE, animationHeight)
            );

            revalidate();

            if (animationHeight == targetHeight) {
                animationTimer.stop();
            }
        });

        animationTimer.start();
    }

    private void showQuickInfo(String name, String subtitle, String details) {
        String title = subtitle.isEmpty() ? name : name + " - " + subtitle;
        JOptionPane.showMessageDialog(null,
                details,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }
}
