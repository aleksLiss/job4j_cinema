package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> create(Ticket ticket);

    Optional<Ticket> findById(int id);

    boolean update(Ticket ticket);

    boolean deleteById(int id);

    Collection<Ticket> findAll();
}
