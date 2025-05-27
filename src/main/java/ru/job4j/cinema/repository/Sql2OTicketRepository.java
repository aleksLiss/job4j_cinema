package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import ru.job4j.cinema.model.Ticket;

import java.util.Collection;
import java.util.Optional;

@Repository
@ThreadSafe
public class Sql2OTicketRepository implements TicketRepository {

    private final Sql2o sql2o;

    public Sql2OTicketRepository(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Collection<Ticket> getAll() {
        String sql = "SELECT * FROM tickets";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                return query.setColumnMappings(Ticket.COLUMN_MAPPING).executeAndFetch(Ticket.class);
            }
        }
    }

    @Override
    public Optional<Ticket> getById(int id) {
        String sql = "SELECT * FROM tickets WHERE id = :id";
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                Ticket foundTicket = query
                        .setColumnMappings(Ticket.COLUMN_MAPPING)
                        .addParameter("id", id)
                        .executeAndFetchFirst(Ticket.class);
                return Optional.ofNullable(foundTicket);
            }
        }
    }

    @Override
    public Optional<Ticket> save(Ticket ticket) {
        String sql = """
                INSERT INTO tickets(id, session_id, row_number, place_number, user_id)
                VALUES(:id, :sessionId, :rowNumber, :placeNumber, :userId)
                """;
        try (Connection connection = sql2o.open()) {
            try (Query query = connection.createQuery(sql)) {
                query
                        .addParameter("id", ticket.getId())
                        .addParameter("sessionId", ticket.getSessionId())
                        .addParameter("rowNumber", ticket.getRowNumber())
                        .addParameter("placeNumber", ticket.getPlaceNumber())
                        .addParameter("userId", ticket.getUserId());
                int generatedKey = query.executeUpdate().getKey(Integer.class);
                ticket.setId(generatedKey);
                return Optional.ofNullable(ticket);
            }
        }
    }

    @Override
    public boolean update(Ticket ticket) {
        return false;
    }

    @Override
    public boolean deleteById(int id) {
        return false;
    }
}
