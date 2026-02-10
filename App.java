import java.awt.*;
import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import javax.swing.*;
import ui.*;

public class App {
    // Data structure to hold roster information
    static class RosterEntry {
        String category;
        String name;
        String details;
        
        RosterEntry(String category, String name, String details) {
            this.category = category;
            this.name = name;
            this.details = details;
        }
    }
    
    private static List<ExpandableCard> allCards = new ArrayList<>();
    private static HeaderPanel headerPanel;
    private static JPanel contentPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::createUI);
    }

    private static void createUI() {
        JFrame frame = new JFrame("Baltimore Ravens – Roster & Staff");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 900);

        // Main container
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(ColorScheme.BG_COLOR);

        // Header with listeners
        headerPanel = new HeaderPanel(
            frame,
            App::filterContent,           // Search listener
            App::filterByCategory,         // Filter listener
            new HeaderPanel.ControlButtonListener() {
                public void onExpandAll() { toggleAll(true); }
                public void onCollapseAll() { toggleAll(false); }
            }
        );
        mainContainer.add(headerPanel, BorderLayout.NORTH);

        // Content panel with cards
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(ColorScheme.BG_COLOR);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Load roster data from CSV
        String csvPath = Paths.get("ravens_roster.csv").toAbsolutePath().toString();
        List<RosterEntry> roster = loadRosterFromCSV(csvPath);
        
        populateContent(contentPanel, roster);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainContainer.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainContainer);
        frame.setVisible(true);
        
        // Show welcome dialog AFTER frame is visible
        SwingUtilities.invokeLater(() -> DialogHelper.showWelcomeDialog(frame));
    }

    // Filter cards by category/position and move to top
    private static void filterByCategory(String category) {
        headerPanel.clearSearchField();
        String lowerCategory = category.toLowerCase().trim();
        
        // If "All Positions" selected, restore original content
        if (lowerCategory.isEmpty()) {
            contentPanel.removeAll();
            
            // Reload roster and repopulate with original structure
            String csvPath = Paths.get("ravens_roster.csv").toAbsolutePath().toString();
            List<RosterEntry> roster = loadRosterFromCSV(csvPath);
            populateContent(contentPanel, roster);
            
            contentPanel.revalidate();
            contentPanel.repaint();
            
            // Scroll to top
            SwingUtilities.invokeLater(() -> {
                contentPanel.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
            });
            return;
        }
        
        // Separate cards into matching and non-matching
        List<ExpandableCard> matchingCards = new ArrayList<>();
        List<ExpandableCard> nonMatchingCards = new ArrayList<>();
        
        for (ExpandableCard card : allCards) {
            boolean matches = card.searchText.toLowerCase().contains(lowerCategory);
            card.setVisible(true); // Show all cards
            
            if (matches) {
                matchingCards.add(card);
            } else {
                nonMatchingCards.add(card);
            }
        }
        
        // Reorder the content panel
        contentPanel.removeAll();
        
        // Add filtered section header
        String filterTitle = "";
        if (lowerCategory.equals("qb")) filterTitle = "QUARTERBACKS";
        else if (lowerCategory.equals("rb")) filterTitle = "RUNNING BACKS";
        else if (lowerCategory.equals("wr")) filterTitle = "WIDE RECEIVERS";
        else if (lowerCategory.equals("te")) filterTitle = "TIGHT ENDS";
        else if (lowerCategory.equals("ol")) filterTitle = "OFFENSIVE LINE";
        else if (lowerCategory.equals("dl")) filterTitle = "DEFENSIVE LINE";
        else if (lowerCategory.equals("lb")) filterTitle = "LINEBACKERS";
        else if (lowerCategory.equals("db")) filterTitle = "DEFENSIVE BACKS";
        else if (lowerCategory.equals("st")) filterTitle = "SPECIAL TEAMS";
        else if (lowerCategory.equals("coach")) filterTitle = "COACHING STAFF";
        
        if (!filterTitle.isEmpty() && !matchingCards.isEmpty()) {
            addSection(contentPanel, filterTitle, ColorScheme.RAVENS_GOLD);
        }
        
        // Add matching cards first
        for (ExpandableCard card : matchingCards) {
            contentPanel.add(card);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        
        // Add a separator if there are both matching and non-matching
        if (!matchingCards.isEmpty() && !nonMatchingCards.isEmpty()) {
            JPanel separator = new JPanel();
            separator.setLayout(new BorderLayout());
            separator.setBackground(ColorScheme.BG_COLOR);
            separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            separator.setAlignmentX(Component.LEFT_ALIGNMENT);
            separator.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
            
            JLabel separatorLabel = new JLabel("─────  Other Positions  ─────");
            separatorLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            separatorLabel.setForeground(new Color(150, 150, 160));
            separatorLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            separator.add(separatorLabel, BorderLayout.CENTER);
            contentPanel.add(separator);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        
        // Add non-matching cards after
        for (ExpandableCard card : nonMatchingCards) {
            contentPanel.add(card);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 8)));
        }
        
        contentPanel.revalidate();
        contentPanel.repaint();
        
        // Scroll to top
        SwingUtilities.invokeLater(() -> {
            contentPanel.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
        });
    }

    // Load roster from CSV file
    private static List<RosterEntry> loadRosterFromCSV(String filename) {
        List<RosterEntry> roster = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean firstLine = true;
            int lineNumber = 0;
            
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (firstLine) {
                    firstLine = false;
                    continue; // Skip header
                }
                
                // Parse CSV line handling quoted fields
                String[] parts = parseCSVLine(line);
                if (parts.length >= 3) {
                    roster.add(new RosterEntry(
                        parts[0].trim(),
                        parts[1].trim(),
                        parts[2].trim().replace("\\n", "\n")
                    ));
                } else {
                    System.err.println("Warning: Line " + lineNumber + " has only " + parts.length + " fields");
                }
            }
            
            System.out.println("Successfully loaded " + roster.size() + " entries from " + filename);
            
        } catch (FileNotFoundException e) {
            System.err.println("CSV file not found: " + filename);
            System.err.println("Using default roster data instead.");
            System.err.println("Make sure " + filename + " is in the correct directory");
            return getDefaultRoster();
        } catch (IOException e) {
            System.err.println("Error reading CSV: " + e.getMessage());
            return getDefaultRoster();
        }
        
        if (roster.isEmpty()) {
            System.err.println("Warning: CSV file was empty, using default data");
            return getDefaultRoster();
        }
        
        return roster;
    }
    
    // Parse a CSV line properly handling quoted fields
    private static String[] parseCSVLine(String line) {
        List<String> result = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            
            if (c == '"') {
                inQuotes = !inQuotes;
            } else if (c == ',' && !inQuotes) {
                result.add(current.toString());
                current = new StringBuilder();
            } else {
                current.append(c);
            }
        }
        result.add(current.toString());
        
        return result.toArray(new String[0]);
    }

    // Fallback data if CSV not found
    private static List<RosterEntry> getDefaultRoster() {
        List<RosterEntry> roster = new ArrayList<>();
        roster.add(new RosterEntry("Player Roster - Quarterbacks", 
            "Lamar Jackson – QB", 
            "Stats:\n• NFL MVP: 2019, 2023\n• Career Passing Yards: 17,000+\n• Career Rushing Yards: 5,000+\n\nDescription:\nElite dual-threat quarterback and centerpiece of the Ravens offense."));
        roster.add(new RosterEntry("Player Roster - Quarterbacks", 
            "Cooper Rush – QB", 
            "Stats:\n• NFL Starts: 10+\n\nDescription:\nVeteran backup quarterback providing experience and stability."));
        roster.add(new RosterEntry("Player Roster - Running Backs", 
            "Derrick Henry – RB", 
            "Stats:\n• Career Rushing Yards: 9,000+\n• Career Rushing TDs: 90+\n\nDescription:\nPower back known for size, speed, and late-game dominance."));
        return roster;
    }

    private static void populateContent(JPanel panel, List<RosterEntry> roster) {
        // Group entries by category
        Map<String, List<RosterEntry>> categorizedRoster = new LinkedHashMap<>();
        for (RosterEntry entry : roster) {
            categorizedRoster.computeIfAbsent(entry.category, k -> new ArrayList<>()).add(entry);
        }

        // Track major sections for proper headers
        String lastMajorSection = "";
        
        // Add sections
        for (Map.Entry<String, List<RosterEntry>> categoryEntry : categorizedRoster.entrySet()) {
            String category = categoryEntry.getKey();
            
            // Determine major section (before the hyphen)
            String majorSection = category.contains(" - ") ? 
                category.substring(0, category.indexOf(" - ")) : category;
            
            // Add major section header if it's new
            if (!majorSection.equals(lastMajorSection)) {
                addSection(panel, majorSection.toUpperCase(), ColorScheme.RAVENS_GOLD);
                lastMajorSection = majorSection;
            }
            
            // Add subsection if there's a subcategory
            if (category.contains(" - ")) {
                String subSection = category.substring(category.indexOf(" - ") + 3);
                addSubsection(panel, subSection);
            }
            
            // Add all entries in this category
            for (RosterEntry entry : categoryEntry.getValue()) {
                // Extract position/role from name if it exists
                String displayName = entry.name;
                String subtitle = "";
                
                if (entry.name.contains("–")) {
                    String[] parts = entry.name.split("–", 2);
                    displayName = parts[0].trim();
                    subtitle = parts[1].trim();
                } else if (entry.name.contains("-")) {
                    String[] parts = entry.name.split("-", 2);
                    displayName = parts[0].trim();
                    subtitle = parts[1].trim();
                }
                
                boolean isPlayer = category.toLowerCase().contains("player");
                addCard(panel, displayName, subtitle, entry.details, isPlayer);
            }
        }
    }

    private static void addSection(JPanel panel, String title, Color color) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setForeground(color);
        label.setBorder(BorderFactory.createEmptyBorder(30, 0, 15, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }

    private static void addSubsection(JPanel panel, String title) {
        JLabel label = new JLabel(title);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(ColorScheme.RAVENS_PURPLE);
        label.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
    }

    private static void addCard(JPanel panel, String name, String subtitle, String details, boolean isPlayer) {
        ExpandableCard card = new ExpandableCard(name, subtitle, details, isPlayer);
        allCards.add(card);
        panel.add(card);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private static void filterContent(String query) {
        String lowerQuery = query.toLowerCase().trim();
        for (ExpandableCard card : allCards) {
            boolean matches = lowerQuery.isEmpty() || 
                card.searchText.toLowerCase().contains(lowerQuery);
            card.setVisible(matches);
        }
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private static void toggleAll(boolean expand) {
        for (ExpandableCard card : allCards) {
            if (card.isVisible()) {
                card.setExpanded(expand);
            }
        }
    }
}