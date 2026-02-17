package misc;

import classes.RosterEntry;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvParser {
        // Load roster from CSV file
    public static List<RosterEntry> loadRosterFromCSV(String filename) {
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
    public static String[] parseCSVLine(String line) {
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
    public static List<RosterEntry> getDefaultRoster() {
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
}
