package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;

import java.util.Collection;

public interface TicketRepository {

    Ticket create(Ticket ticket);

    Ticket findById(int id);

    boolean update(Ticket ticket);

    boolean deleteById(int id);

    Collection<Ticket> findAll();
}
