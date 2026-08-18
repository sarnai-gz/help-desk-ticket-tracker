package src;

/**
 * Status of a support ticket. Using an enum keeps the allowed values
 * limited to Open, In Progress, and Resolved.
 */
public enum TicketStatus {
    OPEN("Open"),
    IN_PROGRESS("In Progress"),
    RESOLVED("Resolved");

    private final String label;

    TicketStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    /**
     * Converts user input like "open", "in progress", or "2" into a status.
     * Returns null if the text does not match a known status.
     */
    public static TicketStatus fromInput(String text) {
        if (text == null) {
            return null;
        }

        String cleaned = text.trim().toLowerCase().replace(' ', '_');

        if (cleaned.equals("1") || cleaned.equals("open")) {
            return OPEN;
        }
        if (cleaned.equals("2") || cleaned.equals("in_progress") || cleaned.equals("in-progress")) {
            return IN_PROGRESS;
        }
        if (cleaned.equals("3") || cleaned.equals("resolved")) {
            return RESOLVED;
        }

        return null;
    }
}
