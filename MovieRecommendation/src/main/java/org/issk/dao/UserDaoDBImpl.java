package org.issk.dao;

import org.issk.dto.User;
import org.issk.mappers.UserMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDaoDBImpl implements UserDao {

    private final JdbcTemplate jdbcTemplate;

    public UserDaoDBImpl(JdbcTemplate jdbcTemplate) {
        //Connect to DB
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> createUser(User user){
        //Check if user is in DB
        return jdbcTemplate.query("SELECT * FROM users;",new UserMapper());

    }

    public void println(){
        System.out.println("here");
    }
}
