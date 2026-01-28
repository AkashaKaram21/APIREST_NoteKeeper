package com.ra12.projecte1.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ra12.projecte1.model.User;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final RowMapper<User> userRowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            return user;
        }
    };

    public int loadFromCSV(String filePath) throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            
            while ((line = br.readLine()) != null) {
                // Saltar la primera línea si es el encabezado
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                String[] values = line.split(",");
                if (values.length >= 2) {
                    String email = values[0].trim();
                    String password = values[1].trim();
                    
                    save(new User(email, password));
                    count++;
                }
            }
        }
        return count;
    }

    public int save(User user) {
        String sql = "INSERT INTO users (email, password) VALUES (?, ?) " +
                     "ON CONFLICT (email) DO UPDATE SET password = EXCLUDED.password";
        return jdbcTemplate.update(sql, user.getEmail(), user.getPassword());
    }

    public List<User> findAll() {
        String sql = "SELECT email, password FROM users";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT email, password FROM users WHERE email = ?";
        List<User> users = jdbcTemplate.query(sql, userRowMapper, email);
        return users.isEmpty() ? Optional.empty() : Optional.of(users.get(0));
    }
}