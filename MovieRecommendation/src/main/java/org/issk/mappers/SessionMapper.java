package org.issk.mappers;

import org.issk.dto.Session;
import org.issk.dto.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SessionMapper implements RowMapper<Session> {
    @Override
    public Session mapRow(ResultSet rs, int rowNum) throws SQLException {
        Session session = new Session();
        session.setSessionId(rs.getString("sessionId"));
        session.setStartTime(rs.getTimestamp("sessionStart").toLocalDateTime());
        session.setPeriodHours(rs.getInt("sessionPeriod"));

        User user = new User();
        user.setUserId(rs.getInt("uid"));
        user.setUsername(rs.getString("username"));

        session.setUser(user);
        return session;
    }
}
