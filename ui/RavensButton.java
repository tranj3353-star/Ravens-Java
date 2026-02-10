package ui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RavensButton extends JButton {
    private static final Color RAVENS_GOLD = new Color(158, 124, 12);
    private static final Color RAVENS_BLACK = new Color(0, 0, 0);
    
    public RavensButton(String text) {
        super(text);
        setFont(new Font("Arial", Font.PLAIN, 13));
        setBackground(RAVENS_GOLD);
        setForeground(RAVENS_BLACK);
        setFocusPainted(false);
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                setBackground(RAVENS_GOLD.brighter());
            }
            public void mouseExited(MouseEvent e) {
                setBackground(RAVENS_GOLD);
            }
        });
    }
}
