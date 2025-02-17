package ru.job4j.cinema.repository;

import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Ticket;

import java.util.Collection;

public class Sql2oTicketRepository implements TicketRepository {

    private final Sql2o sql2o;

    public Sql2oTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Ticket create(Ticket ticket) {
        try (var connection = sql2o.open()) {
            String query = """
                    INSERT INTO tickets(session_id, row_number, place_number, user_id)
                    VALUES(:sessionId, :rowNumber, :placeNumber, :userId)
                    """;
            int generatedId = connection.createQuery(query).executeUpdate().getResult();
            ticket.setId(generatedId);
            return ticket;
        }
    }

    @Override
    public Ticket findById(int id) {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM tickets WHERE id = :id")
                    .addParameter("id", id);
            return sql.executeAndFetchFirst(Ticket.class);
        }
    }

    @Override
    public boolean update(Ticket ticket) {
        try (var connection = sql2o.open()) {
            String query = """
                    UPDATE tickets
                    SET session_id = :sessionId, row_number = :rowNumber, place_number = :placeNumber, user_id = :userId
                    WHERE id = :id
                    """;
            var sql = connection.createQuery(query)
                    .addParameter("sessionId", ticket.getSessionId())
                    .addParameter("rowNumber", ticket.getRowNumber())
                    .addParameter("placeNumber", ticket.getPlaceNumber())
                    .addParameter("userId", ticket.getUserId());
            sql.executeUpdate();
            return true;
        }
    }

    @Override
    public boolean deleteById(int id) {
        try (var connection = sql2o.open()) {
            var query = connection.createQuery("DELETE FROM tickets WHERE id = :id")
                    .addParameter("id", id);
            query.executeUpdate();
            return true;
        }
    }

    @Override
    public Collection<Ticket> findAll() {
        try (var connection = sql2o.open()) {
            var sql = connection.createQuery("SELECT * FROM tickets");
            return sql.executeAndFetch(Ticket.class);
        }
    }
}
