package com.scibetta.repository;

import com.scibetta.model.CreditCardTransactionUserView;
import com.scibetta.model.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Component
@Repository
public class TransactionDatabase {

    private final JdbcTemplate jdbcTemplate;
    private final UserDatabase userDatabase;

    @Autowired
    public TransactionDatabase(JdbcTemplate jdbcTemplate, UserDatabase userDatabase) {
        this.jdbcTemplate = jdbcTemplate;
        this.userDatabase = userDatabase;
    }

    RowMapper<Transaction> rowMapper = (rs, rowNumber) -> {
        Transaction transaction = new Transaction();
        transaction.setId(rs.getInt("id"));
        transaction.setDescription(rs.getString("description"));
        transaction.setUserid(rs.getInt("userid"));
        transaction.setSellerid(rs.getInt("sellerid"));
        transaction.setDate(rs.getTimestamp("date"));
        transaction.setOperation(rs.getInt("operation"));
        transaction.setCreditcardnumber(rs.getInt("creditcardnumber"));
        return transaction;
    };

    public List<Transaction> selectAll() {
        String sql = "SELECT * FROM transaction";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // è necessario creare un mapper diverso per il JOIN tra le table
    RowMapper<CreditCardTransactionUserView> rowMapperSelectAllJoinUser = (rs, rowNumber) -> {
        CreditCardTransactionUserView creditCardTransactionUserView = new CreditCardTransactionUserView();
        creditCardTransactionUserView.setTid(rs.getInt("id"));
        creditCardTransactionUserView.setDescription(rs.getString("description"));
        creditCardTransactionUserView.setOperation(rs.getInt("operation"));
        creditCardTransactionUserView.setDate(rs.getTimestamp("date"));
        creditCardTransactionUserView.setFirstname(rs.getString("firstname"));
        creditCardTransactionUserView.setLastname(rs.getString("lastname"));
        creditCardTransactionUserView.setStorename(rs.getString("storename"));
        creditCardTransactionUserView.setNumber(rs.getInt("number"));
        creditCardTransactionUserView.setCreditcardnumber(rs.getInt("creditcardnumber"));
        return creditCardTransactionUserView;
    };

    public List<CreditCardTransactionUserView> selectAllJoinUserForSeller(Authentication authentication) { /* per evitare di fare due mapper differenti, setto a 0 la colonna "number" per la lista transizioni del venditore */
        String username = authentication.getName();
        String sql =
                """
                SELECT t.id, description, operation, date, firstname, lastname, storename, creditcardnumber, 0 as number
                FROM transaction AS t JOIN user AS u ON t.userid = u.id
                WHERE t.sellerid = ?
                """;
        int sellerid = userDatabase.selectByUsername(username).get().getId(); // recupero l'id del negoziante autenticato per visualizzare le sue transazioni
        return jdbcTemplate.query(sql, rowMapperSelectAllJoinUser, sellerid);
    }

    public List<CreditCardTransactionUserView> selectAllJoinUserForUser(Authentication authentication) {
        String username = authentication.getName();
        String sql =
                """
                SELECT t.id, description, operation, date, firstname, lastname, storename, number, creditcardnumber
                FROM transaction AS t
                    JOIN user as u ON t.sellerid = u.id
                    JOIN creditcard AS c ON c.owner = t.userid
                WHERE t.creditcardnumber = c.number AND c.owner = ?
                """;
        int userid = userDatabase.selectByUsername(username).get().getId(); // recupero l'id dell'utente autenticato che sarà il proprietario della carta che deve visualizzare i moviementi
        return jdbcTemplate.query(sql, rowMapperSelectAllJoinUser, userid);
    }

    public Optional<Transaction> selectById(int id) {
        String sql = "SELECT * FROM transaction WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public Optional<Transaction> selectByUserId(int userid) {
        String sql = "SELECT * FROM transaction WHERE userid = ?";
        return jdbcTemplate.query(sql, rowMapper, userid).stream().findFirst();
    }

    public void insert(Transaction transaction) {
        String sql =
                """
                INSERT into transaction (description, userid, sellerid, date, operation, creditcardnumber)
                VALUES (?, ?, ?, ?, ?, ?);
                """;
        jdbcTemplate.update(sql,
                transaction.getDescription(),
                transaction.getUserid(),
                transaction.getSellerid(),
                transaction.getDate(),
                transaction.getOperation(),
                transaction.getCreditcardnumber()
        );
    }

    public int deleteById(int id) {
        String sql = "DELETE FROM transaction WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
