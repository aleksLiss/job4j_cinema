package service;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.Collection;
import java.util.Optional;

public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    Optional<Ticket> create(Ticket ticket) {
        return ticketRepository.create(ticket);
    };

    Optional<Ticket> findById(int id) {
        return ticketRepository.findById(id);
    };

    Collection<Ticket> findAll() {
        return ticketRepository.findAll();
    };

    boolean update(Ticket ticket) {
        return ticketRepository.update(ticket);
    };

    boolean deleteById(int id) {
        return ticketRepository.deleteById(id);
    };
}
