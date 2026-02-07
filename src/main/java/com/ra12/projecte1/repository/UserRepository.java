package com.ra12.projecte1.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.ra12.projecte1.dto.UserRequestDTO;
import com.ra12.projecte1.model.User;

@Repository
public class UserRepository {
    @Autowired
    JdbcTemplate jdbcTemplate;

    // Custom RowMapper per tornar tots els resultats d'una query de SQL en una llista d'usuaris
    private static final class UserRowMapper implements RowMapper<User> {

        @Override
        public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setName(resultSet.getString("name"));
            user.setEmail(resultSet.getString("email"));
            user.setPassword(resultSet.getString("password"));

            return user;
        }
    }

    // Funcio de debug perque no tinc el codi d'afegir / obtenir usuaris
    public void insertUser(User user) {
        jdbcTemplate.update("insert into users (name, email, password, imagePath) values (?, ?, ?, ?)", user.getName(), user.getEmail(), user.getPassword(), user.getImagePath());
    }

    // Funcio de debug perque no tinc el codi d'afegir / obtenir usuaris
    public User getUserById(long userId) {        
        List<User> users = jdbcTemplate.query("select * from users where id = ?", new UserRowMapper(), userId);
            
        return users.isEmpty() ? null : users.get(0);
    }

    // Obtenir 
    public User getUserByUserRequestDTO(UserRequestDTO userRequest) {        
        List<User> users = jdbcTemplate.query("select * from users where email = ?", new UserRowMapper(), userRequest.getEmail());

        return users.isEmpty() ? null : users.get(0);
    }

    public void updateUser(User user) {
        jdbcTemplate.update(String.format("update users set name = ?, email = ?, password = ?, imagePath = ? where id = %s", user.getId()), user.getName(), user.getEmail(), user.getPassword(), user.getImagePath());
    }

    public void deleteAllUsers() {
        jdbcTemplate.update("delete from users");
    }

    public void deleteUser(long userId) {
        jdbcTemplate.update("delete from users where id = ?", userId);
    }
}