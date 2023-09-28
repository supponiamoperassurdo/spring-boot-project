package com.scibetta.repository;

import java.util.List;
import java.util.Optional;
import com.scibetta.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
@Repository
public class UserDatabase {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDatabase(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    RowMapper<User> rowMapper = (rs, rowNumber) -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setFirstname(rs.getString("firstname"));
        user.setLastname(rs.getString("lastname"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setStorename(rs.getString("storename"));
        return user;
    };

    public List<User> selectAll() {
        String sql = "SELECT * FROM user";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Optional<User> selectById(int id) {
        String sql = "SELECT * FROM user WHERE id = ?";
        return jdbcTemplate.query(sql, rowMapper, id).stream().findFirst();
    }

    public Optional<User> selectByUsername(String username) {
        String sql = "SELECT * FROM user WHERE username = ?";
        return jdbcTemplate.query(sql, rowMapper, username).stream().findFirst();
    }

    public void insert(User user) {
        String sql =
            """
            INSERT into user (firstname, lastname, username, password, role, storename)
            VALUES (?, ?, ?, ?, ?, ?);
            """;
        jdbcTemplate.update(sql,
                user.getFirstname(),
                user.getLastname(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getStorename()
        );
    }

    public void updateById(int id, User user) {
        String sql = "UPDATE user SET id = ?, firstname = ?, lastname = ?, username = ?, password = ?, role = ?, storename = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                user.getUsername(),
                user.getPassword(),
                user.getRole(),
                user.getStorename(),
                id
        );
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM user WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

}
