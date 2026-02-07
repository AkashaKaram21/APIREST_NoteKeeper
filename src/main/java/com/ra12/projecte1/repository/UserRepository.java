package com.ra12.projecte1.repository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    // Obtenir usuari a traves de UserRequestDTO (email, al ser unic podem obtenir un usuari)
    public User getUserByUserRequestDTO(UserRequestDTO userRequest) {        
        List<User> users = jdbcTemplate.query("select * from users where email = ?", new UserRowMapper(), userRequest.getEmail());

        return users.isEmpty() ? null : users.get(0);
    }

    // Actualitzem l'usuari ja existent amb els seus atributs
    public void updateUser(User user) {
        jdbcTemplate.update(String.format("update users set name = ?, email = ?, password = ?, imagePath = ? where id = %s", user.getId()), user.getName(), user.getEmail(), user.getPassword(), user.getImagePath());
    }

    // Borrem tots els usuaris
    public void deleteAllUsers() {
        jdbcTemplate.update("delete from users");
    }

    // Borrem un usuari a partir de la seva userId
    public void deleteUser(long userId) {
        jdbcTemplate.update("delete from users where id = ?", userId);
    }

    // Carrega massiva d'usuaris
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
                if (values.length >= 3) {
                    String name = values[0].trim();
                    String email = values[1].trim();
                    String password = values[2].trim();
                    
                    insertUser(new User(name, email, password));
                    count++;
                }
            }
        }
        return count;
    }

    // Obtencio d'una llista de tots els usuaris
    public List<User> findAll() {
        String sql = "select * from users";
        return jdbcTemplate.query(sql, new UserRowMapper());
    }
}