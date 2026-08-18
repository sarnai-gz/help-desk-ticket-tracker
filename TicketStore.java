package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds the ticket list in memory and saves it to a local text file
 * so tickets are still there the next time the program runs.
 */
public class TicketStore {
    private final Path filePath;
    private final List<Ticket> tickets = new ArrayList<>();
    private int nextId = 1;

    public TicketStore(Path filePath) {
        this.filePath = filePath;
    }

    public List<Ticket> getAll() {
        return tickets;
    }

    public Ticket findById(int id) {
        for (Ticket ticket : tickets) {
            if (ticket.getId() == id) {
                return ticket;
            }
        }
        return null;
    }

    public List<Ticket> search(String keyword) {
        List<Ticket> matches = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.matches(keyword)) {
                matches.add(ticket);
            }
        }
        return matches;
    }

    public Ticket add(String category, String summary) {
        Ticket ticket = new Ticket(nextId, category, TicketStatus.OPEN, summary);
        tickets.add(ticket);
        nextId++;
        return ticket;
    }

    public void load() throws IOException {
        tickets.clear();
        nextId = 1;

        if (!Files.exists(filePath)) {
            return;
        }

        List<String> lines = Files.readAllLines(filePath);
        for (String line : lines) {
            Ticket ticket = Ticket.fromFileLine(line);
            if (ticket != null) {
                tickets.add(ticket);
                if (ticket.getId() >= nextId) {
                    nextId = ticket.getId() + 1;
                }
            }
        }
    }

    public void save() throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        List<String> lines = new ArrayList<>();
        lines.add("# id|category|status|summary");
        for (Ticket ticket : tickets) {
            lines.add(ticket.toFileLine());
        }
        Files.write(filePath, lines);
    }
}
