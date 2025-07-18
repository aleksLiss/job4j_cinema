package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {

    Collection<Ticket> getAll();

    Optional<Ticket> getById(int id);

    Optional<Ticket> save(Ticket ticket);

    boolean deleteById(int id);

}
