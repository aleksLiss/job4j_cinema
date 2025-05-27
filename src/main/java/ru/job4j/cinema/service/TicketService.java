package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

public interface TicketService {

    Collection<Ticket> getAll();

    Optional<Ticket> getById(int id);

    Optional<Ticket> save(Ticket ticket);
}
