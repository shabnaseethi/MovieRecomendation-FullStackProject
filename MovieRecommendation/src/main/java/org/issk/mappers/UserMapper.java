package org.issk.mappers;

import org.issk.dto.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("uid"));
        user.setPassword(rs.getString("passwordHash"));
        user.setUsername(rs.getString("userName"));
        return user;
    }
}
