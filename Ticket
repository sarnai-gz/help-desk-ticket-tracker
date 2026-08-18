package src;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

/**
 * Command-line help desk tracker: add tickets, change status,
 * search by keyword, and save everything to a local file.
 */
public class HelpDeskApp {
    private static final Path DATA_FILE = Path.of("data", "tickets.txt");
    private static final String[] CATEGORIES = {
            "Hardware", "Software", "Network", "Account", "Printing", "Other"
    };

    private final TicketStore store;
    private final Scanner scanner;

    public HelpDeskApp(TicketStore store, Scanner scanner) {
        this.store = store;
        this.scanner = scanner;
    }

    public static void main(String[] args) {
        TicketStore store = new TicketStore(DATA_FILE);
        try {
            store.load();
        } catch (IOException e) {
            System.out.println("Could not load tickets: " + e.getMessage());
            return;
        }

        try (Scanner scanner = new Scanner(System.in)) {
            new HelpDeskApp(store, scanner).run();
        }
    }

    private void run() {
        System.out.println("Help Desk Ticket Tracker");
        System.out.println("Tickets are saved to " + DATA_FILE);
        System.out.println();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = readLine("Choose an option: ");
            System.out.println();

            switch (choice) {
                case "1" -> addTicket();
                case "2" -> listTickets(store.getAll(), "All tickets");
                case "3" -> searchTickets();
                case "4" -> updateStatus();
                case "5" -> running = false;
                default -> System.out.println("Please enter a number from 1 to 5.");
            }
            System.out.println();
        }

        saveQuietly();
        System.out.println("Tickets saved. Goodbye.");
    }

    private void printMenu() {
        System.out.println("1. Add a ticket");
        System.out.println("2. List all tickets");
        System.out.println("3. Search by keyword");
        System.out.println("4. Update ticket status");
        System.out.println("5. Save and exit");
    }

    private void addTicket() {
        System.out.println("Categories:");
        for (int i = 0; i < CATEGORIES.length; i++) {
            System.out.println("  " + (i + 1) + ". " + CATEGORIES[i]);
        }

        String category = pickCategory();
        if (category == null) {
            System.out.println("Canceled. That was not a valid category.");
            return;
        }

        String summary = readLine("Short summary of the problem: ");
        if (summary.isBlank()) {
            System.out.println("Canceled. A ticket needs a summary.");
            return;
        }

        Ticket ticket = store.add(category, summary);
        saveQuietly();
        System.out.println("Created ticket " + ticket.toDisplayLine());
    }

    private void searchTickets() {
        String keyword = readLine("Keyword: ");
        if (keyword.isBlank()) {
            System.out.println("Enter a word to search for, such as wifi or canvas.");
            return;
        }

        listTickets(store.search(keyword), "Search results for \"" + keyword + "\"");
    }

    private void updateStatus() {
        if (store.getAll().isEmpty()) {
            System.out.println("No tickets yet. Add one first.");
            return;
        }

        listTickets(store.getAll(), "Current tickets");
        int id = readInt("Ticket ID to update: ");
        Ticket ticket = store.findById(id);
        if (ticket == null) {
            System.out.println("No ticket found with ID " + id + ".");
            return;
        }

        System.out.println("Current status: " + ticket.getStatus().getLabel());
        System.out.println("  1. Open");
        System.out.println("  2. In Progress");
        System.out.println("  3. Resolved");

        TicketStatus status = TicketStatus.fromInput(readLine("New status: "));
        if (status == null) {
            System.out.println("Canceled. Status must be Open, In Progress, or Resolved.");
            return;
        }

        ticket.setStatus(status);
        saveQuietly();
        System.out.println("Updated ticket " + ticket.toDisplayLine());
    }

    private void listTickets(List<Ticket> tickets, String heading) {
        System.out.println(heading);
        if (tickets.isEmpty()) {
            System.out.println("  (none)");
            return;
        }

        System.out.printf("  %-4s  %-12s  %-12s  %s%n", "ID", "CATEGORY", "STATUS", "SUMMARY");
        for (Ticket ticket : tickets) {
            System.out.println("  " + ticket.toDisplayLine());
        }
        System.out.println("  " + tickets.size() + " ticket(s)");
    }

    private String pickCategory() {
        String input = readLine("Category number or name: ");
        try {
            int number = Integer.parseInt(input);
            if (number >= 1 && number <= CATEGORIES.length) {
                return CATEGORIES[number - 1];
            }
        } catch (NumberFormatException ignored) {
            for (String category : CATEGORIES) {
                if (category.equalsIgnoreCase(input)) {
                    return category;
                }
            }
        }
        return null;
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        if (!scanner.hasNextLine()) {
            return "";
        }
        return scanner.nextLine().trim();
    }

    private int readInt(String prompt) {
        try {
            return Integer.parseInt(readLine(prompt));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void saveQuietly() {
        try {
            store.save();
        } catch (IOException e) {
            System.out.println("Warning: could not save tickets (" + e.getMessage() + ").");
        }
    }
}
