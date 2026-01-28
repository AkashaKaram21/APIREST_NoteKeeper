package com.ra12.projecte1.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper; 
import org.springframework.stereotype.Repository;

import com.ra12.projecte1.logging.UserLogging;
import com.ra12.projecte1.model.User; 

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserLogging userLogging; 

    // Mapear
    private static final class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            user.setPassword(rs.getString("password"));
            user.setUltimAcces(rs.getTimestamp("ultim_acces"));  
            user.setDataCreated(rs.getTimestamp("data_created"));  
            user.setDataUpdated(rs.getTimestamp("data_updated"));  
            return user;
        }
    }

    // Crear usuario
    public int insertUser(User user) {
        userLogging.logInfo(
            "UserRepository", 
            "insertUser", 
            "Executant INSERT: INSERT INTO users (name, email, password, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?)"
        );
        
        try {
            String sql = "INSERT INTO users (name, email, password, dataCreated, dataUpdated) VALUES (?, ?, ?, ?, ?)";
            int result = jdbcTemplate.update(sql,
                    user.getName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getDataCreated(),
                    user.getDataUpdated()); 
            
            userLogging.logInfo(
                "UserRepository", 
                "insertUser", 
                "INSERT executat correctament. Usuari creat."
            );
            return result;
        } catch (Exception e) {
            userLogging.logError("UserRepository", "insertUser", "Error al insertar usuari", e);
            throw e;
        }
    } // Añadido cierre de método

    // Obtener todos los usuarios creados 
    public List<User> getAllUsers() {
        userLogging.logInfo(
            "UserRepository", 
            "getAllUsers", 
            "Executant consulta: SELECT * FROM users ORDER BY id"
        );
        
        try {
            String sql = "SELECT * FROM users ORDER BY id";
            List<User> users = jdbcTemplate.query(sql, new UserRowMapper());
            
            userLogging.logInfo(
                "UserRepository", 
                "getAllUsers", 
                "Consulta executada correctament. S'han trobat " + users.size() + " usuaris"
            );
            
            return users;
        } catch (Exception e) {
            userLogging.logError(
                "UserRepository", 
                "getAllUsers", 
                "Error executant consulta SELECT * FROM users ORDER BY id", 
                e
            );
            throw e;
        }
    }

    // Obtener usuario por ID 
    public User getUserById(long userId) {
        userLogging.logInfo(
            "UserRepository", 
            "getUserById", 
            "Executant consulta: SELECT * FROM users WHERE id = " + userId
        );
        
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            List<User> users = jdbcTemplate.query(sql, new UserRowMapper(), userId);
            
            User result = users.isEmpty() ? null : users.get(0);
            
            userLogging.logInfo(
                "UserRepository", 
                "getUserById", 
                "Consulta executada correctament. Resultat: " + (result != null ? "trobat" : "no trobat")
            );
            
            return result;
        } catch (Exception e) {
            userLogging.logError(
                "UserRepository", 
                "getUserById", 
                "Error executant consulta SELECT * FROM users WHERE id = " + userId, 
                e
            );
            throw e;
        }
    }
}