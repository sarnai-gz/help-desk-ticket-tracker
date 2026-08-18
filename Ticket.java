package src;

/**
 * One help-desk ticket. Each ticket has an ID, a category, a status,
 * and a short summary of the problem.
 */
public class Ticket {
    private final int id;
    private String category;
    private TicketStatus status;
    private String summary;

    public Ticket(int id, String category, TicketStatus status, String summary) {
        this.id = id;
        this.category = category;
        this.status = status;
        this.summary = summary;
    }

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public String getSummary() {
        return summary;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    /** True if the keyword shows up in the ID, category, status, or summary. */
    public boolean matches(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return false;
        }

        String needle = keyword.toLowerCase();
        return String.valueOf(id).contains(needle)
                || category.toLowerCase().contains(needle)
                || status.getLabel().toLowerCase().contains(needle)
                || summary.toLowerCase().contains(needle);
    }

    /** One line used when listing tickets in the terminal. */
    public String toDisplayLine() {
        return String.format("#%-3d  %-12s  %-12s  %s",
                id, category, status.getLabel(), summary);
    }

    /** Pipe-separated line used when saving to the data file. */
    public String toFileLine() {
        return id + "|" + category + "|" + status.name() + "|" + summary;
    }

    /**
     * Rebuilds a ticket from a saved file line.
     * Returns null if the line is blank, a comment, or malformed.
     */
    public static Ticket fromFileLine(String line) {
        if (line == null || line.isBlank() || line.startsWith("#")) {
            return null;
        }

        String[] parts = line.split("\\|", 4);
        if (parts.length != 4) {
            return null;
        }

        try {
            int id = Integer.parseInt(parts[0].trim());
            String category = parts[1].trim();
            TicketStatus status = TicketStatus.valueOf(parts[2].trim());
            String summary = parts[3].trim();
            return new Ticket(id, category, status, summary);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
