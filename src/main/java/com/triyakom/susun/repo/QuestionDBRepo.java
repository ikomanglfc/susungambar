package com.triyakom.susun.repo;

import com.triyakom.susun.model.LuckySpin;
import com.triyakom.susun.model.QuestDB;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

@Repository
public class QuestionDBRepo extends Repository5_9 implements QuestionRepo {

    @Override
    public QuestDB loadQuestion(long sessionId, int number) {
        String query = "exec sp_trivia_susun_isat_quest " + sessionId + ", " + number;
        QuestDB quest = template.queryForObject(query, new BeanPropertyRowMapper<QuestDB>(QuestDB.class));
        return quest;
    }

    @Override
    public long initPlay(int userId, String source){
        String insert = "insert into trivia_susun_isat_play_session(date_insert, user_id, source) values(getdate(), ?, ?)";
        long sessionId = insertAndGetKey(insert, "session_id", userId, source).longValue();
        return sessionId;
    }

    @Override
    public void updateSession(long sessionId, String reason, int kuota) {
        String update = "update trivia_susun_isat_play_session set end_reason = ?, kuota = ? where session_id = ?";
        getTemplate().update(update, reason, kuota, sessionId);
    }

    @Override
    public void logQuest(long sessionId, int questionId, String answer, long duration, int number){
        String insert = "insert into trivia_susun_isat_play_log " +
                " (answer, date_insert, duration, number, question_id, session_id) " +
                " values(?, getdate(), ?, ?, ?, ?)";
        getTemplate().update(insert, answer, duration, number, questionId, sessionId);
    }

    @Override
    public void sendKuota(long sessionId, int kuota){
        try (Connection connection = getTemplate().getDataSource().getConnection(); Statement stmt = connection.createStatement()) {
            stmt.execute("exec sp_trivia_susun_hti_send_kuota " + sessionId + ", " + kuota);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public int checkCheater(long sessionId, int kuota, String ipAddress, String userAgent) {
        SimpleJdbcCall call = new SimpleJdbcCall(getTemplate().getDataSource())
        .withProcedureName("sp_trivia_susun_isat_cheater");
        SqlParameterSource param = new MapSqlParameterSource().addValue("sessionId", sessionId)
                .addValue("kuota", kuota)
                .addValue("ipAddress", ipAddress)
                .addValue("userAgent", userAgent);
        Map<String, Object> output = call.execute(param);

        int result = (Integer) output.get("result");
        return result;
    }

    @Override
    public LuckySpin checkEligible(int userId) {
        String query = "select top 1 b.msisdn, a.session_id from [dbo].[trivia_susun_isat_play_session] a inner join  [dbo].[trivia_susun_isat_user] b on a.user_id = b.user_id where kuota >= 500 and a.user_id = ? and a.date_insert between dateadd(dd, 0, DATEDIFF(dd, 0, GETDATE())) and dateadd(day,0, GETDATE()) order by session_id asc";

        try {
            return getTemplate().queryForObject(query, new BeanPropertyRowMapper<>(LuckySpin.class), userId);
        }
        catch (Exception x) {
            return null;
        }
    }
}
