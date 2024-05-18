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
        //TODO incorporate user into session
        //session.setUser(new UserMapper());
        session.setStartTime(rs.getTimestamp("sessionStart").toLocalDateTime());
        session.setPeriodHours(rs.getInt("sessionPeriod"));
        return session;
    }
}
