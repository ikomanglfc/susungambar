package com.triyakom.susun.repo;

import com.triyakom.susun.model.SusunGambarUser;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepo extends Repository5_9{

    public SusunGambarUser getOne(String msisdn){
        String query = "select * from trivia_susun_isat_user where msisdn = ?";
        return getTemplate().queryForObject(query, new BeanPropertyRowMapper<>(SusunGambarUser.class), msisdn);
    }

    public SusunGambarUser getOne(int userId){
        String query = "select * from trivia_susun_isat_user where user_id = ?";
        return getTemplate().queryForObject(query, new BeanPropertyRowMapper<>(SusunGambarUser.class), userId);
    }

    public void updateName(int userId, String name) {
        String update = "update trivia_susun_isat_user set [name] = ? where user_id = ?";
        getTemplate().update(update, name, userId);
    }

    public void updateHasPlay(int userId) {
        String update = "update trivia_susun_isat_user set has_play = ? where user_id = ?";
        getTemplate().update(update, false, userId);
    }

    public void updateTutor(int userId, int flagTutor) {
        String update = "update trivia_susun_isat_user set flag_tutor = ? where user_id = ?";
        getTemplate().update(update, flagTutor, userId);
    }

    public void logHint(int userId, long sessionId, int number){
        String insert = "insert into trivia_susun_isat_hint_log " +
                " (user_id, date_log, session_id, number) " +
                " values(?, getdate(), ?, ?)";
        getTemplate().update(insert, userId, sessionId, number);
    }

    public void updateHint (int userId, int hint) {
        String update = "update trivia_susun_isat_user set hint = ? where user_id = ?";
        getTemplate().update(update, hint, userId);
    }

    public void updatePull (int userId, int pull) {
        String update = "update trivia_susun_isat_user set pull = ? where user_id = ?";
        getTemplate().update(update, pull, userId);
    }

    public List<Object[]> getLeaderboard(int userId){
        String query = "exec sp_trivia_susun_hti_leaderboard" + " " + userId ;

        return getTemplate().query(query, arrayObjectMapper());
    }

    public List<Object[]> getWinner(){
        String query = "select top 15 REPLACE(b.msisdn,RIGHT(b.msisdn,4),'XXXX') + ' menang ' as msisdn, case when a.kuota = 300 then '300 MB' when a.kuota = 400 then '400 MB' when a.kuota = 500 then '500 MB' when a.kuota = 600 then '600 MB' when a.kuota = 750 then '750 MB' when a.kuota = 1024 then '1 GB' when a.kuota = 10240 then '10 GB' end as kuota from [dbo].[trivia_susun_isat_user] b inner join [dbo].[trivia_susun_isat_kuota_log] a on a.user_id=b.user_id where kuota != 0 and b.operator = 'hti' order by date_insert desc" ;

        return getTemplate().query(query, arrayObjectMapper());
    }

}
