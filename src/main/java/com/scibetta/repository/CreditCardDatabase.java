package com.scibetta.repository;

import com.scibetta.model.CreditCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class CreditCardDatabase {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public CreditCardDatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<CreditCard> rowMapper = (rs, rowNumber) -> {
        CreditCard creditCard = new CreditCard();
        creditCard.setId(rs.getInt("id"));
        creditCard.setNumber(rs.getInt("number"));
        creditCard.setBalance(rs.getInt("balance"));
        creditCard.setOwner(rs.getInt("owner"));
        creditCard.setStatus(rs.getInt("status"));
        return creditCard;
    };

    public List<CreditCard> selectAll() {
        String sql = "SELECT * FROM creditcard";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<CreditCard> selectById(int id) {
        String sql = "SELECT * FROM creditcard WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public Optional<CreditCard> selectByNumber(int number) {
        String sql = "SELECT * FROM creditcard WHERE number = ?";
        return jdbcTemplate.query(sql, rowMapper, number).stream().findFirst();
    }

    public void insert(CreditCard creditCard) {
        String sql =
                """
                INSERT into creditcard (number, balance, owner, status)
                VALUES (?, ?, ?, ?);
                """;
        jdbcTemplate.update(sql,
                creditCard.getId(),
                creditCard.getNumber(),
                creditCard.getBalance(),
                creditCard.getOwner(),
                creditCard.getStatus()
        );
    }

    public void insertWithoutOwner(CreditCard creditCard) {
        String sql =
                """
                INSERT into creditcard (number, balance, status)
                VALUES (?, ?, ?);
                """;
        jdbcTemplate.update(sql,
                // creditCard.getId(),
                creditCard.getNumber(),
                creditCard.getBalance(),
                // creditCard.getOwner(),
                creditCard.getStatus()
        );
    }

    public void updateById(int id, CreditCard creditCard) {
        String sql = "UPDATE creditcard SET id = ?, number = ?, balance = ?, owner = ?, status = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                creditCard.getId(),
                creditCard.getNumber(),
                creditCard.getBalance(),
                creditCard.getOwner(),
                creditCard.getStatus(),
                id
        );
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM creditcard WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
